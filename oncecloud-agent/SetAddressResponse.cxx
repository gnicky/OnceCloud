#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>
#include "Response.h"
#include "SetAddressResponse.h"

using namespace std;
using namespace boost::property_tree;

SetAddressResponse::SetAddressResponse(bool result)
{
	string responseType="setAddress";
	this->SetResponseType(responseType);
	this->SetResult(result);
	this->BuildRawResponse();
}

SetAddressResponse::~SetAddressResponse()
{

}

bool SetAddressResponse::GetResult()
{
	return this->result;
}

void SetAddressResponse::SetResult(bool result)
{
	this->result=result;
}

void SetAddressResponse::BuildJson()
{
	ptree json;
	this->SetJson(json);
	this->GetJson().put("responseType",this->GetResponseType());
	this->GetJson().put("result",this->GetResult());
}

