#pragma once

#include "Request.h"

class PingRequest
	: public Request
{
public:
	PingRequest(const std::string & rawRequest);
	~PingRequest();

};

