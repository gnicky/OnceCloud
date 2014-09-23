#include <boost/property_tree/ptree.hpp>
#include "Request.h"
#include "SetPasswordRequest.h"

using namespace std;
using namespace boost::property_tree;

SetPasswordRequest::SetPasswordRequest(string & rawRequest)
	: Request(rawRequest)
{
	string userName=this->GetJson().get<string>("userName");
	string password=this->GetJson().get<string>("password");
	this->SetUserName(userName);
	this->SetPassword(password);
}

SetPasswordRequest::~SetPasswordRequest()
{

}

string & SetPasswordRequest::GetUserName()
{
	return this->userName;
}

void SetPasswordRequest::SetUserName(string & userName)
{
	this->userName=userName;
}

string & SetPasswordRequest::GetPassword()
{
	return this->password;
}

void SetPasswordRequest::SetPassword(string & password)
{
	this->password=password;
}

