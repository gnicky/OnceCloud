#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"

const char * PluginName="DHCP";
const char * PluginVersion="1.0.0.0";

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
	strcat(buffer,"<ListAllBindingsResult>\n");
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
	strcat(buffer,"</ListAllBindingsResult>\n");
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

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * ipAddress=request->GetHeader(request,"x-bws-ip-address");
	const char * hardwareAddress=request->GetHeader(request,"x-bws-hardware-address");

	if(ipAddress==NULL || hardwareAddress==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify IP Address and Hardware Address.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	Bind(ipAddress,hardwareAddress);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * subnet=request->GetHeader(request,"x-bws-subnet");
	const char * netmask=request->GetHeader(request,"x-bws-netmask");
	const char * router=request->GetHeader(request,"x-bws-router");
	const char * dns=request->GetHeader(request,"x-bws-dns");
	const char * rangeStart=request->GetHeader(request,"x-bws-range-start");
	const char * rangeEnd=request->GetHeader(request,"x-bws-range-end");
	const char * defaultLease=request->GetHeader(request,"x-bws-default-lease");
	const char * maxLease=request->GetHeader(request,"x-bws-max-lease");

	if(subnet==NULL || netmask==NULL || router==NULL || dns==NULL || rangeStart==NULL || rangeEnd==NULL || defaultLease==NULL || maxLease==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Sub-network Address, Sub-network Mask, Router, DNS, Range and Lease Time.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	InitializeConfiguration(subnet,netmask,router,dns,rangeStart,rangeEnd,defaultLease,maxLease);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");
	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * ipAddress=request->GetHeader(request,"x-bws-ip-address");
	const char * hardwareAddress=request->GetHeader(request,"x-bws-hardware-address");

	if(ipAddress==NULL || hardwareAddress==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify IP Address and Hardware Address.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	Unbind(ipAddress,hardwareAddress);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}
