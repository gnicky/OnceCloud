#include <iostream>
#include <string.h>
#include <boost/asio.hpp>
#include <boost/bind.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

#include "Request.h"
#include "SetPasswordRequest.h"

#define BUFFER_SIZE 1048576

char RequestBuffer[BUFFER_SIZE];

Request * ParseRequest(boost::property_tree::ptree & rawRequest)
{
	std::string requestType=rawRequest.get<std::string>("requestType");
	if(requestType=="setPassword")
	{
		return new SetPasswordRequest(rawRequest);
	}
	return new Request(rawRequest);
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
	while(true)
	{
		int requestLength;
		boost::property_tree::ptree rawRequest;
		boost::asio::read(serialPort,boost::asio::buffer(&requestLength,sizeof(int)));	
		boost::asio::read(serialPort,boost::asio::buffer(RequestBuffer,requestLength),boost::asio::transfer_all());
		std::stringstream requestStream(RequestBuffer);
		boost::property_tree::read_json<boost::property_tree::ptree>(requestStream,rawRequest);

		Request * request=ParseRequest(rawRequest);
		if(dynamic_cast<SetPasswordRequest *>(request)!=NULL)
		{
			std::cout<<"Set Password Request"<<std::endl;
			SetPasswordRequest * setPasswordRequest=dynamic_cast<SetPasswordRequest *>(request);
			std::cout<<"User Name: "<<setPasswordRequest->GetUserName()<<std::endl;
			std::cout<<"Password: "<<setPasswordRequest->GetPassword()<<std::endl;
			response.put("responseType","setPassword");	
		}
		else
		{
			std::cout<<"Unknown request: "<<request->GetRequestType()<<std::endl;
		}

		boost::property_tree::ptree response;
		std::stringstream responseStream;
		boost::property_tree::write_json<boost::property_tree::ptree>(responseStream,response);
		int responseLength=responseStream.str().size()+1;
		boost::asio::write(serialPort,boost::asio::buffer(&responseLength,sizeof(int)));
		boost::asio::write(serialPort,boost::asio::buffer(responseStream.str().c_str(),responseLength));
		
		delete request;
		ioService.run();
	}
	return 0;
}
