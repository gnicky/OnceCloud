#include <memory.h>
#include <signal.h>
#include <stdlib.h>

#include "Mongoose.h"
#include "Plugin.h"
#include "PluginManager.h"
#include "HttpHelper.h"
#include "Logger.h"

int Exiting=0;

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
			int hit=0;

			struct HttpRequest * request=NewHttpRequest();
			struct HttpResponse * response=NewHttpResponse();

			FillHttpRequest(connection,request);

			if(strcmp(connection->request_method,"GET")==0)
			{
				plugin->HandleGetRequest(request,response);
				hit=1;
			}
			if(strcmp(connection->request_method,"HEAD")==0)
			{
				plugin->HandleHeadRequest(request,response);
				hit=1;
			}
			if(strcmp(connection->request_method,"POST")==0)
			{
				plugin->HandlePostRequest(request,response);
				hit=1;
			}
			if(strcmp(connection->request_method,"PUT")==0)
			{
				plugin->HandlePutRequest(request,response);
				hit=1;
			}
			if(strcmp(connection->request_method,"DELETE")==0)
			{
				plugin->HandleDeleteRequest(request,response);
				hit=1;
			}

			if(!hit)
			{
				// 405 Method not allowed
				mg_send_status(connection,405);
				mg_send_header(connection,"Content-Length","0");
				mg_send_data(connection,"",0);
			}
			else
			{
				mg_send_status(connection,response->StatusCode);
				int i=0;
				for(i=0;i<response->HeaderCount;i++)
				{
					mg_send_header(connection,response->Headers[i].Name,response->Headers[i].Value);
				}
				mg_send_data(connection,response->Content,strlen(response->Content));
			}

			DeleteHttpRequest(request);
			DeleteHttpResponse(response);
		}
		else
		{
			WriteLog(LOG_INFO,"Cannot find plugin %s, sending 404.",pluginName);
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

void Interrupt(int signal)
{
	WriteLog(LOG_INFO,"Asked to interrupt. Exiting.");
	Exiting=1;
}

void Terminate(int signal)
{
	WriteLog(LOG_INFO,"Asked to terminate. Exiting.");
	Exiting=1;
}

void OnSegmentationFault(int signal)
{
	WriteLog(LOG_ERR,"Segmentation fault detected. Aborting.");
	abort();
}


int main(int argc, char * argv [])
{
	signal(SIGINT,Interrupt);
	signal(SIGTERM,Terminate);
	signal(SIGSEGV,OnSegmentationFault);

	WriteLog(LOG_INFO,"Net Daemon started.");

	Exiting=0;

	WriteLog(LOG_INFO,"Loading plugins...");
	LoadPlugins();

	struct mg_server * server;

	server=mg_create_server(NULL,EventHandler);
	mg_set_option(server,"listening_port","9090");

	WriteLog(LOG_INFO,"Server started. Listening on port %s",mg_get_option(server, "listening_port"));
	while(!Exiting)
	{
		mg_poll_server(server,1000);
	}

	mg_destroy_server(&server);
	WriteLog(LOG_INFO,"Server stopped.");

	WriteLog(LOG_INFO,"Unloading plugins...");
	UnloadPlugins();

	WriteLog(LOG_INFO,"Net Daemon stopped.");

	return 0;
}
