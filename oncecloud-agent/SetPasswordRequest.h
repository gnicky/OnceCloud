#pragma once

#include <boost/property_tree/ptree.hpp>
#include "Request.h"

using namespace std;
using namespace boost::property_tree;

class SetPasswordRequest
	: public Request
{
public:
	SetPasswordRequest(ptree & rawRequest);
	~SetPasswordRequest();
	string GetUserName();
	string GetPassword();

private:
	string userName;
	string password;
	void SetUserName(string userName);
	void SetPassword(string password);
};

