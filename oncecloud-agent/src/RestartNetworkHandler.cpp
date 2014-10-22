#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <unistd.h>

#include "Process.h"
#include "String.h"
#include "RestartNetworkHandler.h"
#include "RestartNetworkRequest.h"
#include "RestartNetworkResponse.h"

RestartNetworkHandler::RestartNetworkHandler()
{

}

RestartNetworkHandler::~RestartNetworkHandler()
{

}

Request * RestartNetworkHandler::ParseRequest(const std::string & request)
{
	return new RestartNetworkRequest(request);
}

Response * RestartNetworkHandler::Handle(Request * request)
{
	// RestartNetworkRequest * restartNetworkRequest=dynamic_cast<RestartNetworkRequest *>(request);
	Process::Execute("service network restart");
	return new RestartNetworkResponse(true);
}

