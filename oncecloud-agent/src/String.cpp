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
