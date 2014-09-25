#pragma once

#include "Response.h"

class RemoveInterfaceResponse
	: public Response
{
public:
	RemoveInterfaceResponse(bool result);
	~RemoveInterfaceResponse();
	bool GetResult();

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
