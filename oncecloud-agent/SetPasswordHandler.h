#pragma once

#include "Handler.h"
#include "Request.h"
#include "Response.h"

class SetPasswordHandler :
	public Handler
{
public:
	SetPasswordHandler();
	~SetPasswordHandler();
	Response * Handle(Request * request);

private:
	bool DoSetPassword(string & userName, string & password);
	void GenerateSalt(char * salt);
	char MakeSalt(int number);
};
