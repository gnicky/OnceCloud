#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dlfcn.h>

#include "Mongoose.h"


struct Plugin
{
	char Path[256];
	void * Handle;
	int (* Activate) (int count, char * values []);
};

struct Plugin DhcpPlugin;
struct Plugin NatPlugin;
struct Plugin FirewallPlugin;

void InitPlugin(struct Plugin * plugin, const char * pluginName)
{
	char * error=NULL;
	char pluginPath[256]="/usr/local/netsh/plugins/";
	strcat(pluginPath,pluginName);
	strcat(pluginPath,".so");	

	FILE * file=NULL;
	if((file=fopen(pluginPath,"rb"))==NULL)
	{
		printf("Error: Cannot find plugin %s\n",pluginName);
		exit(-1);
	}
	fclose(file);

	strcpy(plugin->Path,pluginPath);

	plugin->Handle=dlopen(pluginPath,RTLD_LAZY);
	error=dlerror();
	if(error!=NULL)
	{
		printf("Error: %s\n",error);
		exit(-1);
	}

	*(void **)(&plugin->Activate)=dlsym(plugin->Handle,"Activate");
	error=dlerror();
	if(error!=NULL)
	{
		printf("Error: %s\n",error);
		exit(-1);
	}
}

void InitPlugins()
{
	InitPlugin(&DhcpPlugin,"dhcp");
	InitPlugin(&NatPlugin,"nat");
	InitPlugin(&FirewallPlugin,"firewall");
}

void DestroyPlugin(struct Plugin * plugin)
{
	char * error=NULL;

	dlclose(plugin->Handle);
	error=dlerror();
	if(error!=NULL)
	{
		printf("Error: %s\n",error);
		exit(-1);
	}
}

void DestroyPlugins()
{
	DestroyPlugin(&DhcpPlugin);
	DestroyPlugin(&NatPlugin);
	DestroyPlugin(&FirewallPlugin);	
}

static int HandleDhcpRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"DHCP");
	DhcpPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleNatRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"NAT");
	NatPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleFirewallRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"Firewall");
	FirewallPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int EventHandler(struct mg_connection * connection, enum mg_event event)
{
	int result=MG_FALSE;

	if(event==MG_REQUEST)
	{
		mg_printf_data(connection,"Hello! Requested URI is [%s]",connection->uri);
		if(strcmp(connection->uri,"/DHCP")==0)
		{
			return HandleDhcpRequest(connection,event);
		}
		if(strcmp(connection->uri,"/NAT")==0)
		{
			return HandleNatRequest(connection,event);
		}
		if(strcmp(connection->uri,"/Firewall")==0)
		{
			return HandleFirewallRequest(connection,event);
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
	InitPlugins();
	struct mg_server * server;

	server=mg_create_server(NULL,EventHandler);
	mg_set_option(server,"listening_port","8080");

	printf("Starting on port %s\n",mg_get_option(server, "listening_port"));
	while(1)
	{
		mg_poll_server(server,1000);
	}

	mg_destroy_server(&server);
	DestroyPlugins();
	return 0;
}
