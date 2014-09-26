#pragma once

#include <string>

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class ConfigureInterfaceHandler:
	public IHandler
{
public:
	ConfigureInterfaceHandler();
	~ConfigureInterfaceHandler();
	Request * ParseRequest(const std::string & request);
	Response * Handle(Request * request);
};
