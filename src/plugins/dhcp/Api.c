#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "Logger.h"

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
	GetDhcpConfiguration(&configuration);
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

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"");

	return TRUE;
}
