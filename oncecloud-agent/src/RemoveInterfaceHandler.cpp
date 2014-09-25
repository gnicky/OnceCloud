#include <iostream>
#include <fstream>
#include <stdlib.h>

#include "Process.h"
#include "String.h"
#include "RemoveInterfaceHandler.h"
#include "RemoveInterfaceRequest.h"
#include "RemoveInterfaceResponse.h"

using namespace std;

RemoveInterfaceHandler::RemoveInterfaceHandler()
{

}

RemoveInterfaceHandler::~RemoveInterfaceHandler()
{

}

Request * RemoveInterfaceHandler::ParseRequest(string request)
{
	return new RemoveInterfaceRequest(request);
}

Response * RemoveInterfaceHandler::Handle(Request * request)
{
	RemoveInterfaceRequest * removeInterfaceRequest=dynamic_cast<RemoveInterfaceRequest *>(request);
	string name=removeInterfaceRequest->GetMac();
	ReplaceString(name,":","");
	string configFileName="/etc/sysconfig/network-scripts/ifcfg-"+name;
	if(access(configFileName.c_str(),F_OK)==0)
	{
		Execute(("ifdown "+name).c_str());
		remove(configFileName.c_str());
	}
	return new RemoveInterfaceResponse(true);
}

