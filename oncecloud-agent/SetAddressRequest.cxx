#include <boost/property_tree/ptree.hpp>
#include "Request.h"
#include "SetAddressRequest.h"

using namespace std;
using namespace boost::property_tree;

SetAddressRequest::SetAddressRequest(string & rawRequest)
	: Request(rawRequest)
{
	string name=this->GetJson().get<string>("name");
	string mac=this->GetJson().get<string>("mac");
	string ipAddress=this->GetJson().get<string>("ipAddress");
	string netmask=this->GetJson().get<string>("netmask");
	string gateway=this->GetJson().get<string>("gateway");
	string dns=this->GetJson().get<string>("dns");
	this->SetName(name);
	this->SetMac(mac);
	this->SetIPAddress(ipAddress);
	this->SetNetmask(netmask);
	this->SetGateway(gateway);
	this->SetDns(dns);
}

SetAddressRequest::~SetAddressRequest()
{

}

string & SetAddressRequest::GetName()
{
	return this->name;
}

void SetAddressRequest::SetName(string & name)
{
	this->name=name;
}

string & SetAddressRequest::GetMac()
{
	return this->mac;
}

void SetAddressRequest::SetMac(string & mac)
{
	this->mac=mac;
}

string & SetAddressRequest::GetIPAddress()
{
	return this->ipAddress;
}

void SetAddressRequest::SetIPAddress(string & ipAddress)
{
	this->ipAddress=ipAddress;
}

string & SetAddressRequest::GetNetmask()
{
	return this->netmask;
}

void SetAddressRequest::SetNetmask(string & netmask)
{
	this->netmask=netmask;
}

string & SetAddressRequest::GetGateway()
{
	return this->gateway;
}

void SetAddressRequest::SetGateway(string & gateway)
{
	this->gateway=gateway;
}

string & SetAddressRequest::GetDns()
{
	return this->dns;
}

void SetAddressRequest::SetDns(string & dns)
{
	this->dns=dns;
}

