#include <boost/property_tree/ptree.hpp>
#include "Request.h"
#include "ConfigureInterfaceRequest.h"

using namespace std;
using namespace boost::property_tree;

ConfigureInterfaceRequest::ConfigureInterfaceRequest(string & rawRequest)
	: Request(rawRequest)
{
	string mac=this->GetJson().get<string>("mac");
	string ipAddress=this->GetJson().get<string>("ipAddress");
	string netmask=this->GetJson().get<string>("netmask");
	string gateway=this->GetJson().get<string>("gateway","");
	string dns=this->GetJson().get<string>("dns","");
	this->SetMac(mac);
	this->SetIPAddress(ipAddress);
	this->SetNetmask(netmask);
	this->SetGateway(gateway);
	this->SetDns(dns);
}

ConfigureInterfaceRequest::~ConfigureInterfaceRequest()
{

}

string & ConfigureInterfaceRequest::GetMac()
{
	return this->mac;
}

void ConfigureInterfaceRequest::SetMac(string & mac)
{
	this->mac=mac;
}

string & ConfigureInterfaceRequest::GetIPAddress()
{
	return this->ipAddress;
}

void ConfigureInterfaceRequest::SetIPAddress(string & ipAddress)
{
	this->ipAddress=ipAddress;
}

string & ConfigureInterfaceRequest::GetNetmask()
{
	return this->netmask;
}

void ConfigureInterfaceRequest::SetNetmask(string & netmask)
{
	this->netmask=netmask;
}

string & ConfigureInterfaceRequest::GetGateway()
{
	return this->gateway;
}

void ConfigureInterfaceRequest::SetGateway(string & gateway)
{
	this->gateway=gateway;
}

string & ConfigureInterfaceRequest::GetDns()
{
	return this->dns;
}

void ConfigureInterfaceRequest::SetDns(string & dns)
{
	this->dns=dns;
}

