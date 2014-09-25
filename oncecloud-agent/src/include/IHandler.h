#pragma once

#include "Request.h"
#include "Response.h"

class IHandler
{
public:
	virtual Request * ParseRequest(string request)=0;
	virtual Response * Handle(Request * request)=0;
};
