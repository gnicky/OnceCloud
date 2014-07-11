#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"

const char * PluginName="LoadBalancer";
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
	response->StatusCode=405;
	response->SetContent(response,"");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	// TODO
	
	response->StatusCode=200;
	response->SetContent(response,"");
	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"");

	return TRUE;
}
