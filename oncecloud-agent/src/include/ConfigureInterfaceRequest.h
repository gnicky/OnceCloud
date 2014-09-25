#pragma once

#include "Request.h"

using namespace std;

class ConfigureInterfaceRequest
	: public Request
{
public:
	ConfigureInterfaceRequest(string rawRequest);
	~ConfigureInterfaceRequest();

	string & GetMac();
	string & GetIPAddress();
	string & GetNetmask();
	string & GetGateway();
	string & GetDns();

protected:
	void SetMac(string mac);
	void SetIPAddress(string ipAddress);
	void SetNetmask(string netmask);
	void SetGateway(string gateway);
	void SetDns(string dns);

private:
	string mac;
	string ipAddress;
	string netmask;
	string gateway;
	string dns;
};

