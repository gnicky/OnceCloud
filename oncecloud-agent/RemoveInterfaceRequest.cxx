#include <boost/property_tree/ptree.hpp>
#include "Request.h"
#include "RemoveInterfaceRequest.h"

using namespace std;
using namespace boost::property_tree;

RemoveInterfaceRequest::RemoveInterfaceRequest(string & rawRequest)
	: Request(rawRequest)
{
	string mac=this->GetJson().get<string>("mac");
	this->SetMac(mac);
}

RemoveInterfaceRequest::~RemoveInterfaceRequest()
{

}

string & RemoveInterfaceRequest::GetMac()
{
	return this->mac;
}

void RemoveInterfaceRequest::SetMac(string & mac)
{
	this->mac=mac;
}

