#pragma once

#include <string>
#include "json/json.h"

class Response
{
public:
	Response();
	virtual ~Response();

	const std::string & GetRawResponse() const;
	const std::string & GetResponseType() const;

protected:
	Json::Value & GetJson();
	void SetJson(const Json::Value & json);
	void SetRawResponse(const std::string & rawResponse);
	void SetResponseType(const std::string & responseType);
	void BuildRawResponse();
	virtual void BuildJson()=0;

private:
	std::string rawResponse;
	std::string responseType;
	Json::Value json;
};

