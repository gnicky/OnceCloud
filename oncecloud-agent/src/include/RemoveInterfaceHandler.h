#pragma once

#include <string>

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class RemoveInterfaceHandler:
	public IHandler
{
public:
	RemoveInterfaceHandler();
	~RemoveInterfaceHandler();
	Request * ParseRequest(const std::string & request);
	Response * Handle(Request * request);
};
