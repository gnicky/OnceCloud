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
	const char * interface=request->GetHeader(request,"x-bws-interface");
	const char * address=request->GetHeader(request,"x-bws-ip-address");
	const char * netmask=request->GetHeader(request,"x-bws-netmask");

	if(interface==NULL || address==NULL || netmask==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Interface, IP Address, and Netmask.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddRoute(interface,address,netmask);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * interface=request->GetHeader(request,"x-bws-interface");
	const char * address=request->GetHeader(request,"x-bws-ip-address");
	const char * netmask=request->GetHeader(request,"x-bws-netmask");

	if(interface==NULL || address==NULL || netmask==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Interface, IP Address, and Netmask.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	AddRoute(interface,address,netmask);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * interface=request->GetHeader(request,"x-bws-interface");

	if(interface==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify Interface.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;
	}

	RemoveRoute(interface);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}
