#pragma once

#include "Response.h"

class RestartNetworkResponse
	: public Response
{
public:
	RestartNetworkResponse(bool result);
	~RestartNetworkResponse();
	bool GetResult() const;

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
