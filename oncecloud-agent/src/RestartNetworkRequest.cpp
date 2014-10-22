#include <string>
#include "json/json.h"
#include "Request.h"
#include "RestartNetworkRequest.h"

RestartNetworkRequest::RestartNetworkRequest(const std::string & rawRequest)
	: Request(rawRequest)
{

}

RestartNetworkRequest::~RestartNetworkRequest()
{

}

