#ifndef _HTTP_HELPER_H_
#define _HTTP_HELPER_H_

#include "HttpRequest.h"
#include "HttpResponse.h"
#include "Mongoose.h"

struct HttpRequest * NewHttpRequest();
void DeleteHttpRequest(struct HttpRequest * request);
void FillHttpRequest(struct mg_connection * connection, struct HttpRequest * request);

struct HttpResponse * NewHttpResponse();
void DeleteHttpResponse(struct HttpResponse * response);

#endif
