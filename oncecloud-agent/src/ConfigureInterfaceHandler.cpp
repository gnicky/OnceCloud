#include <iostream>
#include <fstream>

#include "Process.h"
#include "String.h"
#include "ConfigureInterfaceHandler.h"
#include "ConfigureInterfaceRequest.h"
#include "ConfigureInterfaceResponse.h"

using namespace std;

ConfigureInterfaceHandler::ConfigureInterfaceHandler()
{

}

ConfigureInterfaceHandler::~ConfigureInterfaceHandler()
{

}

Request * ConfigureInterfaceHandler::ParseRequest(string request)
{
	return new ConfigureInterfaceRequest(request);
}

Response * ConfigureInterfaceHandler::Handle(Request * request)
{
	ConfigureInterfaceRequest * configureInterfaceRequest=dynamic_cast<ConfigureInterfaceRequest *>(request);
	string name=configureInterfaceRequest->GetMac();
	ReplaceString(name,":","");
	string configFileName="/etc/sysconfig/network-scripts/ifcfg-"+name;
	if(access(configFileName.c_str(),F_OK)==0)
	{
		Execute(("ifdown "+name).c_str());
	}
	ofstream configureFileStream(configFileName.c_str());
	configureFileStream<<"NAME=\""<<name<<"\""<<endl;
	configureFileStream<<"HWADDR=\""<<configureInterfaceRequest->GetMac()<<"\""<<endl;
	configureFileStream<<"IPADDR=\""<<configureInterfaceRequest->GetIPAddress()<<"\""<<endl;
	configureFileStream<<"NETMASK=\""<<configureInterfaceRequest->GetNetmask()<<"\""<<endl;
	if(configureInterfaceRequest->GetGateway()!="")
	{
		configureFileStream<<"GATEWAY=\""<<configureInterfaceRequest->GetGateway()<<"\""<<endl;
	}
	if(configureInterfaceRequest->GetDns()!="")
	{
		configureFileStream<<"DNS1=\""<<configureInterfaceRequest->GetDns()<<"\""<<endl;
	}
	configureFileStream<<"ONBOOT=\"yes\""<<endl;
	configureFileStream.close();
	Execute(("ifup "+name).c_str());
	return new ConfigureInterfaceResponse(true);
}

