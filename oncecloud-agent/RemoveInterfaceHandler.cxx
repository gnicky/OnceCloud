#include <iostream>
#include <fstream>
#include <boost/filesystem.hpp>
#include <boost/algorithm/string.hpp>

#include "Process.h"
#include "RemoveInterfaceHandler.h"
#include "RemoveInterfaceRequest.h"
#include "RemoveInterfaceResponse.h"

using namespace std;
using namespace boost;

RemoveInterfaceHandler::RemoveInterfaceHandler()
{

}

RemoveInterfaceHandler::~RemoveInterfaceHandler()
{

}

Request * RemoveInterfaceHandler::ParseRequest(string & request)
{
	return new RemoveInterfaceRequest(request);
}

Response * RemoveInterfaceHandler::Handle(Request * request)
{
	RemoveInterfaceRequest * removeInterfaceRequest=dynamic_cast<RemoveInterfaceRequest *>(request);
	string name=replace_all_copy(removeInterfaceRequest->GetMac(),":","");
	string configureFileName="/etc/sysconfig/network-scripts/ifcfg-"+name;
	boost::filesystem::path configureFile(configureFileName);
	if(boost::filesystem::is_regular_file(configureFile))
	{
		Execute(("ifdown "+name).c_str());
		boost::filesystem::remove(configureFile);
	}
	return new RemoveInterfaceResponse(true);
}

