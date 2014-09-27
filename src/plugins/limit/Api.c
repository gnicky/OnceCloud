#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"

const char * PluginName="Limit";
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
	response->StatusCode=200;
	response->SetContent(response,"");

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
	const char * ip=request->GetHeader(request,"x-bws-ip-address");
	const char * speed=request->GetHeader(request,"x-bws-speed");

	if(ip!=NULL && speed!=NULL)
	{
		SetLimit(ip,speed);

		response->StatusCode=200;
		response->SetContent(response,"");
		return TRUE;
	}
	
	char ErrorMessage[]=
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		"<Error>\n\tInvalid parameters.\n</Error>\n";

	response->StatusCode=400;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,ErrorMessage);

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * ip=request->GetHeader(request,"x-bws-ip-address");
	const char * speed=request->GetHeader(request,"x-bws-speed");

	if(ip!=NULL && speed!=NULL)
	{
		SetLimit(ip,speed);

		response->StatusCode=200;
		response->SetContent(response,"");
		return TRUE;
	}
	
	char ErrorMessage[]=
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		"<Error>\n\tInvalid parameters.\n</Error>\n";

	response->StatusCode=400;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,ErrorMessage);

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

