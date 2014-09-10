#ifndef HTTP_SERVER_H
#define HTTP_SERVER_H 1

#include "mongoose.h"

class HttpServer
{
public:
	HttpServer();
	virtual ~HttpServer();
	void Start(unsigned short listeningPort);
	void Stop();

protected:
	static int MongooseEventHandler(mg_connection * connection, mg_event event);
	mg_server * GetMongooseServer();
	void SetMongooseServer(mg_server * mongooseServer);
	bool IsExiting();
	void SetExiting(bool exiting);
	
private:
	mg_server * mongooseServer;
	bool exiting;
};

#endif
