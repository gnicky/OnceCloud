#pragma once

#include "Handler.h"
#include "Request.h"
#include "Response.h"

class SetAddressHandler:
	public Handler
{
public:
	SetAddressHandler();
	~SetAddressHandler();
	Response * Handle(Request * request);
};
