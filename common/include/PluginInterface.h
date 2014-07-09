#ifndef _PLUGIN_INTERFACE_H_
#define _PLUGIN_INTERFACE_H_

#include "Type.h"

#include "HttpRequest.h"
#include "HttpResponse.h"

extern const char * PluginName;
extern const char * PluginVersion;

int Initialize();
int Destroy();

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response);
int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response);
int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response);
int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response);
int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response);

#endif
