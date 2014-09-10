#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <unistd.h>
#include <vector>
#include <boost/format.hpp>
#include <boost/algorithm/string.hpp>
#include <boost/lexical_cast.hpp>

struct UserInfo
{
	std::string UserName;
	std::string Salt;
	std::string Password;
};

int main(int argc, char * argv [])
{
	std::ostringstream oss;
	std::ifstream shadow("/etc/shadow");
	if(shadow==NULL)
	{
		std::cout<<"Error opening /etc/shadow"<<std::endl;
		return 1;
	}

	oss<<shadow.rdbuf();
	std::string content=oss.str();

	std::vector<std::string> list;
	std::vector<std::string> item;

	boost::split(list,content,boost::is_any_of("\n"));

	int i=0;
	for(i=0;i<list.size();i++)
	{
		boost::split(item,list[i],boost::is_any_of(":"));
		if(item[0]=="root")
		{
			std::cout<<item[1]<<std::endl;
		}
	}

	// const char * key="Test1234";
	// std::cout<<crypt(key,NULL)<<std::endl;

	return 0;
}
