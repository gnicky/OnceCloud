#include <boost/property_tree/ptree.hpp>
#include "Request.h"

using namespace std;
using namespace boost::property_tree;

Request::Request(ptree & rawRequest)
	:rawRequest(rawRequest)
{
	this->SetRequestType(this->GetRawRequest().get<string>("requestType"));
}

Request::~Request()
{

}

ptree & Request::GetRawRequest()
{
	return this->rawRequest;
}

void Request::SetRawRequest(ptree & rawRequest)
{
	this->rawRequest=rawRequest;
}

string Request::GetRequestType()
{
	return this->requestType;
}

void Request::SetRequestType(string requestType)
{
	this->requestType=requestType;
}

