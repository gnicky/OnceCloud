#pragma once

#include <boost/property_tree/ptree.hpp>
#include "Request.h"

using namespace std;
using namespace boost::property_tree;

class RemoveInterfaceRequest
	: public Request
{
public:
	RemoveInterfaceRequest(string & rawRequest);
	~RemoveInterfaceRequest();

	string & GetMac();

protected:
	void SetMac(string & mac);

private:
	string mac;
};

