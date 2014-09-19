#pragma once

#include <boost/property_tree/ptree.hpp>

using namespace std;
using namespace boost::property_tree;

class Response
{
public:
	Response();
	virtual ~Response();

	string & GetRawResponse();
	string & GetResponseType();
	ptree & GetJson();

protected:
	void SetRawResponse(string rawResponse);
	void SetResponseType(string responseType);
	void SetJson(ptree json);

private:
	string rawResponse;
	string responseType;
	ptree json;
};

