#ifndef _HTTP_REQUEST_H_
#define _HTTP_REQUEST_H_

#include "HttpHeader.h"

struct HttpRequest
{
	const char * Method;
	const char * Uri;
	const char * QueryString;

	struct HttpHeader Headers[30];
	int HeaderCount;

	const char * Content;
};

#endif
