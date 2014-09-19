#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include "Request.h"

using namespace std;
using namespace boost::property_tree;

Request::Request(string rawRequest)
{
	ptree json;
	stringstream stream(rawRequest);
	read_json<ptree>(stream,json);
	this->SetRawRequest(rawRequest);
	this->SetJson(json);
	this->SetRequestType(this->GetJson().get<string>("requestType"));
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

ptree & Request::GetJson()
{
	return this->json;
}

void Request::SetJson(ptree json)
{
	this->json=json;
}

