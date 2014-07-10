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
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,"");

	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,"");

	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("Query String: %s\n",request->QueryString);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("Query String: %s\n",request->QueryString);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");

	return TRUE;
}
