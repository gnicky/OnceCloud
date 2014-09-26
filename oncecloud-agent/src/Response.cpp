#include <string>

#include "json/json.h"
#include "Response.h"

Response::Response()
{

}

Response::~Response()
{

}

const std::string & Response::GetRawResponse() const
{
	return this->rawResponse;
}

void Response::SetRawResponse(const std::string & rawResponse)
{
	this->rawResponse=rawResponse;
}

const std::string & Response::GetResponseType() const
{
	return this->responseType;
}

void Response::SetResponseType(const std::string & responseType)
{
	this->responseType=responseType;
}

Json::Value & Response::GetJson()
{
	return this->json;
}

void Response::SetJson(const Json::Value & json)
{
	this->json=json;
}

void Response::BuildRawResponse()
{
	this->BuildJson();
	this->SetRawResponse(this->GetJson().toStyledString());
}

