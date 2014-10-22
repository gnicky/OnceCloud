#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "Frozen.h"

const char * PluginName="Firewall";
const char * PluginVersion="1.0.0.0";

void GenerateFirewallRuleList(char * buffer, struct FirewallRule * firewallRule, int count);

int Initialize()
{
	return 0;
}

int Destroy()
{
	return 0;
}

void GenerateFirewallRuleList(char * buffer, struct FirewallRule * firewallRule, int count)
{
	int i=0;
	buffer[0]='\0';
	strcat(buffer,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	strcat(buffer,"<ListFirewallRulesResult>\n");
	strcat(buffer,"\t<Rules>\n");
	for(i=0;i<count;i++)
	{
		strcat(buffer,"\t\t<Rule>\n");
		strcat(buffer,"\t\t\t<Protocol>");
		strcat(buffer,firewallRule[i].Protocol);
		strcat(buffer,"</Protocol>\n");
		strcat(buffer,"\t\t\t<InternalIPRange>");
		strcat(buffer,firewallRule[i].InternalIPRange);
		strcat(buffer,"</InternalIPRange>\n");
		strcat(buffer,"\t\t\t<ExternalIPRange>");
		strcat(buffer,firewallRule[i].ExternalIPRange);
		strcat(buffer,"</ExternalIPRange>\n");
		strcat(buffer,"\t\t\t<Port>");
		strcat(buffer,firewallRule[i].Port);
		strcat(buffer,"</Port>\n");
		strcat(buffer,"\t\t</Rule>\n");
	}
	strcat(buffer,"\t</Rules>\n");
	strcat(buffer,"</ListFirewallRulesResult>\n");
}

void GeneratePingRuleList(char * buffer, struct PingRule * pingRule, int count)
{	
	int i=0;
	buffer[0]='\0';
	strcat(buffer,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	strcat(buffer,"<ListPingRulesResult>\n");
	strcat(buffer,"\t<Rules>\n");
	for(i=0;i<count;i++)
	{
		strcat(buffer,"\t\t<Rule>\n");
		strcat(buffer,"\t\t\t<TargetIPRange>");
		strcat(buffer,pingRule[i].TargetIPRange);
		strcat(buffer,"</TargetIPRange>\n");
		strcat(buffer,"\t\t\t<FromIPRange>");
		strcat(buffer,pingRule[i].FromIPRange);
		strcat(buffer,"</FromIPRange>\n");
		strcat(buffer,"\t\t</Rule>\n");
	}
	strcat(buffer,"\t</Rules>\n");
	strcat(buffer,"</ListPingRulesResult>\n");
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->QueryString!=NULL)
	{
		if(strcmp(request->QueryString,"ping")==0)
		{
			int pingRuleCount=0;
			struct PingRule pingRule[300];

			ListPingRule(pingRule,&pingRuleCount);

			char * pingRuleBuffer=malloc(65536);
			GeneratePingRuleList(pingRuleBuffer,pingRule,pingRuleCount);

			response->StatusCode=200;
			response->SetHeader(response,"Content-Type","application/xml");
			response->SetContent(response,pingRuleBuffer);

			free(pingRuleBuffer);
			return TRUE;	
		}
	}

	int firewallRuleCount=0;
	struct FirewallRule firewallRule[300];

	ListFirewallRule(firewallRule,&firewallRuleCount);

	char * firewallRuleBuffer=malloc(65536);
	GenerateFirewallRuleList(firewallRuleBuffer,firewallRule,firewallRuleCount);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,firewallRuleBuffer);

	free(firewallRuleBuffer);
	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->QueryString!=NULL)
	{
		if(strcmp(request->QueryString,"ping")==0)
		{
			const char * targetIPRange=request->GetHeader(request,"x-bws-target-ip-range");
			const char * fromIPRange=request->GetHeader(request,"x-bws-from-ip-range");

			if(targetIPRange==NULL)
			{
				char ErrorMessage[]=
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					"<Error>\n\tPlease specify Target IP Range.\n</Error>\n";

				response->StatusCode=400;
				response->SetHeader(response,"Content-Type","application/xml");
				response->SetContent(response,ErrorMessage);
				return TRUE;
			}

			AllowPing(targetIPRange,fromIPRange);

			response->StatusCode=200;
			response->SetContent(response,"");

			return TRUE;
		}
	}

	const char * protocol=request->GetHeader(request,"x-bws-protocol");
	const char * internalIPRange=request->GetHeader(request,"x-bws-internal-ip-range");
	const char * externalIPRange=request->GetHeader(request,"x-bws-external-ip-range");
	const char * port=request->GetHeader(request,"x-bws-port");

	if(protocol==NULL || internalIPRange==NULL || port==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Protocol, Internal IP Range and Port.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddRule(protocol,internalIPRange,externalIPRange,port);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int ReadTextValue(struct json_token * object, const char * key, char * buffer)
{
	const struct json_token * token;
	token=find_json_token(object,key);
	if(token==NULL)
	{
		return 1;
	}
	memcpy(buffer,token->ptr,token->len);
	buffer[token->len]='\0';
	return 0;
}

int ParseRequest(const char * json, struct FirewallConfiguration * configuration)
{
	struct json_token * object;
	const struct json_token * token;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return 1;
	}

	int status;
	int i=0;
	while(1)
	{
		char ipIndex[100];
		sprintf(ipIndex,"IP[%d]",i);
		token=find_json_token(object,ipIndex);
		if(token==NULL)
		{
			break;
		}

		status=ReadTextValue(object,ipIndex,configuration->FromIPAddress[i]);
		if(status!=0)
		{
			return 1;
		}
		i++;
	}
	configuration->IPCount=i;

	i=0;
	while(1)
	{
		char ruleIndex[100];
		char temp[100];

		sprintf(ruleIndex,"rules[%d]",i);
		token=find_json_token(object,ruleIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.protocol",ruleIndex);
		status=ReadTextValue(object,temp,configuration->Rules[i].Protocol);
		if(status!=0)
		{
			return 1;
		}

		sprintf(temp,"%s.IP",ruleIndex);
		status=ReadTextValue(object,temp,configuration->Rules[i].ToIPAddress);
		if(status!=0)
		{
			return 1;
		}

		if(strcmp(configuration->Rules[i].Protocol,"tcp")==0 || strcmp(configuration->Rules[i].Protocol,"udp")==0)
		{
			char number[100];

			sprintf(temp,"%s.startPort",ruleIndex);
			status=ReadTextValue(object,temp,number);
			sscanf(number,"%d",&(configuration->Rules[i].StartPort));
			if(status!=0)
			{
				return 1;
			}
		
			sprintf(temp,"%s.endPort",ruleIndex);
			status=ReadTextValue(object,temp,number);
			sscanf(number,"%d",&(configuration->Rules[i].EndPort));
			if(status!=0)
			{
				return 1;
			}		
		}
		

		i++;
	}

	configuration->RuleCount=i;

	free(object);
	return 0;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->Content==NULL || strlen(request->Content)==0)
	{
		InitializeFirewall();

		response->StatusCode=200;
		response->SetContent(response,"");

		return TRUE;
	}

	struct FirewallConfiguration configuration;
	int ret=ParseRequest(request->Content,&configuration);
	if(ret!=0)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	SetFirewallRules(&configuration);
	
	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->QueryString!=NULL)
	{
		if(strcmp(request->QueryString,"ping")==0)
		{
			const char * targetIPRange=request->GetHeader(request,"x-bws-target-ip-range");
			const char * fromIPRange=request->GetHeader(request,"x-bws-from-ip-range");

			if(targetIPRange==NULL)
			{
				char ErrorMessage[]=
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					"<Error>\n\tPlease specify Target IP Range.\n</Error>\n";

				response->StatusCode=400;
				response->SetHeader(response,"Content-Type","application/xml");
				response->SetContent(response,ErrorMessage);
				return TRUE;
			}

			DenyPing(targetIPRange,fromIPRange);

			response->StatusCode=200;
			response->SetContent(response,"");

			return TRUE;
		}
	}

	const char * protocol=request->GetHeader(request,"x-bws-protocol");
	const char * internalIPRange=request->GetHeader(request,"x-bws-internal-ip-range");
	const char * externalIPRange=request->GetHeader(request,"x-bws-external-ip-range");
	const char * port=request->GetHeader(request,"x-bws-port");

	if(protocol==NULL || internalIPRange==NULL || port==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Protocol, Internal IP Range and Port.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	RemoveRule(protocol,internalIPRange,externalIPRange,port);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}
