#pragma once

#include "Response.h"

class RemoveInterfaceResponse
	: public Response
{
public:
	RemoveInterfaceResponse(bool result);
	~RemoveInterfaceResponse();
	bool GetResult() const;

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
