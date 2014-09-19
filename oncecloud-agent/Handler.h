#pragma once

#include "Request.h"
#include "Response.h"

class Handler
{
public:
	Handler();
	virtual ~Handler();
	virtual Response * Handle(Request * request);
};
