#pragma once

#include "Response.h"

class ConfigureInterfaceResponse
	: public Response
{
public:
	ConfigureInterfaceResponse(bool result);
	~ConfigureInterfaceResponse();
	bool GetResult() const;

protected:
	void SetResult(bool result);
	void BuildJson();

private:
	bool result;
};
