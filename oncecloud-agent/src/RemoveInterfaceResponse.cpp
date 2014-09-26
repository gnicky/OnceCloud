#include "json/json.h"
#include "Response.h"
#include "RemoveInterfaceResponse.h"

RemoveInterfaceResponse::RemoveInterfaceResponse(bool result)
{
	this->SetResponseType("removeInterface");
	this->SetResult(result);
	this->BuildRawResponse();
}

RemoveInterfaceResponse::~RemoveInterfaceResponse()
{

}

bool RemoveInterfaceResponse::GetResult() const
{
	return this->result;
}

void RemoveInterfaceResponse::SetResult(bool result)
{
	this->result=result;
}

void RemoveInterfaceResponse::BuildJson()
{
	Json::Value value;
	this->SetJson(value);
	this->GetJson()["responseType"]=this->GetResponseType();
	this->GetJson()["result"]=this->GetResult();
}

