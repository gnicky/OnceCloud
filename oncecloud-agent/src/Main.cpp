#include <iostream>
#include <signal.h>
#include "HttpServer.h"

static HttpServer * server=NULL;

void OnInterrupt(int signal)
{
	if(server!=NULL)
	{
		server->Stop();
	}
}

void OnTerminate(int signal)
{
	if(server!=NULL)
	{
		server->Stop();
	}
}

void OnHangup(int signal)
{

}

int main(int argc, char * argv [])
{
	signal(SIGINT,OnInterrupt);
	signal(SIGTERM,OnTerminate);
	signal(SIGHUP,OnHangup);

	server=new HttpServer(9090);
	server->Start();
	delete server;

	return 0;
}
