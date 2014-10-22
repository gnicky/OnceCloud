#pragma once

#include <string>

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class PingHandler:
	public IHandler
{
public:
	PingHandler();
	~PingHandler();
	Request * ParseRequest(const std::string & request);
	Response * Handle(Request * request);
};
