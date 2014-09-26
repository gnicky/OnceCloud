#pragma once

#include "Request.h"

class RemoveInterfaceRequest
	: public Request
{
public:
	RemoveInterfaceRequest(const std::string & rawRequest);
	~RemoveInterfaceRequest();

	const std::string & GetMac() const;

protected:
	void SetMac(const std::string & mac);

private:
	std::string mac;
};

