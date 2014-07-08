#ifndef _PLUGIN_H_
#define _PLUGIN_H

#include "Mongoose.h"

struct Plugin
{
	void * Handle;
	char Path[256];

	char Name[100];
	char Version[100];

	int (* HandleGetRequest) (struct mg_connection * connection, enum mg_event event);
	int (* HandleHeadRequest) (struct mg_connection * connection, enum mg_event event);
	int (* HandlePostRequest) (struct mg_connection * connection, enum mg_event event);
	int (* HandlePutRequest) (struct mg_connection * connection, enum mg_event event);
	int (* HandleDeleteRequest) (struct mg_connection * connection, enum mg_event event);
};

#endif
