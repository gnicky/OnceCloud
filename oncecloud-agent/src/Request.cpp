#include <string>
#include "json/json.h"
#include "Request.h"

Request::Request(const std::string & rawRequest)
{
	Json::Reader reader;
	Json::Value value;
	reader.parse(rawRequest,value);
	this->SetRawRequest(rawRequest);
	this->SetJson(value);
	this->SetRequestType(value.get("requestType","").asString());
}

Request::~Request()
{

}

const std::string & Request::GetRawRequest() const
{
	return this->rawRequest;
}

void Request::SetRawRequest(const std::string & rawRequest)
{
	this->rawRequest=rawRequest;
}

const std::string & Request::GetRequestType() const
{
	return this->requestType;
}

void Request::SetRequestType(const std::string & requestType)
{
	this->requestType=requestType;
}

Json::Value & Request::GetJson()
{
	return this->json;
}

void Request::SetJson(const Json::Value & json)
{
	this->json=json;
}

