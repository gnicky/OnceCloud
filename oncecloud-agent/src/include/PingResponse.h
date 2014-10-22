#pragma once

#include "Response.h"

class PingResponse
	: public Response
{
public:
	PingResponse(bool result);
	~PingResponse();
	bool GetResult() const;

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
