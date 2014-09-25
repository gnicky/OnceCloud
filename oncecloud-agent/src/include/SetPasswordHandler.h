#pragma once

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class SetPasswordHandler :
	public IHandler
{
public:
	SetPasswordHandler();
	~SetPasswordHandler();
	Request * ParseRequest(string & request);	
	Response * Handle(Request * request);

private:
	bool DoSetPassword(string & userName, string & password);
	void GenerateSalt(char * salt);
	char MakeSalt(int number);
};
