#pragma once

#include "Response.h"

class SetAddressResponse
	: public Response
{
public:
	SetAddressResponse(bool result);
	~SetAddressResponse();
	bool GetResult();

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
