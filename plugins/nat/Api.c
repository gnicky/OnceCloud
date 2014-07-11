#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Type.h"
#include "Core.h"
#include "NatEntry.h"

const char * PluginName="NAT";
const char * PluginVersion="1.0.0.0";

void GenerateNatEntryList(char * buffer, struct NatEntry * natEntry, int count);

int Initialize()
{
	return 0;
}

int Destroy()
{
	return 0;
}

void GenerateNatEntryList(char * buffer, struct NatEntry * natEntry, int count)
{
	int i=0;
	buffer[0]='\0';
	strcat(buffer,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	strcat(buffer,"<ListBindingsResult>\n");
	strcat(buffer,"\t<Bindings>\n");
	for(i=0;i<count;i++)
	{
		strcat(buffer,"\t\t<Binding>\n");
		strcat(buffer,"\t\t\t<InternalIPAddress>");
		strcat(buffer,natEntry[i].InternalIPAddress);
		strcat(buffer,"</InternalIPAddress>\n");
		strcat(buffer,"\t\t\t<ExternalIPAddress>");
		strcat(buffer,natEntry[i].ExternalIPAddress);
		strcat(buffer,"</ExternalIPAddress>\n");
		strcat(buffer,"\t\t</Binding>\n");
	}
	strcat(buffer,"\t</Bindings>\n");
	strcat(buffer,"</ListBindingsResult>\n");
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	int entryCount=0;
	struct NatEntry natEntry[300];

	ListNatEntry(natEntry,&entryCount);

	char * buffer=malloc(65536);
	GenerateNatEntryList(buffer,natEntry,entryCount);

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
	const char * internalIPAddress=request->GetHeader(request,"x-bws-internal-ip-address");
	const char * externalIPAddress=request->GetHeader(request,"x-bws-external-ip-address");

	if(internalIPAddress==NULL || externalIPAddress==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Internal IP Address and External IP Address.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddNat(internalIPAddress,externalIPAddress);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	InitializeNat();

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * internalIPAddress=request->GetHeader(request,"x-bws-internal-ip-address");
	const char * externalIPAddress=request->GetHeader(request,"x-bws-external-ip-address");

	if(internalIPAddress==NULL || externalIPAddress==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Internal IP Address and External IP Address.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	RemoveNat(internalIPAddress,externalIPAddress);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}
