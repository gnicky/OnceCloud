#ifndef _PLUGIN_INTERFACE_H_
#define _PLUGIN_INTERFACE_H_

#include "PluginMetadata.h"
#include "Type.h"

#include "Mongoose.h"

int InitPlugin();
struct PluginMetadata GetMetadata();

int HandleGetRequest(struct mg_connection * connection, enum mg_event event);
int HandleHeadRequest(struct mg_connection * connection, enum mg_event event);
int HandlePostRequest(struct mg_connection * connection, enum mg_event event);
int HandlePutRequest(struct mg_connection * connection, enum mg_event event);
int HandleDeleteRequest(struct mg_connection * connection, enum mg_event event);

#endif
