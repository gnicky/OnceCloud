#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "File.h"

const char * PluginName="OpenVPN";
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
	if(request->QueryString!=NULL && strcmp(request->QueryString,"takey")==0)
	{
		char * buffer=malloc(1048576);
		memset(buffer,0,1048576);
		GenerateTLSAuthKey(buffer);
		response->SetHeader(response,"Content-Type","text/plain");
		response->StatusCode=200;
		response->SetContent(response,buffer);
		free(buffer);
		return TRUE;
	}
	else
	{
		response->StatusCode=400;
		response->SetContent(response,"");
		return TRUE;
	}
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"HEAD /OpenVPN");

	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"POST /OpenVPN");

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	struct Configuration configuration;
	strcpy(configuration.Protocol,"tcp");
	configuration.Port=10086;
	ReadFile("/root/keys/ca.crt",configuration.CACertificate);
	ReadFile("/root/keys/server.crt",configuration.ServerCertificate);
	ReadFile("/root/keys/server.key",configuration.ServerPrivateKey);
	ReadFile("/root/keys/dh2048.pem",configuration.DiffieHellmanParameter);
	ReadFile("/root/keys/ta.key",configuration.TLSAuthKey);
	strcpy(configuration.NetworkAddress,"172.16.5.0");
	strcpy(configuration.Netmask,"255.255.255.0");

	Configure(&configuration);

	response->StatusCode=200;
	response->SetContent(response,"PUT /OpenVPN");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"DELETE /OpenVPN");

	return TRUE;
}

