#include "json/json.h"
#include "Response.h"
#include "SetPasswordResponse.h"

SetPasswordResponse::SetPasswordResponse(bool result)
{
	this->SetResponseType("setPassword");
	this->SetResult(result);
	this->BuildRawResponse();
}

SetPasswordResponse::~SetPasswordResponse()
{

}

bool SetPasswordResponse::GetResult() const
{
	return this->result;
}

void SetPasswordResponse::SetResult(bool result)
{
	this->result=result;
}

void SetPasswordResponse::BuildJson()
{
	this->SetJson(Json::Value());
	this->GetJson()["responseType"]=this->GetResponseType();
	this->GetJson()["result"]=this->GetResult();
}

