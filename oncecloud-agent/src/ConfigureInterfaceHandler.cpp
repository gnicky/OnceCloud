#include <iostream>
#include <fstream>
#include <string>

#include "Process.h"
#include "String.h"
#include "ConfigureInterfaceHandler.h"
#include "ConfigureInterfaceRequest.h"
#include "ConfigureInterfaceResponse.h"

ConfigureInterfaceHandler::ConfigureInterfaceHandler()
{

}

ConfigureInterfaceHandler::~ConfigureInterfaceHandler()
{

}

Request * ConfigureInterfaceHandler::ParseRequest(const std::string & request)
{
	return new ConfigureInterfaceRequest(request);
}

Response * ConfigureInterfaceHandler::Handle(Request * request)
{
	ConfigureInterfaceRequest * configureInterfaceRequest=dynamic_cast<ConfigureInterfaceRequest *>(request);
	std::string name=configureInterfaceRequest->GetMac();
	ReplaceString(name,":","");
	std::string configFileName="/etc/sysconfig/network-scripts/ifcfg-"+name;
	if(access(configFileName.c_str(),F_OK)==0)
	{
		Execute(("ifdown "+name).c_str());
	}
	std::ofstream configureFileStream(configFileName.c_str());
	configureFileStream<<"NAME=\""<<name<<"\""<<std::endl;
	configureFileStream<<"HWADDR=\""<<configureInterfaceRequest->GetMac()<<"\""<<std::endl;
	configureFileStream<<"IPADDR=\""<<configureInterfaceRequest->GetIPAddress()<<"\""<<std::endl;
	configureFileStream<<"NETMASK=\""<<configureInterfaceRequest->GetNetmask()<<"\""<<std::endl;
	if(configureInterfaceRequest->GetGateway()!="")
	{
		configureFileStream<<"GATEWAY=\""<<configureInterfaceRequest->GetGateway()<<"\""<<std::endl;
	}
	if(configureInterfaceRequest->GetDns()!="")
	{
		configureFileStream<<"DNS1=\""<<configureInterfaceRequest->GetDns()<<"\""<<std::endl;
	}
	configureFileStream<<"ONBOOT=\"yes\""<<std::endl;
	configureFileStream.close();
	Execute(("ifup "+name).c_str());
	return new ConfigureInterfaceResponse(true);
}

