#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Configuration.h"
#include "Core.h"
#include "Frozen.h"

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
	char networkAddress[50];
	char maxConnections[50];

	struct json_token * object;
	const struct json_token * token;

	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return FALSE;
	}

	int status;

	status=ReadTextValue(object,"networkAddress",networkAddress);
	if(status!=0)
	{
		return FALSE;
	}
	strcpy(configuration->NetworkAddress,networkAddress);

	status=ReadTextValue(object,"maxConnections",maxConnections);
	if(status!=0)
	{
		return FALSE;
	}
	sscanf(maxConnections,"%d",&configuration->MaxConnections);

	int i=0;
	while(1)
	{
		char temp[100];
		char userIndex[100];
		char userName[100];
		char password[100];

		sprintf(userIndex,"users[%d]",i);
		token=find_json_token(object,userIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.userName",userIndex);
		status=ReadTextValue(object,temp,userName);
		if(status!=0)
		{
			return FALSE;
		}

		sprintf(temp,"%s.password",userIndex);
		status=ReadTextValue(object,temp,password);
		if(status!=0)
		{
			return FALSE;
		}

		strcpy(configuration->Users[i].UserName,userName);
		strcpy(configuration->Users[i].Password,password);
		strcpy(configuration->Users[i].Server,"pptpd");
		strcpy(configuration->Users[i].IPAddress,"*");

		i++;
	}
	configuration->UserCount=i;
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
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	response->StatusCode=405;
	response->SetContent(response,"DELETE /PPTP");

	return TRUE;
}

