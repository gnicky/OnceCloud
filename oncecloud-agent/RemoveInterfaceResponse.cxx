#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include "Response.h"
#include "RemoveInterfaceResponse.h"

using namespace std;
using namespace boost::property_tree;

RemoveInterfaceResponse::RemoveInterfaceResponse(bool result)
{
	string responseType="removeInterface";
	this->SetResponseType(responseType);
	this->SetResult(result);
	this->BuildRawResponse();
}

RemoveInterfaceResponse::~RemoveInterfaceResponse()
{

}

bool RemoveInterfaceResponse::GetResult()
{
	return this->result;
}

void RemoveInterfaceResponse::SetResult(bool result)
{
	this->result=result;
}

void RemoveInterfaceResponse::BuildJson()
{
	ptree json;
	this->SetJson(json);
	this->GetJson().put("responseType",this->GetResponseType());
	this->GetJson().put("result",this->GetResult());
}

