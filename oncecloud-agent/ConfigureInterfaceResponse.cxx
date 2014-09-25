#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include "Response.h"
#include "ConfigureInterfaceResponse.h"

using namespace std;
using namespace boost::property_tree;

ConfigureInterfaceResponse::ConfigureInterfaceResponse(bool result)
{
	string responseType="configureInterface";
	this->SetResponseType(responseType);
	this->SetResult(result);
	this->BuildRawResponse();
}

ConfigureInterfaceResponse::~ConfigureInterfaceResponse()
{

}

bool ConfigureInterfaceResponse::GetResult()
{
	return this->result;
}

void ConfigureInterfaceResponse::SetResult(bool result)
{
	this->result=result;
}

void ConfigureInterfaceResponse::BuildJson()
{
	ptree json;
	this->SetJson(json);
	this->GetJson().put("responseType",this->GetResponseType());
	this->GetJson().put("result",this->GetResult());
}

