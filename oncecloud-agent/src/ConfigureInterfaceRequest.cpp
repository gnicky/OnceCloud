#include "json/json.h"
#include "Request.h"
#include "ConfigureInterfaceRequest.h"

using namespace std;

ConfigureInterfaceRequest::ConfigureInterfaceRequest(string rawRequest)
	: Request(rawRequest)
{
	Json::Value & value=this->GetJson();
	this->SetMac(value["mac"].asString());
	this->SetIPAddress(value["ipAddress"].asString());
	this->SetNetmask(value["netmask"].asString());
	this->SetGateway(value["gateway"].isNull()?"":value["gateway"].asString());
	this->SetDns(value["dns"].isNull()?"":value["dns"].asString());
}

ConfigureInterfaceRequest::~ConfigureInterfaceRequest()
{

}

string & ConfigureInterfaceRequest::GetMac()
{
	return this->mac;
}

void ConfigureInterfaceRequest::SetMac(string mac)
{
	this->mac=mac;
}

string & ConfigureInterfaceRequest::GetIPAddress()
{
	return this->ipAddress;
}

void ConfigureInterfaceRequest::SetIPAddress(string ipAddress)
{
	this->ipAddress=ipAddress;
}

string & ConfigureInterfaceRequest::GetNetmask()
{
	return this->netmask;
}

void ConfigureInterfaceRequest::SetNetmask(string netmask)
{
	this->netmask=netmask;
}

string & ConfigureInterfaceRequest::GetGateway()
{
	return this->gateway;
}

void ConfigureInterfaceRequest::SetGateway(string gateway)
{
	this->gateway=gateway;
}

string & ConfigureInterfaceRequest::GetDns()
{
	return this->dns;
}

void ConfigureInterfaceRequest::SetDns(string dns)
{
	this->dns=dns;
}

