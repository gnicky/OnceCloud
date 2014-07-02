#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include <Type.h>

int SetInput(char * buffer, const char * commandLine)
{
	FILE * write=NULL;

	write=popen(commandLine,"w");
	if(write==NULL)
	{
		return FALSE;
	}

	int length=strlen(buffer);
	fwrite(buffer,sizeof(char),length,write);

	pclose(write);
	return TRUE;
}

int GetOutput(char * buffer, const char * commandLine)
{
	buffer[0]='\0';
	FILE * read=NULL;
	char readBuffer[BUFFER_SIZE+1];
	int bytesRead=0;

	read=popen(commandLine,"r");
	if(read==NULL)
	{
		return FALSE;
	}

	bytesRead=fread(readBuffer,sizeof(char),BUFFER_SIZE,read);
	while(bytesRead>0)
	{
		readBuffer[bytesRead]='\0';
		strcat(buffer,readBuffer);
		bytesRead=fread(readBuffer,sizeof(char),BUFFER_SIZE,read);
	}
	pclose(read);
	return TRUE;
}
