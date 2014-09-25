#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include "Response.h"
#include "SetPasswordResponse.h"

using namespace std;
using namespace boost::property_tree;

SetPasswordResponse::SetPasswordResponse(bool result)
{
	string responseType="setPassword";
	this->SetResponseType(responseType);
	this->SetResult(result);
	this->BuildRawResponse();
}

SetPasswordResponse::~SetPasswordResponse()
{

}

bool SetPasswordResponse::GetResult()
{
	return this->result;
}

void SetPasswordResponse::SetResult(bool result)
{
	this->result=result;
}

void SetPasswordResponse::BuildJson()
{
	ptree json;
	this->SetJson(json);
	this->GetJson().put("responseType",this->GetResponseType());
	this->GetJson().put("result",this->GetResult());
}

