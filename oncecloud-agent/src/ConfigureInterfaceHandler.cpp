#include <iostream>
#include <fstream>
#include <boost/filesystem.hpp>
#include <boost/algorithm/string.hpp>

#include "Process.h"
#include "ConfigureInterfaceHandler.h"
#include "ConfigureInterfaceRequest.h"
#include "ConfigureInterfaceResponse.h"

using namespace std;
using namespace boost;

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
	string name=replace_all_copy(configureInterfaceRequest->GetMac(),":","");
	string configureFileName="/etc/sysconfig/network-scripts/ifcfg-"+name;
	boost::filesystem::path configureFile(configureFileName);
	if(boost::filesystem::is_regular_file(configureFile))
	{
		Execute(("ifdown "+name).c_str());
	}
	ofstream configureFileStream(configureFile.file_string().c_str());
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

