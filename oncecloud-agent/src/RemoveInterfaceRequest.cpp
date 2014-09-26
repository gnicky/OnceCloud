#include <string>
#include "json/json.h"
#include "Request.h"
#include "RemoveInterfaceRequest.h"

RemoveInterfaceRequest::RemoveInterfaceRequest(const std::string & rawRequest)
	: Request(rawRequest)
{
	this->SetMac(this->GetJson().get("mac","").asString());
}

RemoveInterfaceRequest::~RemoveInterfaceRequest()
{

}

const std::string & RemoveInterfaceRequest::GetMac() const
{
	return this->mac;
}

void RemoveInterfaceRequest::SetMac(const std::string & mac)
{
	this->mac=mac;
}

