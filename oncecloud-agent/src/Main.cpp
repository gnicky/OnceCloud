#include <iostream>
#include <string>
#include <map>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/file.h>
#include <termios.h>

#include "json/json.h"
#include "Logger.h"
#include "Request.h"
#include "Response.h"
#include "IHandler.h"
#include "PingHandler.h"
#include "RestartNetworkHandler.h"
#include "SetPasswordHandler.h"
#include "ConfigureInterfaceHandler.h"
#include "RemoveInterfaceHandler.h"

#define BUFFER_SIZE 1048576

char requestBuffer[BUFFER_SIZE];
std::map<std::string, IHandler *> handlers;
bool isRunning;
Logger logger(LogLevel::Debug);

void OnInterrupt(int signal)
{
	logger.Write(LogLevel::Information,"Asked to interrupt. Exiting.");
	isRunning=false;
}

void OnTerminate(int signal)
{
	logger.Write(LogLevel::Information,"Asked to terminate. Exiting.");
	isRunning=false;
}

void OnHangup(int signal)
{
	logger.Write(LogLevel::Information,"Asked to hang up. Ignored.");
}

void InitializeHandlers()
{
	handlers["Agent.Ping"]=new PingHandler();
	handlers["Agent.RestartNetwork"]=new RestartNetworkHandler();
	handlers["Agent.SetPassword"]=new SetPasswordHandler();
	handlers["Router.ConfigureInterface"]=new ConfigureInterfaceHandler();
	handlers["Router.RemoveInterface"]=new RemoveInterfaceHandler();
}

void CleanupHandlers()
{
	delete handlers["Agent.Ping"];
	delete handlers["Agent.RestartNetwork"];
	delete handlers["Agent.SetPassword"];
	delete handlers["Router.ConfigureInterface"];
	delete handlers["Router.RemoveInterface"];
}

void DoRead(int fileDescriptor, void * buffer, int count)
{
	int readBytes=0;
	int remainingBytes=count;
	while(remainingBytes>0 && isRunning)
	{
		int currentRead=read(fileDescriptor,((char *)buffer)+readBytes,remainingBytes);
		readBytes+=currentRead;
		remainingBytes-=currentRead;
	}
}

void SetSerialPort(int serialPortDescriptor)
{
	termios serialPortOption;
	tcgetattr(serialPortDescriptor,&serialPortOption);
	tcflush(serialPortDescriptor,TCIOFLUSH);

	// Raw Mode
	serialPortOption.c_lflag&=~ICANON;
	serialPortOption.c_lflag&=~ECHO;
	serialPortOption.c_lflag&=~ECHOE;
	serialPortOption.c_lflag&=~ISIG;
	serialPortOption.c_oflag&=~OPOST;

	serialPortOption.c_cflag|=(CLOCAL|CREAD);
	serialPortOption.c_oflag&=~(ONLCR|OCRNL);
	serialPortOption.c_iflag&=~(ICRNL|INLCR);
	serialPortOption.c_iflag&=~(IXON|IXOFF|IXANY);

	// Character Size
	serialPortOption.c_cflag&=~CSIZE;
	serialPortOption.c_cflag|=CS8;
	// Parity
	serialPortOption.c_cflag&=~PARENB;
	serialPortOption.c_iflag&=~INPCK;
	// Stop Bits
	serialPortOption.c_cflag&=~CSTOPB;
	// Timeout and Minimum bytes
	serialPortOption.c_cc[VTIME]=0;
	serialPortOption.c_cc[VMIN]=0;
	// Baud Rate
	cfsetispeed(&serialPortOption,B115200);
	cfsetospeed(&serialPortOption,B115200);
	
	tcsetattr(serialPortDescriptor,TCSANOW,&serialPortOption);
	tcflush(serialPortDescriptor, TCIOFLUSH);
}

int main(int argc, char * argv [])
{
	signal(SIGINT,OnInterrupt);
	signal(SIGTERM,OnTerminate);
	signal(SIGHUP,OnHangup);
	
	logger.Write(LogLevel::Information,"BeyondCloud Agent started.");

	std::string serialPortPath="/dev/ttyS0";
	int serialPortDescriptor=open(serialPortPath.c_str(),O_RDWR|O_NOCTTY);

	if(serialPortDescriptor==-1)
	{
		logger.Write(LogLevel::Error,"Cannot open "+serialPortPath+".");
		return 1;
	}

	if(flock(serialPortDescriptor,LOCK_EX|LOCK_NB)<0)
	{
		logger.Write(LogLevel::Error,"Cannot lock "+serialPortPath+".");
		return 1;
	}

	SetSerialPort(serialPortDescriptor);

	logger.Write(LogLevel::Information,"Initialize handlers.");
	InitializeHandlers();
	isRunning=true;

	std::string info="Starting to listen on ";
	logger.Write(LogLevel::Information,"Starting to listen on "+serialPortPath);
	const char * ready="Ready";
	write(serialPortDescriptor,ready,strlen(ready));
	logger.Write(LogLevel::Information,"Greeting string written.");
	while(isRunning)
	{
		Request * request=NULL;
		Response * response=NULL;
		try
		{
			int requestLength=0;
			DoRead(serialPortDescriptor,&requestLength,sizeof(int));
			DoRead(serialPortDescriptor,requestBuffer,requestLength);

			if(!isRunning)
			{
				break;
			}

			Json::Reader reader;
			Json::Value value;
			reader.parse(requestBuffer,value);
			std::string requestType=value["requestType"].asString();
			std::string requestString=std::string(requestBuffer);
			logger.Write(LogLevel::Information,"Get Request: Type = "+requestType);

			request=handlers[requestType]->ParseRequest(requestString);
			response=handlers[requestType]->Handle(request);

			int responseLength=response->GetRawResponse().size();
			write(serialPortDescriptor,&responseLength,sizeof(int));
			write(serialPortDescriptor,response->GetRawResponse().c_str(),responseLength);
			logger.Write(LogLevel::Information,"Send Response: Type = "+requestType);
		}
		catch(std::exception & ex)
		{
			std::cout<<"Exception: "<<ex.what()<<std::endl;
		}

		if(request!=NULL)
		{
			delete request;
		}
		if(response!=NULL)
		{
			delete response;
		}
	}

	logger.Write(LogLevel::Information,"Cleanup handlers.");
	CleanupHandlers();

	flock(serialPortDescriptor,LOCK_UN);
	close(serialPortDescriptor);

	logger.Write(LogLevel::Information,"BeyondCloud Agent stopped.");
	return 0;
}

