#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <boost/algorithm/string.hpp>

bool SetPassword(std::string user, std::string key)
{
	std::string input;
	std::vector<std::string> list;
	std::vector<std::string> item;
	std::vector<std::string> password;

	size_t i=0;
	size_t j=0;
	int status=0;

	std::ifstream readShadowFile("/etc/shadow");
	if(readShadowFile==NULL)
	{
		return false;
	}

	while(std::getline(readShadowFile,input))
	{
		list.push_back(input);
	}

	readShadowFile.close();

	status=chmod("/etc/shadow",S_IWUSR);
	if(status!=0)
	{
		return false;
	}

	std::ofstream writeShadowFile("/etc/shadow");
	for(i=0;i<list.size();i++)
	{
		boost::split(item,list[i],boost::is_any_of(":"));
		if(item[0]=="root")
		{
			std::string salt("");
			boost::split(password,item[1],boost::is_any_of("$"));
			for(j=0;j<password.size()-1;j++)
			{
				salt+=password[j];
				salt+="$";
			}
			std::string newPassword=crypt(key.c_str(),salt.c_str());
			writeShadowFile<<item[0]<<":"<<newPassword<<":";
			for(j=2;j<item.size()-1;j++)
			{
				writeShadowFile<<item[j]<<":";
			}
			writeShadowFile<<item[j]<<std::endl;			
		}
		else
		{
			for(j=0;j<item.size()-1;j++)
			{
				writeShadowFile<<item[j]<<":";
			}
			writeShadowFile<<item[j]<<std::endl;
		}
	}
	writeShadowFile.close();
	
	status=chmod("/etc/shadow",0);
	if(status!=0)
	{
		return false;
	}

	return true;
}
