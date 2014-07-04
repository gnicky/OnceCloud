#include <stdio.h>
#include <string.h>

#include "Mongoose.h"

static int EventHandler(struct mg_connection * connection, enum mg_event event)
{
	int result = MG_FALSE;

	if(event==MG_REQUEST)
	{
		mg_printf_data(connection,"Hello! Requested URI is [%s]",connection->uri);
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
	struct mg_server * server;

	server=mg_create_server(NULL,EventHandler);
	mg_set_option(server,"listening_port","8080");

	printf("Starting on port %s\n",mg_get_option(server, "listening_port"));
	while(1)
	{
		mg_poll_server(server,1000);
	}

	mg_destroy_server(&server);

	return 0;
}
