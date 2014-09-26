#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

#include "SetPasswordHandler.h"
#include "SetPasswordRequest.h"
#include "SetPasswordResponse.h"
#include "String.h"

SetPasswordHandler::SetPasswordHandler()
{

}

SetPasswordHandler::~SetPasswordHandler()
{

}

Request * SetPasswordHandler::ParseRequest(const std::string & request)
{
	return new SetPasswordRequest(request);
}

Response * SetPasswordHandler::Handle(Request * request)
{
	SetPasswordRequest * setPasswordRequest=dynamic_cast<SetPasswordRequest *>(request);
	bool result=this->DoSetPassword(setPasswordRequest->GetUserName(),setPasswordRequest->GetPassword());
	return new SetPasswordResponse(result);
}

bool SetPasswordHandler::DoSetPassword(const std::string & userName, const std::string & password)
{
	char salt[30];
	this->GenerateSalt(salt);
	char * encryptedPassword=crypt(password.c_str(),salt);

	std::string inputLine;
	std::vector<std::string> list;
	size_t i=0;
	size_t j=0;
	int status=0;

	std::ifstream readShadowFile("/etc/shadow");
	if(readShadowFile==NULL)
	{
		return false;
	}
	while(getline(readShadowFile,inputLine))
	{
		list.push_back(inputLine);
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
		std::vector<std::string> item;
		String::Split(item,list[i],":");
		if(item[0]==userName)
		{
			writeShadowFile<<item[0]<<":"<<encryptedPassword<<":";
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

void SetPasswordHandler::GenerateSalt(char * salt)
{
	salt[0]='$';
	salt[1]='6';
	salt[2]='$';
	int i;
	int length=16;
	srand((unsigned int)(time(NULL)));
	int number=rand();
	for(i=3;i<length+3;i++)
	{
		number=rand()%64;
		salt[i]=this->MakeSalt(number);
	}
	salt[i++]='$';
	salt[i]='\0';
}

char SetPasswordHandler::MakeSalt(int number)
{
	if(number<26)
	{
		return 'a'+number;
	}
	else if(number<52)
	{
		return 'A'+(number-26);
	}
	else if(number<62)
	{
		return '0'+(number-52);
	}
	else if(number==63)
	{
		return '.';
	}
	else
	{
		return '/';
	}
}

