#pragma once

#include "Response.h"

class SetPasswordResponse
	: public Response
{
public:
	SetPasswordResponse(bool result);
	~SetPasswordResponse();
	bool GetResult() const;

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
