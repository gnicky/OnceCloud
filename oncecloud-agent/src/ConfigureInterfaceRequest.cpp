#include <string>

#include "json/json.h"
#include "Request.h"
#include "ConfigureInterfaceRequest.h"

ConfigureInterfaceRequest::ConfigureInterfaceRequest(const std::string & rawRequest)
	: Request(rawRequest)
{
	this->SetMac(this->GetJson().get("mac","").asString());
	this->SetIPAddress(this->GetJson().get("ipAddress","").asString());
	this->SetNetmask(this->GetJson().get("netmask","").asString());
	this->SetGateway(this->GetJson().get("gateway","").asString());
	this->SetDns(this->GetJson().get("dns","").asString());
}

ConfigureInterfaceRequest::~ConfigureInterfaceRequest()
{

}

const std::string & ConfigureInterfaceRequest::GetMac() const
{
	return this->mac;
}

void ConfigureInterfaceRequest::SetMac(const std::string & mac)
{
	this->mac=mac;
}

const std::string & ConfigureInterfaceRequest::GetIPAddress() const
{
	return this->ipAddress;
}

void ConfigureInterfaceRequest::SetIPAddress(const std::string & ipAddress)
{
	this->ipAddress=ipAddress;
}

const std::string & ConfigureInterfaceRequest::GetNetmask() const
{
	return this->netmask;
}

void ConfigureInterfaceRequest::SetNetmask(const std::string & netmask)
{
	this->netmask=netmask;
}

const std::string & ConfigureInterfaceRequest::GetGateway() const
{
	return this->gateway;
}

void ConfigureInterfaceRequest::SetGateway(const std::string & gateway)
{
	this->gateway=gateway;
}

const std::string & ConfigureInterfaceRequest::GetDns() const
{
	return this->dns;
}

void ConfigureInterfaceRequest::SetDns(const std::string & dns)
{
	this->dns=dns;
}

