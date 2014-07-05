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

static int HandleDhcpGetRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"DHCP GET<br/>");
	DhcpPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleDhcpPostRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"DHCP POST<br/>");
	DhcpPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleDhcpPutRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"DHCP PUT<br/>");
	DhcpPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleDhcpDeleteRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"DHCP DELETE<br/>");
	DhcpPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleDhcpRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"DHCP<br/>");
	if(strcmp(connection->request_method,"GET")==0)
	{
		return HandleDhcpGetRequest(connection,event);
	}
	if(strcmp(connection->request_method,"POST")==0)
	{
		return HandleDhcpPostRequest(connection,event);
	}
	if(strcmp(connection->request_method,"PUT")==0)
	{
		return HandleDhcpPutRequest(connection,event);
	}
	if(strcmp(connection->request_method,"DELETE")==0)
	{
		return HandleDhcpDeleteRequest(connection,event);
	}
	mg_printf_data(connection,"Unsupported method.<br/>");
	return MG_TRUE;
}

static int HandleNatGetRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"NAT GET<br/>");
	NatPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleNatPostRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"NAT POST<br/>");
	NatPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleNatPutRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"NAT PUT<br/>");
	NatPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleNatDeleteRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"NAT DELETE<br/>");
	NatPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleNatRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"NAT<br/>");
	if(strcmp(connection->request_method,"GET")==0)
	{
		return HandleNatGetRequest(connection,event);
	}
	if(strcmp(connection->request_method,"POST")==0)
	{
		return HandleNatPostRequest(connection,event);
	}
	if(strcmp(connection->request_method,"PUT")==0)
	{
		return HandleNatPutRequest(connection,event);
	}
	if(strcmp(connection->request_method,"DELETE")==0)
	{
		return HandleNatDeleteRequest(connection,event);
	}
	mg_printf_data(connection,"Unsupported method.<br/>");
	return MG_TRUE;
}

static int HandleFirewallGetRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"Firewall GET<br/>");
	FirewallPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleFirewallPostRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"Firewall POST<br/>");
	FirewallPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleFirewallPutRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"Firewall PUT<br/>");
	FirewallPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleFirewallDeleteRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"Firewall DELETE<br/>");
	FirewallPlugin.Activate(0,NULL);
	return MG_TRUE;
}

static int HandleFirewallRequest(struct mg_connection * connection, enum mg_event event)
{
	mg_printf_data(connection,"Firewall<br/>");
	if(strcmp(connection->request_method,"GET")==0)
	{
		return HandleFirewallGetRequest(connection,event);
	}
	if(strcmp(connection->request_method,"POST")==0)
	{
		return HandleFirewallPostRequest(connection,event);
	}
	if(strcmp(connection->request_method,"PUT")==0)
	{
		return HandleFirewallPutRequest(connection,event);
	}
	if(strcmp(connection->request_method,"DELETE")==0)
	{
		return HandleFirewallDeleteRequest(connection,event);
	}
	mg_printf_data(connection,"Unsupported method.<br/>");
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
