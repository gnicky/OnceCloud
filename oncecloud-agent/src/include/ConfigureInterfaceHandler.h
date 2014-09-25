#pragma once

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class ConfigureInterfaceHandler:
	public IHandler
{
public:
	ConfigureInterfaceHandler();
	~ConfigureInterfaceHandler();
	Request * ParseRequest(string request);
	Response * Handle(Request * request);
};
