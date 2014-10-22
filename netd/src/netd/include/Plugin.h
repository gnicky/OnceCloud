#ifndef _PLUGIN_H_
#define _PLUGIN_H

#include "HttpRequest.h"
#include "HttpResponse.h"

struct Plugin
{
	void * Handle;
	char Path[256];

	char Name[100];
	char Version[100];

	int (* Initialize) ();
	int (* Destroy) ();

	int (* HandleGetRequest) (struct HttpRequest * request, struct HttpResponse * response);
	int (* HandleHeadRequest) (struct HttpRequest * request, struct HttpResponse * response);
	int (* HandlePostRequest) (struct HttpRequest * request, struct HttpResponse * response);
	int (* HandlePutRequest) (struct HttpRequest * request, struct HttpResponse * response);
	int (* HandleDeleteRequest) (struct HttpRequest * request, struct HttpResponse * response);
};

#endif
