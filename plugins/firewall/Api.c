#include <stdio.h>

#include "PluginInterface.h"


const char * PluginName="Firewall";
const char * PluginVersion="1.0.0.0";

int Initialize()
{
	printf("Initialize firewall plugin.\n");
	return 0;
}

int Destroy()
{
	printf("Destroy firewall plugin.\n");
	return 0;
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("GET Firewall\n");
	// mg_send_status(connection,200);
	// mg_send_header(connection,"Content-Length","0");
	// mg_send_data(connection,"",0);
	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("HEAD Firewall\n");
	// mg_send_status(connection,200);
	// mg_send_header(connection,"Content-Length","0");
	// mg_send_data(connection,"",0);
	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("POST Firewall\n");
	// mg_send_status(connection,200);
	// mg_send_header(connection,"Content-Length","0");
	// mg_send_data(connection,"",0);
	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("PUT Firewall\n");
	// mg_send_status(connection,200);
	// mg_send_header(connection,"Content-Length","0");
	// mg_send_data(connection,"",0);
	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	printf("DELETE Firewall\n");
	// mg_send_status(connection,200);
	// mg_send_header(connection,"Content-Length","0");
	// mg_send_data(connection,"",0);
	return TRUE;
}

