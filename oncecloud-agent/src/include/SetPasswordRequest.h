#pragma once

#include "Request.h"

class SetPasswordRequest
	: public Request
{
public:
	SetPasswordRequest(const std::string & rawRequest);
	~SetPasswordRequest();

	const std::string & GetUserName() const;
	const std::string & GetPassword() const;

protected:
	void SetUserName(const std::string & userName);
	void SetPassword(const std::string & password);	

private:
	std::string userName;
	std::string password;
};

