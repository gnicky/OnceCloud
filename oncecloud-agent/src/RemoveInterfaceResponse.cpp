#include "json/json.h"
#include "Response.h"
#include "RemoveInterfaceResponse.h"

using namespace std;

RemoveInterfaceResponse::RemoveInterfaceResponse(bool result)
{
	string responseType="removeInterface";
	this->SetResponseType(responseType);
	this->SetResult(result);
	this->BuildRawResponse();
}

RemoveInterfaceResponse::~RemoveInterfaceResponse()
{

}

bool RemoveInterfaceResponse::GetResult()
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

