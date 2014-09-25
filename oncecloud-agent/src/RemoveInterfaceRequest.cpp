#include "json/json.h"
#include "Request.h"
#include "RemoveInterfaceRequest.h"

using namespace std;

RemoveInterfaceRequest::RemoveInterfaceRequest(string rawRequest)
	: Request(rawRequest)
{
	Json::Value & value=this->GetJson();
	this->SetMac(value["mac"].asString());
}

RemoveInterfaceRequest::~RemoveInterfaceRequest()
{

}

string & RemoveInterfaceRequest::GetMac()
{
	return this->mac;
}

void RemoveInterfaceRequest::SetMac(string mac)
{
	this->mac=mac;
}

