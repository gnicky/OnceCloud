#pragma once

#include <string>

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class RestartNetworkHandler:
	public IHandler
{
public:
	RestartNetworkHandler();
	~RestartNetworkHandler();
	Request * ParseRequest(const std::string & request);
	Response * Handle(Request * request);
};
