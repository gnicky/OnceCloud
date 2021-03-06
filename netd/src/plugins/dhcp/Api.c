#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "Parser.h"
#include "Logger.h"
#include "Frozen.h"

const char * PluginName="DHCP";
const char * PluginVersion="1.0.0.0";

int Initialize()
{
	return 0;
}

int Destroy()
{
	return 0;
}

void GenerateDhcpConfigurationXml(char * buffer, struct DhcpConfiguration * configuration)
{
	int i=0;

	buffer[0]='\0';
	strcat(buffer,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

	strcat(buffer,"<DhcpConfiguration>\n");

	strcat(buffer,"\t<GlobalConfiguration>\n");
	for(i=0;i<configuration->GlobalConfigurationCount;i++)
	{
		strcat(buffer,"\t\t<Option>\n");
		strcat(buffer,"\t\t\t<Key>");
		strcat(buffer,configuration->GlobalConfiguration[i].Key);
		strcat(buffer,"</Key>\n");
		strcat(buffer,"\t\t\t<Value>");
		strcat(buffer,configuration->GlobalConfiguration[i].Value);
		strcat(buffer,"</Value>\n");
		strcat(buffer,"\t\t</Option>\n");
	}
	strcat(buffer,"\t</GlobalConfiguration>\n");

	strcat(buffer,"\t<SubnetConfiguration>\n");
	for(i=0;i<configuration->SubnetConfigurationCount;i++)
	{
		strcat(buffer,"\t\t<Subnet>\n");
		strcat(buffer,"\t\t\t<SubnetAddress>");
		strcat(buffer,configuration->SubnetConfiguration[i].SubnetAddress);
		strcat(buffer,"</SubnetAddress>\n");
		strcat(buffer,"\t\t\t<Netmask>");
		strcat(buffer,configuration->SubnetConfiguration[i].Netmask);
		strcat(buffer,"</Netmask>\n");
		strcat(buffer,"\t\t\t<Routers>");
		strcat(buffer,configuration->SubnetConfiguration[i].Routers);
		strcat(buffer,"</Routers>\n");
		strcat(buffer,"\t\t\t<SubnetMask>");
		strcat(buffer,configuration->SubnetConfiguration[i].SubnetMask);
		strcat(buffer,"</SubnetMask>\n");
		strcat(buffer,"\t\t\t<DomainNameServers>");
		strcat(buffer,configuration->SubnetConfiguration[i].DomainNameServers);
		strcat(buffer,"</DomainNameServers>\n");
		strcat(buffer,"\t\t\t<Range>\n");
		strcat(buffer,"\t\t\t\t<Start>");
		strcat(buffer,configuration->SubnetConfiguration[i].RangeStart);
		strcat(buffer,"</Start>\n");
		strcat(buffer,"\t\t\t\t<End>");
		strcat(buffer,configuration->SubnetConfiguration[i].RangeEnd);
		strcat(buffer,"</End>\n");
		strcat(buffer,"\t\t\t</Range>\n");
		strcat(buffer,"\t\t\t<DefaultLeaseTime>");
		strcat(buffer,configuration->SubnetConfiguration[i].DefaultLeaseTime);
		strcat(buffer,"</DefaultLeaseTime>\n");
		strcat(buffer,"\t\t\t<MaxLeaseTime>");
		strcat(buffer,configuration->SubnetConfiguration[i].MaxLeaseTime);
		strcat(buffer,"</MaxLeaseTime>\n");
		strcat(buffer,"\t\t</Subnet>\n");
	}
	strcat(buffer,"\t</SubnetConfiguration>\n");

	strcat(buffer,"\t<HostConfiguration>\n");
	for(i=0;i<configuration->HostConfigurationCount;i++)
	{
		strcat(buffer,"\t\t<Host>\n");
		strcat(buffer,"\t\t\t<Name>");
		strcat(buffer,configuration->HostConfiguration[i].Name);
		strcat(buffer,"</Name>\n");
		strcat(buffer,"\t\t\t<HardwareAddress>");
		strcat(buffer,configuration->HostConfiguration[i].HardwareAddress);
		strcat(buffer,"</HardwareAddress>\n");
		strcat(buffer,"\t\t\t<IPAddress>");
		strcat(buffer,configuration->HostConfiguration[i].IPAddress);
		strcat(buffer,"</IPAddress>\n");
		strcat(buffer,"\t\t</Host>\n");
	}
	strcat(buffer,"\t</HostConfiguration>\n");

	strcat(buffer,"</DhcpConfiguration>\n");
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	WriteLog(LOG_NOTICE,"GET /DHCP");
	char * buffer=malloc(1048576);
	struct DhcpConfiguration configuration;
	ReadDhcpConfiguration(&configuration);
	GenerateDhcpConfigurationXml(buffer,&configuration);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,buffer);

	free(buffer);
	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
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

int DoAddHosts(const char * json)
{
	struct DhcpConfiguration configuration;
	ReadDhcpConfiguration(&configuration);

	struct json_token * object;
	const struct json_token * token;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return FALSE;
	}

	int status=0;
	int i=0;
	int ret=FALSE;

	while(1)
	{
		char hostIndex[100];
		char temp[100];
		char hardwareAddress[100];
		char ipAddress[100];

		sprintf(hostIndex,"hosts[%d]",i);
		token=find_json_token(object,hostIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.hardwareAddress",hostIndex);
		status=ReadTextValue(object,temp,hardwareAddress);
		if(status!=0)
		{
			return FALSE;
		}

		sprintf(temp,"%s.ipAddress",hostIndex);
		status=ReadTextValue(object,temp,ipAddress);
		if(status!=0)
		{
			return FALSE;
		}

		ret=AddOrUpdateHost(&configuration,hardwareAddress,ipAddress);
		if(ret!=TRUE)
		{
			return FALSE;
		}

		i++;
	}

	free(object);

	SaveDhcpConfiguration(&configuration);

	return TRUE;
}

int DoAddSubnet(const char * json)
{
	struct DhcpConfiguration configuration;
	ReadDhcpConfiguration(&configuration);

	char subnetAddress[50];
	char netmask[50];
	char routers[50];
	char subnetMask[50];
	char domainNameServers[50];
	char rangeStart[50];
	char rangeEnd[50];
	char defaultLeaseTime[50];
	char maxLeaseTime[50];

	struct json_token * object;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return FALSE;
	}

	int status;

	status=ReadTextValue(object,"subnet",subnetAddress);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"netmask",netmask);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"router",routers);
	if(status!=0)
	{
		return FALSE;
	}

	strcpy(subnetMask,netmask);

	status=ReadTextValue(object,"dns",domainNameServers);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"rangeStart",rangeStart);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"rangeEnd",rangeEnd);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"defaultLease",defaultLeaseTime);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"maxLease",maxLeaseTime);
	if(status!=0)
	{
		return 1;
	}

	free(object);

	int ret=AddOrUpdateSubnet(&configuration,subnetAddress,netmask,routers,subnetMask,domainNameServers
		,rangeStart,rangeEnd,defaultLeaseTime,maxLeaseTime);

	if(ret==FALSE)
	{
		return FALSE;
	}
	else
	{
		SaveDhcpConfiguration(&configuration);
	}

	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	int ret=FALSE;

	if(request->QueryString!=NULL && strcmp(request->QueryString,"host")==0)
	{
		ret=DoAddHosts(request->Content);
	}
	else if(request->QueryString!=NULL && strcmp(request->QueryString,"assign")==0)
	{
		const char * hardwareAddress=request->GetHeader(request,"x-bws-hardware-address");
		const char * subnetAddress=request->GetHeader(request,"x-bws-subnet-address");
		if(hardwareAddress==NULL || subnetAddress==NULL)
		{
			response->StatusCode=400;
			response->SetContent(response,"");
			return TRUE;
		}
		struct DhcpConfiguration configuration;
		ReadDhcpConfiguration(&configuration);
		char assignedIPAddress[100];
		ret=AssignIPAddressForHost(&configuration,hardwareAddress,subnetAddress,assignedIPAddress);
		if(ret==TRUE)
		{
			response->SetHeader(response,"x-bws-assigned-ip-address",assignedIPAddress);
		}
		SaveDhcpConfiguration(&configuration);
	}
	else
	{
		ret=DoAddSubnet(request->Content);
	}

	if(ret!=TRUE)
	{
		response->StatusCode=400;
		response->SetContent(response,"");
		return TRUE;
	}

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	struct DhcpConfiguration configuration;
	InitializeDhcpConfiguration(&configuration);
	SaveDhcpConfiguration(&configuration);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int DoRemoveHosts(const char * json)
{
	struct DhcpConfiguration configuration;
	ReadDhcpConfiguration(&configuration);

	struct json_token * object;
	const struct json_token * token;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return FALSE;
	}

	int status=0;
	int i=0;
	int ret=FALSE;

	while(1)
	{
		char hostIndex[100];
		char temp[100];
		char hardwareAddress[100];

		sprintf(hostIndex,"hosts[%d]",i);
		token=find_json_token(object,hostIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.hardwareAddress",hostIndex);
		status=ReadTextValue(object,temp,hardwareAddress);
		if(status!=0)
		{
			return FALSE;
		}

		ret=RemoveHost(&configuration,hardwareAddress);
		if(ret!=TRUE)
		{
			return FALSE;
		}

		i++;
	}

	free(object);

	SaveDhcpConfiguration(&configuration);

	return TRUE;
}

int DoRemoveSubnet(const char * json)
{
	struct DhcpConfiguration configuration;
	ReadDhcpConfiguration(&configuration);

	char subnetAddress[50];
	char netmask[50];

	struct json_token * object;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return FALSE;
	}

	int status;

	status=ReadTextValue(object,"subnet",subnetAddress);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"netmask",netmask);
	if(status!=0)
	{
		return FALSE;
	}

	free(object);

	int ret=RemoveSubnet(&configuration,subnetAddress,netmask);

	if(ret==FALSE)
	{
		return FALSE;
	}
	else
	{
		SaveDhcpConfiguration(&configuration);
	}

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	int ret=FALSE;

	if(request->QueryString!=NULL && strcmp(request->QueryString,"host")==0)
	{
		ret=DoRemoveHosts(request->Content);
	}
	else
	{
		ret=DoRemoveSubnet(request->Content);
	}

	if(ret!=TRUE)
	{
		response->StatusCode=400;
		response->SetContent(response,"");
		return TRUE;
	}

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}
