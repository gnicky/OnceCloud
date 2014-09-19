#pragma once

#include <boost/property_tree/ptree.hpp>

using namespace std;
using namespace boost::property_tree;

class Request
{
public:
	Request(string rawRequest);
	virtual ~Request();

	string & GetRawRequest();
	string & GetRequestType();
	ptree & GetJson();

protected:
	void SetRawRequest(string rawRequest);
	void SetRequestType(string requestType);
	void SetJson(ptree json);

private:
	string rawRequest;
	string requestType;
	ptree json;
};

