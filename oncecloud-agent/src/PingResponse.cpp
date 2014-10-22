#include "json/json.h"
#include "Response.h"
#include "PingResponse.h"

PingResponse::PingResponse(bool result)
{
	this->SetResponseType("Agent.Ping");
	this->SetResult(result);
	this->BuildRawResponse();
}

PingResponse::~PingResponse()
{

}

bool PingResponse::GetResult() const
{
	return this->result;
}

void PingResponse::SetResult(bool result)
{
	this->result=result;
}

void PingResponse::BuildJson()
{
	this->SetJson(Json::Value());
	this->GetJson()["responseType"]=this->GetResponseType();
	this->GetJson()["result"]=this->GetResult();
}

