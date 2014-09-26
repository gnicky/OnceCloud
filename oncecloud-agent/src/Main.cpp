#include <iostream>
#include <map>
#include <sys/file.h>
#include <termios.h>
#include <signal.h>

#include "json/json.h"
#include "Logger.h"
#include "Request.h"
#include "Response.h"
#include "IHandler.h"
#include "SetPasswordHandler.h"
#include "ConfigureInterfaceHandler.h"
#include "RemoveInterfaceHandler.h"

#define BUFFER_SIZE 1048576

char RequestBuffer[BUFFER_SIZE];
map<string, IHandler *> Handlers;
bool IsRunning;

void OnInterrupt(int signal)
{
	WriteLog(LOG_INFO,string("Asked to interrupt. Exiting."));
	IsRunning=false;
}

void OnTerminate(int signal)
{
	WriteLog(LOG_INFO,string("Asked to terminate. Exiting."));
	IsRunning=false;
}

void OnHangup(int signal)
{
	WriteLog(LOG_INFO,string("Asked to hang up. Ignored."));
}

void InitializeHandlers()
{
	Handlers["setPassword"]=new SetPasswordHandler();
	Handlers["configureInterface"]=new ConfigureInterfaceHandler();
	Handlers["removeInterface"]=new RemoveInterfaceHandler();
}

void CleanupHandlers()
{
	delete Handlers["setPassword"];
	delete Handlers["configureInterface"];
	delete Handlers["removeInterface"];
}

void DoRead(int fileDescriptor, void * buffer, int count)
{
	int readBytes=0;
	int remainingBytes=count;
	while(remainingBytes>0 && IsRunning)
	{
		int count=read(fileDescriptor,((char *)buffer)+readBytes,remainingBytes);
		readBytes+=count;
		remainingBytes-=count;
	}
}

void SetSerialPort(int serialPortDescriptor)
{
	termios serialPortOption;
	tcgetattr(serialPortDescriptor,&serialPortOption);

	// Raw Mode
	serialPortOption.c_lflag&=~ICANON;
	serialPortOption.c_lflag&=~ECHO;
	serialPortOption.c_lflag&=~ECHOE;
	serialPortOption.c_lflag&=~ISIG;
	serialPortOption.c_oflag&=~OPOST;
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
	cfsetispeed(&serialPortOption,B38400);
	cfsetospeed(&serialPortOption,B38400);
	
	tcsetattr(serialPortDescriptor,TCSANOW,&serialPortOption);
}

int main(int argc, char * argv [])
{
	signal(SIGINT,OnInterrupt);
	signal(SIGTERM,OnTerminate);
	signal(SIGHUP,OnHangup);
	
	WriteLog(LOG_INFO,"BeyondCloud Agent started.");

	string serialPortPath="/dev/ttyS1";
	int serialPortDescriptor=open(serialPortPath.c_str(),O_RDWR|O_NOCTTY);

	if(serialPortDescriptor==-1)
	{
		cout<<"Cannot open "<<serialPortPath<<endl;
		return 1;
	}

	if(flock(serialPortDescriptor,LOCK_EX|LOCK_NB)<0)
	{
		cout<<"Cannot lock "<<serialPortPath<<endl;
		return 1;
	}

	SetSerialPort(serialPortDescriptor);

	WriteLog(LOG_INFO,"Initialize handlers.");
	InitializeHandlers();
	IsRunning=true;

	string info="Starting to listen on ";
	WriteLog(LOG_INFO,"Starting to listen on "+serialPortPath);
	while(IsRunning)
	{
		Request * request=NULL;
		Response * response=NULL;
		try
		{
			int requestLength;
			DoRead(serialPortDescriptor,&requestLength,sizeof(int));
			DoRead(serialPortDescriptor,RequestBuffer,requestLength);

			if(!IsRunning)
			{
				break;
			}

			Json::Reader reader;
			Json::Value value;
			reader.parse(RequestBuffer,value);
			std::string requestType=value["requestType"].asString();
			std::string requestString=string(RequestBuffer);
			WriteLog(LOG_INFO,"Get Request: Type = "+requestType);

			request=Handlers[requestType]->ParseRequest(requestString);
			response=Handlers[requestType]->Handle(request);

			int responseLength=response->GetRawResponse().size()+1;
			write(serialPortDescriptor,&responseLength,sizeof(int));
			write(serialPortDescriptor,response->GetRawResponse().c_str(),responseLength);
			WriteLog(LOG_INFO,"Send Response: Type = "+requestType);
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

	WriteLog(LOG_INFO,"Cleanup handlers.");
	CleanupHandlers();

	flock(serialPortDescriptor,LOCK_UN);
	close(serialPortDescriptor);

	WriteLog(LOG_INFO,"BeyondCloud Agent stopped.");
	return 0;
}

