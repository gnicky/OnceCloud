#pragma once

#include "Request.h"

class RestartNetworkRequest
	: public Request
{
public:
	RestartNetworkRequest(const std::string & rawRequest);
	~RestartNetworkRequest();

};

