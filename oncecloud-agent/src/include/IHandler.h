#pragma once

#include <string>

#include "Request.h"
#include "Response.h"

class IHandler
{
public:
	virtual Request * ParseRequest(const std::string & request)=0;
	virtual Response * Handle(Request * request)=0;
};
