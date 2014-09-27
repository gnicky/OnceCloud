#include <string>

#include "json/json.h"
#include "Response.h"
#include "ConfigureInterfaceResponse.h"

ConfigureInterfaceResponse::ConfigureInterfaceResponse(bool result)
{
 	this->SetResponseType("Router.ConfigureInterface");
	this->SetResult(result);
	this->BuildRawResponse();
}

ConfigureInterfaceResponse::~ConfigureInterfaceResponse()
{

}

bool ConfigureInterfaceResponse::GetResult() const
{
	return this->result;
}

void ConfigureInterfaceResponse::SetResult(bool result)
{
	this->result=result;
}

void ConfigureInterfaceResponse::BuildJson()
{
	this->SetJson(Json::Value());
	this->GetJson()["responseType"]=this->GetResponseType();
	this->GetJson()["result"]=this->GetResult();
}

