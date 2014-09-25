#include "json/json.h"
#include "Response.h"

using namespace std;

Response::Response()
{

}

Response::~Response()
{

}

string & Response::GetRawResponse()
{
	return this->rawResponse;
}

void Response::SetRawResponse(string rawResponse)
{
	this->rawResponse=rawResponse;
}

string & Response::GetResponseType()
{
	return this->responseType;
}

void Response::SetResponseType(string responseType)
{
	this->responseType=responseType;
}

Json::Value & Response::GetJson()
{
	return this->json;
}

void Response::SetJson(Json::Value json)
{
	this->json=json;
}

void Response::BuildRawResponse()
{
	this->BuildJson();
	this->SetRawResponse(this->GetJson().toStyledString());
}

