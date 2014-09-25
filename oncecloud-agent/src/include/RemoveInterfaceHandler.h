#pragma once

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class RemoveInterfaceHandler:
	public IHandler
{
public:
	RemoveInterfaceHandler();
	~RemoveInterfaceHandler();
	Request * ParseRequest(string request);
	Response * Handle(Request * request);
};
