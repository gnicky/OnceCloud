#include <string>
#include "json/json.h"
#include "Request.h"
#include "PingRequest.h"

PingRequest::PingRequest(const std::string & rawRequest)
	: Request(rawRequest)
{

}

PingRequest::~PingRequest()
{

}

