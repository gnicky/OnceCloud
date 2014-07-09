#include <stdlib.h>
#include <memory.h>

#include "HttpHelper.h"
#include "HttpRequest.h"
#include "HttpResponse.h"

void FreeHttpHeaders(struct HttpHeader headers[], int headerCount);

const char * GetRequestHeader(struct HttpRequest * request, const char * name);

void SetResponseHeader(struct HttpResponse * response, const char * name, const char * value);
void SetResponseContent(struct HttpResponse * response, const char * content);

const char * GetRequestHeader(struct HttpRequest * request, const char * name)
{
	int i=0;
	for(i=0;i<request->HeaderCount;i++)
	{
		if(strcmp(request->Headers[i].Name,name)==0)
		{
			return request->Headers[i].Value;
		}
	}
	return NULL;
}

void SetResponseHeader(struct HttpResponse * response, const char * name, const char * value)
{
	int i=0;
	int exist=0;
	for(i=0;i<response->HeaderCount;i++)
	{
		if(strcmp(response->Headers[i].Name,name)==0)
		{
			free(response->Headers[i].Value);
			response->Headers[i].Value=malloc(strlen(value)+1);
			strcpy(response->Headers[i].Value,value);
			exist=1;
			break;
		}
	}
	if(!exist)
	{
		response->Headers[response->HeaderCount].Name=malloc(strlen(name)+1);
		strcpy(response->Headers[response->HeaderCount].Name,name);
		response->Headers[response->HeaderCount].Value=malloc(strlen(value)+1);
		strcpy(response->Headers[response->HeaderCount].Value,value);
		response->HeaderCount++;
	}
}

void SetResponseContent(struct HttpResponse * response, const char * content)
{
	if(response->Content!=NULL)
	{
		free(response->Content);
	}
	response->Content=malloc(strlen(content)+1);
	strcpy(response->Content,content);

	int length=strlen(content);
	char textLength[50];
	sprintf(textLength,"%d",length);
	response->SetHeader(response,"Content-Length",textLength);
}

void FreeHttpHeaders(struct HttpHeader headers[], int headerCount)
{
	int i=0;
	for(i=0;i<headerCount;i++)
	{
		free(headers[i].Name);
		free(headers[i].Value);
	}
}

struct HttpRequest * NewHttpRequest()
{
	struct HttpRequest * request=malloc(sizeof(struct HttpRequest));
	memset(request,0,sizeof(struct HttpRequest));

	*(&(request->GetHeader))=GetRequestHeader;
	return request;
}

void DeleteHttpRequest(struct HttpRequest * request)
{
	free(request->Method);
	free(request->Uri);
	free(request->QueryString);
	free(request->Content);

	FreeHttpHeaders(request->Headers,request->HeaderCount);

	free(request);
}

void FillHttpRequest(struct mg_connection * connection, struct HttpRequest * request)
{
	if(connection->request_method!=NULL)
	{
		request->Method=malloc(strlen(connection->request_method)+1);
		strcpy(request->Method,connection->request_method);
	}
	if(connection->uri!=NULL)
	{
		request->Uri=malloc(strlen(connection->uri)+1);
		strcpy(request->Uri,connection->uri);
	}
	if(connection->query_string!=NULL)
	{
		request->QueryString=malloc(strlen(connection->query_string)+1);
		strcpy(request->QueryString,connection->query_string);
	}
	if(connection->content!=NULL)
	{
		request->Content=malloc(connection->content_len+1);
		memcpy(request->Content,connection->content,connection->content_len);
		request->Content[connection->content_len]='\0';
	}

	int i=0;
	for(i=0;i<connection->num_headers;i++)
	{
		request->Headers[i].Name=malloc(strlen(connection->http_headers[i].name)+1);
		strcpy(request->Headers[i].Name,connection->http_headers[i].name);
		request->Headers[i].Value=malloc(strlen(connection->http_headers[i].value)+1);
		strcpy(request->Headers[i].Value,connection->http_headers[i].value);
	}
	request->HeaderCount=connection->num_headers;
}

struct HttpResponse * NewHttpResponse()
{
	struct HttpResponse * response=malloc(sizeof(struct HttpResponse));
	memset(response,0,sizeof(struct HttpResponse));
	*(&(response->SetHeader))=SetResponseHeader;
	*(&(response->SetContent))=SetResponseContent;
	return response;
}

void DeleteHttpResponse(struct HttpResponse * response)
{
	free(response->Content);

	FreeHttpHeaders(response->Headers, response->HeaderCount);

	free(response);
}
