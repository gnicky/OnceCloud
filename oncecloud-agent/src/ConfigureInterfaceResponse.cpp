#include "json/json.h"
#include "Response.h"
#include "ConfigureInterfaceResponse.h"

using namespace std;

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
	this->SetJson(Json::Value());
	this->GetJson()["responseType"]=this->GetResponseType();
	this->GetJson()["result"]=this->GetResult();
}

