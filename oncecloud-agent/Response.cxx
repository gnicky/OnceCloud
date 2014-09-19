#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include "Response.h"

using namespace std;
using namespace boost::property_tree;

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

ptree & Response::GetJson()
{
	return this->json;
}

void Response::SetJson(ptree json)
{
	this->json=json;
}

