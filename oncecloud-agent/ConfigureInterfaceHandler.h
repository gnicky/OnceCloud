#pragma once

#include "Handler.h"
#include "Request.h"
#include "Response.h"

class ConfigureInterfaceHandler:
	public Handler
{
public:
	ConfigureInterfaceHandler();
	~ConfigureInterfaceHandler();
	Response * Handle(Request * request);
};
