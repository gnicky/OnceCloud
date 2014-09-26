#pragma once

#include <string>

#include "IHandler.h"
#include "Request.h"
#include "Response.h"

class SetPasswordHandler :
	public IHandler
{
public:
	SetPasswordHandler();
	~SetPasswordHandler();
	Request * ParseRequest(const std::string & request);	
	Response * Handle(Request * request);

private:
	bool DoSetPassword(const std::string & userName, const std::string & password);
	void GenerateSalt(char * salt);
	char MakeSalt(int number);
};
