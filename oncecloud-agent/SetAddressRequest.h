#pragma once

#include <boost/property_tree/ptree.hpp>
#include "Request.h"

using namespace std;
using namespace boost::property_tree;

class SetAddressRequest
	: public Request
{
public:
	SetAddressRequest(string & rawRequest);
	~SetAddressRequest();

	string & GetName();
	string & GetMac();
	string & GetIPAddress();
	string & GetNetmask();
	string & GetGateway();
	string & GetDns();

protected:
	void SetName(string & name);
	void SetMac(string & mac);
	void SetIPAddress(string & ipAddress);
	void SetNetmask(string & netmask);
	void SetGateway(string & gateway);
	void SetDns(string & dns);

private:
	string name;
	string mac;
	string ipAddress;
	string netmask;
	string gateway;
	string dns;
};

