#pragma once

#include "Response.h"

class PingResponse
	: public Response
{
public:
	PingResponse();
	~PingResponse();

protected:
	void BuildJson();

};
