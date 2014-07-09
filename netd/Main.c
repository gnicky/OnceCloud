#include <memory.h>

#include "Mongoose.h"
#include "Plugin.h"
#include "PluginManager.h"

static int EventHandler(struct mg_connection * connection, enum mg_event event)
{
	int result=MG_FALSE;

	if(event==MG_REQUEST)
	{
		char pluginName[100];
		memset(pluginName,0,sizeof(pluginName));
		const char * position=strstr(connection->uri+strlen("/"),"/");
		if(position==NULL)
		{
			strcpy(pluginName,connection->uri+strlen("/"));
		}
		else
		{
			memcpy(pluginName,connection->uri+strlen("/"),position-connection->uri-strlen("/"));
		}

		struct Plugin * plugin=FindPlugin(pluginName);
		if(plugin!=NULL)
		{
			struct HttpRequest * request=NULL;
			struct HttpResponse * response=NULL;

			if(strcmp(connection->request_method,"GET")==0)
			{
				plugin->HandleGetRequest(request,response);
			}
			if(strcmp(connection->request_method,"HEAD")==0)
			{
				plugin->HandleHeadRequest(request,response);
			}
			if(strcmp(connection->request_method,"POST")==0)
			{
				plugin->HandlePostRequest(request,response);
			}
			if(strcmp(connection->request_method,"PUT")==0)
			{
				plugin->HandlePutRequest(request,response);
			}
			if(strcmp(connection->request_method,"DELETE")==0)
			{
				plugin->HandleDeleteRequest(request,response);
			}

			// 405 Method not allowed
			mg_send_status(connection,405);
			mg_send_header(connection,"Content-Length","0");
			mg_send_data(connection,"",0);
		}
		else
		{
			// 404 Not Found
			mg_send_status(connection,404);
			mg_send_header(connection,"Content-Length","0");
			mg_send_data(connection,"",0);			
		}
		result=MG_TRUE;
	}
	else if(event==MG_AUTH)
	{
		result=MG_TRUE;
	}

	return result;
}


int main(int argc, char * argv [])
{
	LoadPlugins();

	struct mg_server * server;

	server=mg_create_server(NULL,EventHandler);
	mg_set_option(server,"listening_port","8080");

	printf("Starting on port %s\n",mg_get_option(server, "listening_port"));
	while(1)
	{
		mg_poll_server(server,1000);
	}

	mg_destroy_server(&server);

	UnloadPlugins();
	return 0;
}
