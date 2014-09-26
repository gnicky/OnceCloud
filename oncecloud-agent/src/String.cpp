#include <string>
#include <vector>
#include "String.h"

void ReplaceString(std::string & source, const std::string & keyword, const std::string & replaceWith)
{
	std::string::size_type position=0;
	std::string::size_type keywordSize=keyword.size();
	std::string::size_type replaceSize=replaceWith.size();
	while((position=source.find(keyword,position))!=std::string::npos)
	{
		source.replace(position,keywordSize,replaceWith);
		position+=replaceSize;
	}
}

void SplitString(std::vector<std::string> & destination, const std::string & source, const std::string & delimiter)
{
	std::string temp;
	std::string::size_type position1;
	std::string::size_type position2;
	position2=source.find(delimiter);
	position1=0;
	while(position2!=std::string::npos)
	{
		destination.push_back(source.substr(position1,position2-position1));
		position1=position2+1;
		position2=source.find(delimiter,position1);
	}
	destination.push_back(source.substr(position1));
}

