#pragma once

#include <boost/property_tree/ptree.hpp>

using namespace std;
using namespace boost::property_tree;

class Request
{
public:
	Request(ptree & rawRequest);
	virtual ~Request();
	string GetRequestType();

protected:
	ptree & GetRawRequest();

private:
	ptree & rawRequest;
	string requestType;
	void SetRawRequest(ptree & rawRequest);
	void SetRequestType(string requestType);
};

