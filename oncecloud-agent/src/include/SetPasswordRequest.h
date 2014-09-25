#pragma once

#include "Request.h"

using namespace std;

class SetPasswordRequest
	: public Request
{
public:
	SetPasswordRequest(string rawRequest);
	~SetPasswordRequest();

	string & GetUserName();
	string & GetPassword();

protected:
	void SetUserName(string userName);
	void SetPassword(string password);	

private:
	string userName;
	string password;
};

