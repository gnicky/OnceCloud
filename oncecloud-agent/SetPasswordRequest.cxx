#include <boost/property_tree/ptree.hpp>
#include "Request.h"
#include "SetPasswordRequest.h"

using namespace std;
using namespace boost::property_tree;

SetPasswordRequest::SetPasswordRequest(ptree & rawRequest)
	: Request(rawRequest)
{
	this->SetUserName(this->GetRawRequest().get<string>("userName"));
	this->SetPassword(this->GetRawRequest().get<string>("password"));
}

SetPasswordRequest::~SetPasswordRequest()
{

}

string SetPasswordRequest::GetUserName()
{
	return this->userName;
}

void SetPasswordRequest::SetUserName(string userName)
{
	this->userName=userName;
}

string SetPasswordRequest::GetPassword()
{
	return this->password;
}

void SetPasswordRequest::SetPassword(string password)
{
	this->password=password;
}
