#ifndef _HTTP_REQUEST_H_
#define _HTTP_REQUEST_H_

#include "HttpHeader.h"

struct HttpRequest
{
	char * Method;
	char * Uri;
	char * QueryString;

	struct HttpHeader Headers[30];
	int HeaderCount;

	char * Content;

	const char * (* GetHeader) (struct HttpRequest * request, const char * name);
};

#endif
