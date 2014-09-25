#pragma once

#include "json/json.h"

using namespace std;

class Request
{
public:
	Request(string rawRequest);
	virtual ~Request();

	string & GetRawRequest();
	string & GetRequestType();
	Json::Value & GetJson();

protected:
	void SetRawRequest(string rawRequest);
	void SetRequestType(string requestType);
	void SetJson(Json::Value json);

private:
	string rawRequest;
	string requestType;
	Json::Value json;
};

