#include <iostream>
#include <string.h>

#include <boost/asio.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

#include "Handler.h"
#include "SetPasswordHandler.h"
#include "Request.h"
#include "SetPasswordRequest.h"
#include "Response.h"

#define BUFFER_SIZE 1048576

char RequestBuffer[BUFFER_SIZE];

Request * ParseRequest(char * rawRequest)
{
	std::stringstream stream(rawRequest);
	boost::property_tree::ptree request;
	boost::property_tree::read_json<boost::property_tree::ptree>(stream,request);
	std::string requestType=request.get<std::string>("requestType");
	if(requestType=="setPassword")
	{
		return new SetPasswordRequest(rawRequest);
	}
	return NULL;
}

Response * HandleRequest(Request * request)
{
	Handler * handler=NULL;
	Response * response=NULL;
	if(dynamic_cast<SetPasswordRequest *>(request)!=NULL)
	{
		handler=new SetPasswordHandler();
	}
	if(handler!=NULL)
	{
		response=handler->Handle(request);
	}
	delete handler;
	return response;
}

int main(int argc, char * argv [])
{
	boost::asio::io_service ioService;
	boost::asio::serial_port serialPort(ioService,"/dev/ttyS1");
	serialPort.set_option(boost::asio::serial_port::baud_rate(19200));
        serialPort.set_option(boost::asio::serial_port::flow_control(boost::asio::serial_port::flow_control::none));
        serialPort.set_option(boost::asio::serial_port::parity(boost::asio::serial_port::parity::none));
        serialPort.set_option(boost::asio::serial_port::stop_bits(boost::asio::serial_port::stop_bits::one));
        serialPort.set_option(boost::asio::serial_port::character_size(8));
	delete ((int *)NULL);
	while(true)
	{
		int requestLength;
		boost::asio::read(serialPort,boost::asio::buffer(&requestLength,sizeof(int)));	
		boost::asio::read(serialPort,boost::asio::buffer(RequestBuffer,requestLength),boost::asio::transfer_all());

		Request * request=ParseRequest(RequestBuffer);
		Response * response=HandleRequest(request);
		if(dynamic_cast<SetPasswordRequest *>(request)!=NULL)
		{
			std::cout<<"Set Password Request"<<std::endl;
			SetPasswordRequest * setPasswordRequest=dynamic_cast<SetPasswordRequest *>(request);
			std::cout<<"User Name: "<<setPasswordRequest->GetUserName()<<std::endl;
			std::cout<<"Password: "<<setPasswordRequest->GetPassword()<<std::endl;
		}
		else
		{
			std::cout<<"Unknown request: "<<request->GetRequestType()<<std::endl;
		}

		cout<<(void *)(response)<<endl;
		boost::property_tree::ptree responseJson;
		std::stringstream responseStream;
		boost::property_tree::write_json<boost::property_tree::ptree>(responseStream,responseJson);
		int responseLength=responseStream.str().size()+1;
		boost::asio::write(serialPort,boost::asio::buffer(&responseLength,sizeof(int)));
		boost::asio::write(serialPort,boost::asio::buffer(responseStream.str().c_str(),responseLength));
		
		delete request;
		ioService.run();
	}
	return 0;
}
