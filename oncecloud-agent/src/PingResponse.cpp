#include "json/json.h"
#include "Response.h"
#include "PingResponse.h"

PingResponse::PingResponse()
{
 	this->SetResponseType("Agent.Ping");
	this->BuildRawResponse();
}

PingResponse::~PingResponse()
{

}

void PingResponse::BuildJson()
{
	Json::Value value;
	this->SetJson(value);
	this->GetJson()["responseType"]=this->GetResponseType();
}

