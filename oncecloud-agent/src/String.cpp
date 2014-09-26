#include "String.h"

using namespace std;

string & ReplaceString(string & source, const string & keyword, const string & replaceWith)
{
	string::size_type position=0;
	string::size_type keywordSize=keyword.size();
	string::size_type replaceSize=replaceWith.size();
	while((position=source.find(keyword,position))!=string::npos)
	{
		source.replace(position,keywordSize,replaceWith);
		position+=replaceSize;
	}
	return source;
}

void SplitString(vector<string> & destination, const string & source, const string & delimiter)
{
	string temp;
	string::size_type position1;
	string::size_type position2;
	position2=source.find(delimiter);
	position1=0;
	while(position2!=string::npos)
	{
		destination.push_back(source.substr(position1,position2-position1));
		position1=position2+1;
		position2=source.find(delimiter,position1);
	}
	destination.push_back(source.substr(position1));
}

