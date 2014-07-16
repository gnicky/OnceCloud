#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "DhcpConfiguration.h"
#include "Frozen.h"

const char * PluginName="DHCP";
const char * PluginVersion="1.0.0.0";

int ParseHostsList(const char * json, int * hostsCount, struct Host * hosts);

void GenerateDhcpEntryList(char * buffer, struct DhcpEntry * dhcpEntry, int count);

int Initialize()
{
	return 0;
}

int Destroy()
{
	return 0;
}

void GenerateDhcpEntryList(char * buffer, struct DhcpEntry * dhcpEntry, int count)
{
	int i=0;
	buffer[0]='\0';
	strcat(buffer,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	strcat(buffer,"<ListBindingsResult>\n");
	strcat(buffer,"\t<Bindings>\n");
	for(i=0;i<count;i++)
	{
		strcat(buffer,"\t\t<Binding>\n");
		strcat(buffer,"\t\t\t<IPAddress>");
		strcat(buffer,dhcpEntry[i].IPAddress);
		strcat(buffer,"</IPAddress>\n");
		strcat(buffer,"\t\t\t<HardwareAddress>");
		strcat(buffer,dhcpEntry[i].HardwareAddress);
		strcat(buffer,"</HardwareAddress>\n");
		strcat(buffer,"\t\t</Binding>\n");
	}
	strcat(buffer,"\t</Bindings>\n");
	strcat(buffer,"</ListBindingsResult>\n");
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	int entryCount=0;
	struct DhcpEntry dhcpEntry[300];

	ListDhcpEntry(dhcpEntry,&entryCount);

	char * buffer=malloc(65536);
	GenerateDhcpEntryList(buffer,dhcpEntry,entryCount);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,buffer);

	free(buffer);
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
	if(request->Content==NULL)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	int hostsCount;
	struct Host hosts[300];

	int ret=ParseHostsList(request->Content,&hostsCount,hosts);
	if(ret!=0)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	AddBindings(hostsCount,hosts);

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

int ParseHostsList(const char * json, int * hostsCount, struct Host * hosts)
{
	struct json_token * object;
	const struct json_token * token;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		printf("NULL\n");
		return 1;
	}

	int status;
	int i=0;

	while(1)
	{
		char hostIndex[100];
		char temp[100];

		sprintf(hostIndex,"hosts[%d]",i);
		token=find_json_token(object,hostIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.ipAddress",hostIndex);
		status=ReadTextValue(object,temp,hosts[i].IPAddress);
		if(status!=0)
		{
			printf("IPAddr\n");
			return 1;
		}
		
		sprintf(temp,"%s.hardwareAddress",hostIndex);
		status=ReadTextValue(object,temp,hosts[i].HardwareAddress);
		if(status!=0)
		{
			printf("HARD\n");
			return 1;
		}
		
		i++;
	}

	*hostsCount=i;
	free(object);

	return 0;
}

int ParseDhcpConfiguration(const char * json, struct DhcpConfiguration * configuration)
{
	struct json_token * object;
	const struct json_token * token;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return 1;
	}

	int status;

	status=ReadTextValue(object,"subnet",configuration->Subnet);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"netmask",configuration->Netmask);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"router",configuration->Router);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"dns",configuration->DNS);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"rangeStart",configuration->RangeStart);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"rangeEnd",configuration->RangeEnd);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"defaultLease",configuration->DefaultLease);
	if(status!=0)
	{
		return 1;
	}

	status=ReadTextValue(object,"maxLease",configuration->MaxLease);
	if(status!=0)
	{
		return 1;
	}

	int i=0;
	while(1)
	{
		char hostIndex[100];
		char temp[100];

		sprintf(hostIndex,"hosts[%d]",i);
		token=find_json_token(object,hostIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.ipAddress",hostIndex);
		status=ReadTextValue(object,temp,configuration->Hosts[i].IPAddress);
		if(status!=0)
		{
			return 1;
		}
		
		sprintf(temp,"%s.hardwareAddress",hostIndex);
		status=ReadTextValue(object,temp,configuration->Hosts[i].HardwareAddress);
		if(status!=0)
		{
			return 1;
		}

		i++;
	}

	configuration->HostCount=i;
	free(object);

	return 0;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->Content==NULL)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	struct DhcpConfiguration configuration;
	int ret=ParseDhcpConfiguration(request->Content,&configuration);
	if(ret!=0)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	GenerateConfiguration(&configuration);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->Content==NULL)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	int hostsCount;
	struct Host hosts[300];

	int ret=ParseHostsList(request->Content,&hostsCount,hosts);
	if(ret!=0)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	RemoveBindings(hostsCount,hosts);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}
