#include <stdio.h>
#include <memory.h>
#include <stdlib.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/wait.h>

#define BUFFER_SIZE 1048576

int CloseAllSockets()
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
			if(S_ISSOCK(status.st_mode))
			{
				toClose[i++]=fd;
			}
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

int Execute(const char * commandLine)
{
	pid_t processId;
	processId=fork();
	if(processId<0)
	{
		exit(1);
	}

	if(processId==0)
	{
		CloseAllSockets();
		int status=system(commandLine);
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

bool SetProcessInput(char * buffer, const char * commandLine)
{
	FILE * write=NULL;

	write=popen(commandLine,"w");
	if(write==NULL)
	{
		return false;
	}

	int length=strlen(buffer);
	fwrite(buffer,sizeof(char),length,write);

	pclose(write);
	return true;
}

bool GetProcessOutput(char * buffer, const char * commandLine)
{
	buffer[0]='\0';
	FILE * read=NULL;
	char readBuffer[BUFFER_SIZE+1];
	int bytesRead=0;

	read=popen(commandLine,"r");
	if(read==NULL)
	{
		return false;
	}

	bytesRead=fread(readBuffer,sizeof(char),BUFFER_SIZE,read);
	while(bytesRead>0)
	{
		readBuffer[bytesRead]='\0';
		strcat(buffer,readBuffer);
		bytesRead=fread(readBuffer,sizeof(char),BUFFER_SIZE,read);
	}
	pclose(read);
	return true;
}
