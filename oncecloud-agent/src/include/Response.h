#pragma once

#include "json/json.h"

using namespace std;

class Response
{
public:
	Response();
	virtual ~Response();

	string & GetRawResponse();
	string & GetResponseType();
	Json::Value & GetJson();

protected:
	void SetRawResponse(string rawResponse);
	void SetResponseType(string responseType);
	void SetJson(Json::Value json);
	void BuildRawResponse();
	virtual void BuildJson()=0;

private:
	string rawResponse;
	string responseType;
	Json::Value json;
};

