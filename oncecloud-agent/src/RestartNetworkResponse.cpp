#include "json/json.h"
#include "Response.h"
#include "RestartNetworkResponse.h"

RestartNetworkResponse::RestartNetworkResponse(bool result)
{
 	this->SetResponseType("Agent.RestartNetwork");
	this->SetResult(result);
	this->BuildRawResponse();
}

RestartNetworkResponse::~RestartNetworkResponse()
{

}

bool RestartNetworkResponse::GetResult() const
{
	return this->result;
}

void RestartNetworkResponse::SetResult(bool result)
{
	this->result=result;
}

void RestartNetworkResponse::BuildJson()
{
	Json::Value value;
	this->SetJson(value);
	this->GetJson()["responseType"]=this->GetResponseType();
	this->GetJson()["result"]=this->GetResult();
}

