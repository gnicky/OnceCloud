#include <stdio.h>

#include "Core.h"
#include "PluginInterface.h"

const char * PluginName="Route";
const char * PluginVersion="1.0.0.0";

int Initialize()
{
	return 0;
}

int Destroy()
{
	return 0;
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"");

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
	const char * mac=request->GetHeader(request,"x-bws-mac");
	const char * address=request->GetHeader(request,"x-bws-ip-address");
	const char * netmask=request->GetHeader(request,"x-bws-netmask");

	if(mac==NULL || address==NULL || netmask==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify MAC, IP Address, and Netmask.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddRoute(mac,address,netmask);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * mac=request->GetHeader(request,"x-bws-mac");
	const char * address=request->GetHeader(request,"x-bws-ip-address");
	const char * netmask=request->GetHeader(request,"x-bws-netmask");

	if(mac==NULL || address==NULL || netmask==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify MAC, IP Address, and Netmask.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddRoute(mac,address,netmask);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * mac=request->GetHeader(request,"x-bws-mac");

	if(mac==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify MAC.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	RemoveRoute(mac);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

