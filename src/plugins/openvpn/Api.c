#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "File.h"
#include "Frozen.h"

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

int ReadTextValue(struct json_token * object, const char * key, char * buffer)
{
	const struct json_token * token;
	token=find_json_token(object,key);
	if(token==NULL)
	{
		return 1;
	}
	memcpy(buffer,token->ptr,token->len);
	buffer[token->len]='\0';
	return 0;
}

int ParseConfiguration(const char * json, struct Configuration * configuration)
{
	struct json_token * object;
	
	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return FALSE;
	}

	int status=0;

	status=ReadTextValue(object,"protocol",configuration->Protocol);
	if(status!=0)
	{
		return FALSE;
	}

	char temp[100];
	status=ReadTextValue(object,"port",temp);
	if(status!=0)
	{
		return FALSE;
	}
	sscanf(temp,"%d",&configuration->Port);


	status=ReadTextValue(object,"caCertificate",configuration->CACertificate);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"serverCertificate",configuration->ServerCertificate);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"serverPrivateKey",configuration->ServerPrivateKey);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"diffieHellmanParameter",configuration->DiffieHellmanParameter);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"tlsAuthKey",configuration->TLSAuthKey);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"networkAddress",configuration->NetworkAddress);
	if(status!=0)
	{
		return FALSE;
	}

	status=ReadTextValue(object,"netmask",configuration->Netmask);
	if(status!=0)
	{
		return FALSE;
	}

	free(object);
	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	struct Configuration configuration;
	int ret=ParseConfiguration(request->Content,&configuration);
	if(ret!=TRUE)
	{
		response->StatusCode=400;
		response->SetContent(response,"");
		return TRUE;
	}

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

