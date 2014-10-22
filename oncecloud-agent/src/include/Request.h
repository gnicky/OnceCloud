#pragma once

#include <string>
#include "json/json.h"

class Request
{
public:
	Request(const std::string & rawRequest);
	virtual ~Request();

	const std::string & GetRawRequest() const;
	const std::string & GetRequestType() const;

protected:
	Json::Value & GetJson();
	void SetJson(const Json::Value & json);
	void SetRawRequest(const std::string & rawRequest);
	void SetRequestType(const std::string & requestType);

private:
	std::string rawRequest;
	std::string requestType;
	Json::Value json;
};

