#ifndef _HTTP_RESPONSE_H_
#define _HTTP_RESPONSE_H_

#include "HttpHeader.h"

struct HttpResponse
{
	int StatusCode;
	char * Content;

	struct HttpHeader Headers[30];
	int HeaderCount;

	void (* SetHeader) (struct HttpResponse * response, const char * name, const char * value);
	void (* SetContent) (struct HttpResponse * response, const char * content);
};

#endif
