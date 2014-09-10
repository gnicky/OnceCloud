#include <string>
#include <boost/lexical_cast.hpp>
#include "mongoose.h"
#include "HttpServer.h"

#define POLL_INTERVAL 1000

HttpServer::HttpServer(unsigned short listeningPort)
{
	this->SetExiting(false);
	std::string listeningPortString=boost::lexical_cast<std::string>(listeningPort);
	this->SetMongooseServer(mg_create_server(NULL,this->MongooseEventHandler));
	mg_set_option(this->GetMongooseServer(),"listening_port",listeningPortString.c_str());
}

HttpServer::~HttpServer()
{
	while(this->IsExiting())
	{

	}
	mg_server * server=this->GetMongooseServer();
	mg_destroy_server(&server);
}

void HttpServer::Start()
{
	while(!this->IsExiting())
	{
		mg_poll_server(this->GetMongooseServer(),POLL_INTERVAL);
	}
	this->SetExiting(false);
}

void HttpServer::Stop()
{
	this->SetExiting(true);
}

mg_server * HttpServer::GetMongooseServer()
{
	return this->mongooseServer;
}

void HttpServer::SetMongooseServer(mg_server * mongooseServer)
{
	this->mongooseServer=mongooseServer;
}

bool HttpServer::IsExiting()
{
	return this->exiting;
}

void HttpServer::SetExiting(bool exiting)
{
	this->exiting=exiting;
}

int HttpServer::MongooseEventHandler(mg_connection * connection, mg_event event)
{
	int result=MG_FALSE;

	if(event==MG_REQUEST)
	{
		// 404 Not Found
		mg_send_status(connection,404);
		mg_send_header(connection,"Content-Length","0");
		mg_send_data(connection,"",0);			
		
		result=MG_TRUE;
	}
	else if(event==MG_AUTH)
	{
		result=MG_TRUE;
	}

	return result;
}
