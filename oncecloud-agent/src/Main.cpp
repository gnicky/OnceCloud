#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <boost/algorithm/string.hpp>

int main(int argc, char * argv [])
{
	std::string key;
	std::cout<<"New Password for root: ";
	std::cin>>key;

	std::ifstream readShadowFile("/etc/shadow");
	if(readShadowFile==NULL)
	{
		std::cout<<"Error opening /etc/shadow"<<std::endl;
		return 1;
	}

	std::string input;
	std::vector<std::string> list;
	std::vector<std::string> item;
	std::vector<std::string> password;

	while(std::getline(readShadowFile,input))
	{
		list.push_back(input);
	}

	readShadowFile.close();

	chmod("/etc/shadow",S_IWUSR);
	std::ofstream writeShadowFile("/etc/shadow");
	size_t i=0;
	size_t j=0;
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
	chmod("/etc/shadow",0);

	return 0;
}
