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
	if(request->QueryString!=NULL)
	{
		if(strcmp(request->QueryString,"port")==0)
		{
			const char * protocol=request->GetHeader(request,"x-bws-protocol");
			const char * internalAddress=request->GetHeader(request,"x-bws-internal-ip-address");
			const char * internalPort=request->GetHeader(request,"x-bws-internal-port");
			const char * externalAddress=request->GetHeader(request,"x-bws-external-ip-address");
			const char * externalPort=request->GetHeader(request,"x-bws-external-port");

			if(protocol==NULL || internalAddress==NULL || internalPort==NULL
				|| externalAddress==NULL || externalPort==NULL)
			{
				response->StatusCode=400;
				response->SetContent(response,"");

				return TRUE;
			}

			AddPortForwarding(protocol,internalAddress,internalPort,externalAddress,externalPort);

			response->StatusCode=200;
			response->SetContent(response,"");

			return TRUE;
		}
	}

	const char * internalIPAddress=request->GetHeader(request,"x-bws-internal-ip-address");
	const char * externalIPAddress=request->GetHeader(request,"x-bws-external-ip-address");
	const char * externalInterface=request->GetHeader(request,"x-bws-external-interface");

	if(internalIPAddress==NULL || externalIPAddress==NULL || externalInterface==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Internal IP Address, External IP Address and External Interface.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddNat(internalIPAddress,externalIPAddress,externalInterface);

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
	if(request->QueryString!=NULL)
	{
		if(strcmp(request->QueryString,"port")==0)
		{
			const char * protocol=request->GetHeader(request,"x-bws-protocol");
			const char * internalAddress=request->GetHeader(request,"x-bws-internal-ip-address");
			const char * internalPort=request->GetHeader(request,"x-bws-internal-port");
			const char * externalAddress=request->GetHeader(request,"x-bws-external-ip-address");
			const char * externalPort=request->GetHeader(request,"x-bws-external-port");

			if(protocol==NULL || internalAddress==NULL || internalPort==NULL
				|| externalAddress==NULL || externalPort==NULL)
			{
				response->StatusCode=400;
				response->SetContent(response,"");

				return TRUE;
			}

			RemovePortForwarding(protocol,internalAddress,internalPort,externalAddress,externalPort);

			response->StatusCode=200;
			response->SetContent(response,"");

			return TRUE;
		}
	}

	const char * internalIPAddress=request->GetHeader(request,"x-bws-internal-ip-address");
	const char * externalIPAddress=request->GetHeader(request,"x-bws-external-ip-address");
	const char * externalInterface=request->GetHeader(request,"x-bws-external-interface");

	if(internalIPAddress==NULL || externalIPAddress==NULL || externalInterface==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Internal IP Address, External IP Address and External Interface.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	RemoveNat(internalIPAddress,externalIPAddress,externalInterface);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}
