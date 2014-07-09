#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"

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
	strcat(buffer,"<ListAllRulesResult>\n");
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
	strcat(buffer,"</ListAllRulesResult>\n");
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	int entryCount=0;
	struct FirewallRule firewallRule[300];

	ListFirewallRule(firewallRule,&entryCount);

	char * buffer=malloc(65536);
	GenerateFirewallRuleList(buffer,firewallRule,entryCount);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,buffer);

	free(buffer);
	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	int entryCount=0;
	struct FirewallRule firewallRule[300];

	ListFirewallRule(firewallRule,&entryCount);

	char * buffer=malloc(65536);
	GenerateFirewallRuleList(buffer,firewallRule,entryCount);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,buffer);

	free(buffer);
	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
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
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	InitializeFirewallRule();

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
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
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}
