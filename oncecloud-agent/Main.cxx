#include <iostream>
#include <map>
#include <string.h>
#include <sys/file.h>

#include <boost/asio.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

#include "Request.h"
#include "Response.h"
#include "IHandler.h"
#include "SetPasswordRequest.h"
#include "SetPasswordHandler.h"
#include "ConfigureInterfaceRequest.h"
#include "ConfigureInterfaceHandler.h"

#define BUFFER_SIZE 1048576

char RequestBuffer[BUFFER_SIZE];
map<string, IHandler *> Handlers;
bool IsRunning;

void InitializeHandlers()
{
	Handlers["setPassword"]=new SetPasswordHandler();
	Handlers["configureInterface"]=new ConfigureInterfaceHandler();
}

void CleanupHandlers()
{
	delete Handlers["setPassword"];
	delete Handlers["configureInterface"];
}

int main(int argc, char * argv [])
{
	const char * serialPortPath="/dev/ttyS1";
	int serialPortDescriptor=open(serialPortPath,O_RDWR);
	if(flock(serialPortDescriptor,LOCK_EX|LOCK_NB)<0)
	{
		cout<<"Cannot lock /dev/ttyS1"<<endl;
		return 1;
	}

	boost::asio::io_service ioService;
	boost::asio::serial_port serialPort(ioService,serialPortPath);
	serialPort.set_option(boost::asio::serial_port::baud_rate(19200));
        serialPort.set_option(boost::asio::serial_port::flow_control(boost::asio::serial_port::flow_control::none));
        serialPort.set_option(boost::asio::serial_port::parity(boost::asio::serial_port::parity::none));
        serialPort.set_option(boost::asio::serial_port::stop_bits(boost::asio::serial_port::stop_bits::one));
        serialPort.set_option(boost::asio::serial_port::character_size(8));

	InitializeHandlers();
	IsRunning=true;
	while(IsRunning)
	{
		int requestLength;
		boost::asio::read(serialPort,boost::asio::buffer(&requestLength,sizeof(int)));	
		boost::asio::read(serialPort,boost::asio::buffer(RequestBuffer,requestLength),boost::asio::transfer_all());
		ioService.run();

		std::stringstream stream(RequestBuffer);
		boost::property_tree::ptree rawRequest;
		boost::property_tree::read_json<boost::property_tree::ptree>(stream,rawRequest);
		std::string requestType=rawRequest.get<std::string>("requestType");
		std::string requestString=string(RequestBuffer);

		IHandler * handler=Handlers[requestType];
		Request * request=handler->ParseRequest(requestString);
		Response * response=handler->Handle(request);

		int responseLength=response->GetRawResponse().size()+1;
		boost::asio::write(serialPort,boost::asio::buffer(&responseLength,sizeof(int)));
		boost::asio::write(serialPort,boost::asio::buffer(response->GetRawResponse().c_str(),responseLength));
		ioService.run();
		
		delete request;
		delete response;
	}
	CleanupHandlers();

	flock(serialPortDescriptor,LOCK_UN);
	close(serialPortDescriptor);

	return 0;
}

