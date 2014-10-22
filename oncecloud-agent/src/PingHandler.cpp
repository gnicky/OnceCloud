#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <unistd.h>

#include "PingHandler.h"
#include "PingRequest.h"
#include "PingResponse.h"

PingHandler::PingHandler()
{

}

PingHandler::~PingHandler()
{

}

Request * PingHandler::ParseRequest(const std::string & request)
{
	return new PingRequest(request);
}

Response * PingHandler::Handle(Request * request)
{
	// PingRequest * pingRequest=dynamic_cast<PingRequest *>(request);
	return new PingResponse(true);
}

