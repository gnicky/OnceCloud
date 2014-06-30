#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include "File.h"

int Activate(int count, char * values [])
{
	printf("DHCP Plugin\n");
	printf("Count: %d\n",count);
	printf("Values:\n");
	int i=0;
	for(i=0;i<count;i++)
	{
		printf("Argument %d: %s\n",i+1,values[i]);
	}

	unsigned long fileSize=GetFileSize("/etc/dhcp/dhcpd.conf");
	printf("File Size: %lu byte(s)\n",fileSize);
	char * fileContent=malloc((unsigned int)fileSize+1000);
	memset(fileContent,0,fileSize);

	ReadAllText("/etc/dhcp/dhcpd.conf",fileContent);
	printf("File Content:\n");
	for(i=0;i<fileSize;i++)
	{
		printf("%c",fileContent[i]);
	}

	strcat(fileContent,"This is a test.\n");

	WriteAllText("/etc/dhcp/dhcpd.conf",fileContent);

	fileSize=GetFileSize("/etc/dhcp/dhcpd.conf");
	printf("New File Size: %lu byte(s)\n",fileSize);
	ReadAllText("/etc/dhcp/dhcpd.conf",fileContent);
	printf("New File Content:\n");
	for(i=0;i<fileSize;i++)
	{
		printf("%c",fileContent[i]);
	}

	free(fileContent);

	return 0;
}
