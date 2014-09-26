#include <stdio.h>
#include <memory.h>
#include <stdlib.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <string>

#include "Process.h"

#define BUFFER_SIZE 1048576

Process::Process()
{

}

Process::~Process()
{

}

bool Process::CloseAllFiles()
{
	DIR * directory=NULL;
	struct dirent * entry=NULL;
	struct stat status;

	int toClose[1024];
	int toCloseCount=0;

	int i=0;
	directory=opendir("/dev/fd");
	while((entry=readdir(directory))!=NULL)
	{
		if(strcmp(entry->d_name,".")==0)
		{
			continue;
		}
		if(strcmp(entry->d_name,"..")==0)
		{
			continue;
		}
		int fd;
		sscanf(entry->d_name,"%d",&fd);
		if(fstat(fd,&status)==0)
		{
			toClose[i++]=fd;
		}
	}
	closedir(directory);
	toCloseCount=i;

	for(i=0;i<toCloseCount;i++)
	{
		close(toClose[i]);
	}

	return true;
}

bool Process::Execute(const std::string & commandLine)
{
	pid_t processId;
	processId=fork();
	if(processId<0)
	{
		exit(1);
	}

	if(processId==0)
	{
		Process::CloseAllFiles();
		int status=system(commandLine.c_str());
		exit(status);
	}

	int ret;
	waitpid(processId,&ret,0);
	if(ret!=0)
	{
		return false;
	}

	return true;
}

bool Process::SetInputAndExecute(const std::string & input, const std::string & commandLine)
{
	FILE * write=NULL;

	write=popen(commandLine.c_str(),"w");
	if(write==NULL)
	{
		return false;
	}

	fwrite(input.c_str(),sizeof(char),input.size(),write);

	pclose(write);
	return true;
}

std::string ExecuteAndGetOutput(const std::string & commandLine)
{
	FILE * read=NULL;
	char readBuffer[BUFFER_SIZE+1];
	int bytesRead=0;
	std::string temp="";

	read=popen(commandLine.c_str(),"r");
	if(read==NULL)
	{
		return "";
	}

	bytesRead=fread(readBuffer,sizeof(char),BUFFER_SIZE,read);
	while(bytesRead>0)
	{
		readBuffer[bytesRead]='\0';
		temp+=readBuffer;
		bytesRead=fread(readBuffer,sizeof(char),BUFFER_SIZE,read);
	}
	pclose(read);
	return temp;
}

