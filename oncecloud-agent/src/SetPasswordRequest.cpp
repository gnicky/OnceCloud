#include <string>
#include "json/json.h"
#include "Request.h"
#include "SetPasswordRequest.h"

SetPasswordRequest::SetPasswordRequest(const std::string & rawRequest)
	: Request(rawRequest)
{
	this->SetUserName(this->GetJson().get("userName","").asString());
	this->SetPassword(this->GetJson().get("password","").asString());
}

SetPasswordRequest::~SetPasswordRequest()
{

}

const std::string & SetPasswordRequest::GetUserName() const
{
	return this->userName;
}

void SetPasswordRequest::SetUserName(const std::string & userName)
{
	this->userName=userName;
}

const std::string & SetPasswordRequest::GetPassword() const
{
	return this->password;
}

void SetPasswordRequest::SetPassword(const std::string & password)
{
	this->password=password;
}

