#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"

const char * PluginName="PPTP";
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
	response->SetContent(response,"GET /PPTP");

	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"HEAD /PPTP");

	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"POST /PPTP");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"PUT /PPTP");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"DELETE /PPTP");

	return TRUE;
}

