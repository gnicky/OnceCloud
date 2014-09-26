#pragma once

#include "Request.h"

#include <string>

class ConfigureInterfaceRequest
	: public Request
{
public:
	ConfigureInterfaceRequest(const std::string & rawRequest);
	~ConfigureInterfaceRequest();

	const std::string & GetMac() const;
	const std::string & GetIPAddress() const;
	const std::string & GetNetmask() const;
	const std::string & GetGateway() const;
	const std::string & GetDns() const;

protected:
	void SetMac(const std::string & mac);
	void SetIPAddress(const std::string & ipAddress);
	void SetNetmask(const std::string & netmask);
	void SetGateway(const std::string & gateway);
	void SetDns(const std::string & dns);

private:
	std::string mac;
	std::string ipAddress;
	std::string netmask;
	std::string gateway;
	std::string dns;
};

