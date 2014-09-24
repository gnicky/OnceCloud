#include <iostream>

#include "SetAddressHandler.h"
#include "SetAddressRequest.h"
#include "SetAddressResponse.h"

using namespace std;

SetAddressHandler::SetAddressHandler()
{

}

SetAddressHandler::~SetAddressHandler()
{

}

Response * SetAddressHandler::Handle(Request * request)
{
	SetAddressRequest * setAddressRequest=dynamic_cast<SetAddressRequest *>(request);
	cout<<"SetAddressRequest"<<endl;
	cout<<"Name="<<setAddressRequest->GetName()<<endl;
	cout<<"MAC="<<setAddressRequest->GetMac()<<endl;
	cout<<"IP Address="<<setAddressRequest->GetIPAddress()<<endl;
	cout<<"Netmask="<<setAddressRequest->GetNetmask()<<endl;
	cout<<"Gateway="<<setAddressRequest->GetGateway()<<endl;
	cout<<"DNS="<<setAddressRequest->GetDns()<<endl;
	bool result=true;
	return new SetAddressResponse(result);
}
