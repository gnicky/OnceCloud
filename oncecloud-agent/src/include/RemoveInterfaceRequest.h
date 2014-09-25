#pragma once

#include "Request.h"

using namespace std;

class RemoveInterfaceRequest
	: public Request
{
public:
	RemoveInterfaceRequest(string rawRequest);
	~RemoveInterfaceRequest();

	string & GetMac();

protected:
	void SetMac(string mac);

private:
	string mac;
};

