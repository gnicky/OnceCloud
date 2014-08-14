#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Configuration.h"
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
	struct Configuration configuration;
	strcpy(configuration.NetworkAddress,"172.16.3.0");
	configuration.MaxConnections=123;
	configuration.UserCount=2;
	strcpy(configuration.Users[0].UserName,"test1");
	strcpy(configuration.Users[0].Password,"test1");
	strcpy(configuration.Users[0].Server,"pptpd");
	strcpy(configuration.Users[0].IPAddress,"*");
	strcpy(configuration.Users[1].UserName,"test2");
	strcpy(configuration.Users[1].Password,"test2");
	strcpy(configuration.Users[1].Server,"pptpd");
	strcpy(configuration.Users[1].IPAddress,"*");
	Configure(&configuration);

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

