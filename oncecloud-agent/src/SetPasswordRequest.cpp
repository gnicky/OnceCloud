#include "json/json.h"
#include "Request.h"
#include "SetPasswordRequest.h"

using namespace std;

SetPasswordRequest::SetPasswordRequest(string rawRequest)
	: Request(rawRequest)
{
	Json::Value & value=this->GetJson();
	this->SetUserName(value["userName"].asString());
	this->SetPassword(value["password"].asString());
}

SetPasswordRequest::~SetPasswordRequest()
{

}

string & SetPasswordRequest::GetUserName()
{
	return this->userName;
}

void SetPasswordRequest::SetUserName(string userName)
{
	this->userName=userName;
}

string & SetPasswordRequest::GetPassword()
{
	return this->password;
}

void SetPasswordRequest::SetPassword(string password)
{
	this->password=password;
}

