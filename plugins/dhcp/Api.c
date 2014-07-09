#include <stdio.h>

#include "PluginInterface.h"

const char * PluginName="DHCP";
const char * PluginVersion="1.0.0.0";

int Initialize()
{
	printf("Initialize dhcp plugin.\n");
	return 0;
}

int Destroy()
{
	printf("Destroy dhcp plugin.\n");
	return 0;
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("GET DHCP\n");
	printf("URI: %s\n",request->Uri);
	printf("Method: %s\n",request->Method);
	printf("Query String: %s\n",request->QueryString);
	printf("Request Content: %s\n",request->Content);
	int i=0;
	for(i=0;i<request->HeaderCount;i++)
	{
		printf("Header [%s]=[%s]\n",request->Headers[i].Name,request->Headers[i].Value);
	}

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");
	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("HEAD DHCP\n");
	printf("URI: %s\n",request->Uri);
	printf("Method: %s\n",request->Method);
	printf("Query String: %s\n",request->QueryString);
	printf("Request Content: %s\n",request->Content);
	int i=0;
	for(i=0;i<request->HeaderCount;i++)
	{
		printf("Header [%s]=[%s]\n",request->Headers[i].Name,request->Headers[i].Value);
	}

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");
	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("POST DHCP\n");
	printf("URI: %s\n",request->Uri);
	printf("Method: %s\n",request->Method);
	printf("Query String: %s\n",request->QueryString);
	printf("Request Content: %s\n",request->Content);
	int i=0;
	for(i=0;i<request->HeaderCount;i++)
	{
		printf("Header [%s]=[%s]\n",request->Headers[i].Name,request->Headers[i].Value);
	}

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");
	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("PUT DHCP\n");
	printf("URI: %s\n",request->Uri);
	printf("Method: %s\n",request->Method);
	printf("Query String: %s\n",request->QueryString);
	printf("Request Content: %s\n",request->Content);
	int i=0;
	for(i=0;i<request->HeaderCount;i++)
	{
		printf("Header [%s]=[%s]\n",request->Headers[i].Name,request->Headers[i].Value);
	}

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");
	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("DELETE DHCP\n");
	printf("URI: %s\n",request->Uri);
	printf("Method: %s\n",request->Method);
	printf("Query String: %s\n",request->QueryString);
	printf("Request Content: %s\n",request->Content);
	int i=0;
	for(i=0;i<request->HeaderCount;i++)
	{
		printf("Header [%s]=[%s]\n",request->Headers[i].Name,request->Headers[i].Value);
	}

	response->StatusCode=200;
	response->SetHeader(response,"Content-Length","0");
	response->SetContent(response,"");
	return TRUE;
}
