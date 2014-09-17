#include <iostream>
#include <string.h>
#include <boost/asio.hpp>
#include <boost/bind.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

#define BUFFER_SIZE 1048576

char Buffer[BUFFER_SIZE];

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
		boost::asio::read(serialPort,boost::asio::buffer(&requestLength,sizeof(int)));	
		boost::asio::read(serialPort,boost::asio::buffer(Buffer,requestLength),boost::asio::transfer_all());
		std::stringstream stream;
		stream<<Buffer;
		boost::property_tree::ptree content;
		boost::property_tree::read_json<boost::property_tree::ptree>(stream,content);
		std::cout<<content.get<std::string>("requestType")<<std::endl;
		ioService.run();
	}
	return 0;
}
