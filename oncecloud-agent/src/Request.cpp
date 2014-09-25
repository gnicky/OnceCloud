#include "json/json.h"
#include "Request.h"

using namespace std;

Request::Request(string rawRequest)
{
	Json::Reader reader;
	Json::Value value;
	reader.parse(rawRequest,value);
	this->SetRawRequest(rawRequest);
	this->SetJson(value);
	this->SetRequestType(value["requestType"].asString());
}

Request::~Request()
{

}

string & Request::GetRawRequest()
{
	return this->rawRequest;
}

void Request::SetRawRequest(string rawRequest)
{
	this->rawRequest=rawRequest;
}

string & Request::GetRequestType()
{
	return this->requestType;
}

void Request::SetRequestType(string requestType)
{
	this->requestType=requestType;
}

Json::Value & Request::GetJson()
{
	return this->json;
}

void Request::SetJson(Json::Value json)
{
	this->json=json;
}

