#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"
#include "Frozen.h"

const char * PluginName="LoadBalancer";
const char * PluginVersion="1.0.0.0";

int ParseRequest(const char * json, struct Configuration * configuration)
{
	struct json_token * object;
	const struct json_token * token;

 	object=parse_json2(json,strlen(json));
	if(object==NULL)
	{
		return 1;
	}

 	token=find_json_token(object,"workerProcesses");
	if(token==NULL)
	{
		return 1;
	}
	memcpy(configuration->WorkerProcesses,token->ptr,token->len);
	configuration->WorkerProcesses[token->len]='\0';

	token=find_json_token(object,"workerConnections");
	if(token==NULL)
	{
		return 1;
	}
	memcpy(configuration->WorkerConnections,token->ptr,token->len);
	configuration->WorkerConnections[token->len]='\0';

	int i=0;
	int j=0;
	while(1)
	{
		j=0;

		char listenerIndex[100];
		char temp[100];

		sprintf(listenerIndex,"listeners[%d]",i);		
		token=find_json_token(object,listenerIndex);
		if(token==NULL)
		{
			break;
		}

		sprintf(temp,"%s.port",listenerIndex);
		token=find_json_token(object,temp);
		if(token==NULL)
		{
			return 1;
		}
		memcpy(configuration->Listeners[i].Port,token->ptr,token->len);
		configuration->Listeners[i].Port[token->len]='\0';

		sprintf(temp,"%s.protocol",listenerIndex);
		token=find_json_token(object,temp);
		if(token==NULL)
		{
			return 1;
		}
		memcpy(configuration->Listeners[i].Protocol,token->ptr,token->len);
		configuration->Listeners[i].Protocol[token->len]='\0';

		sprintf(temp,"%s.policy",listenerIndex);
		token=find_json_token(object,temp);
		if(token==NULL)
		{
			return 1;
		}
		memcpy(configuration->Listeners[i].Policy,token->ptr,token->len);
		configuration->Listeners[i].Policy[token->len]='\0';

		while(1)
		{
			char ruleIndex[100];

			sprintf(ruleIndex,"%s.rules[%d]",listenerIndex,j);
			token=find_json_token(object,ruleIndex);
			if(token==NULL)
			{
				break;
			}

			sprintf(temp,"%s.port",ruleIndex);
			token=find_json_token(object,temp);
			if(token==NULL)
			{
				return 1;
			}	
			memcpy(configuration->Listeners[i].Rules[j].Port,token->ptr,token->len);
			configuration->Listeners[i].Rules[j].Port[token->len]='\0';

			sprintf(temp,"%s.ip",ruleIndex);
			token=find_json_token(object,temp);
			if(token==NULL)
			{
				return 1;
			}	
			memcpy(configuration->Listeners[i].Rules[j].IPAddress,token->ptr,token->len);
			configuration->Listeners[i].Rules[j].IPAddress[token->len]='\0';

			j++;
		}

		configuration->Listeners[i].RuleCount=j;
		i++;
	}

	configuration->ListenerCount=i;

 	free(object);

	return 0;
}

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
/*
// Sample JSON 	
	char * json=	
		"{\n"
		"\t\"workerProcesses\":5,\n"
		"\t\"workerConnections\":1000,\n"
		"\t\"listeners\":\n"
		"\t[\n"
		"\t\t{\n"
		"\t\t\t\"port\":80,\n"
		"\t\t\t\"protocol\":\"http\",\n"
		"\t\t\t\"policy\":0,\n"
		"\t\t\t\"rules\":\n"
		"\t\t\t[\n"
		"\t\t\t\t{\n"
		"\t\t\t\t\t\"port\":8080,\n"
		"\t\t\t\t\t\"ip\":\"10.0.0.1\"\n"
		"\t\t\t\t},\n"
		"\t\t\t\t{\n"
		"\t\t\t\t\t\"port\":8090,\n"
		"\t\t\t\t\t\"ip\":\"10.0.0.2\"\n"
		"\t\t\t\t}\n"
		"\t\t\t]\n"
		"\t\t},\n"
		"\t\t{\n"
		"\t\t\t\"port\":443,\n"
		"\t\t\t\"protocol\":\"tcp\",\n"
		"\t\t\t\"policy\":1,\n"
		"\t\t\t\"rules\":\n"
		"\t\t\t[\n"
		"\t\t\t\t{\n"
		"\t\t\t\t\t\"port\":8080,\n"
		"\t\t\t\t\t\"ip\":\"10.0.0.3\"\n"
		"\t\t\t\t},\n"
		"\t\t\t\t{\n"
		"\t\t\t\t\t\"port\":8090,\n"
		"\t\t\t\t\t\"ip\":\"10.0.0.4\"\n"
		"\t\t\t\t}\n"
		"\t\t\t]\n"
		"\t\t}\n"
		"\t]\n"
		"}\n";
*/

	if(request->Content==NULL)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	struct Configuration configuration;
	int ret=ParseRequest(request->Content,&configuration);
	if(ret!=0)
	{
		response->StatusCode=400;
		response->SetContent(response,"");

		return TRUE;
	}

	SaveConfiguration(&configuration);
	RestartService();

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
