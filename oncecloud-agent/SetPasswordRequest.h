#pragma once

#include <boost/property_tree/ptree.hpp>
#include "Request.h"

using namespace std;
using namespace boost::property_tree;

class SetPasswordRequest
	: public Request
{
public:
	SetPasswordRequest(string rawRequest);
	~SetPasswordRequest();

	string GetUserName();
	string GetPassword();

protected:
	void SetUserName(string userName);
	void SetPassword(string password);	

private:
	string userName;
	string password;
};

