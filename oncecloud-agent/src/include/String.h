#pragma once

#include <string>
#include <vector>

class String
{
public:
	static void Replace(std::string & source, const std::string & keyword, const std::string & replaceWith);
	static void Split(std::vector<std::string> & destination, const std::string & source, const std::string & delimiter);

private:
	String();
	~String();
};
