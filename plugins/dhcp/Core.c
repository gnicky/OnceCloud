#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "Type.h"
#include "File.h"
#include "DhcpEntry.h"
#include "Core.h"

const char * DhcpdConfigurationFileName="/etc/dhcp/dhcpd.conf";

void GenerateConfiguration(char * buffer, const char * ipAddress, const char * hardwareAddress)
{
	buffer[0]='\0';

	strcat(buffer,"\thost ");
	strcat(buffer,ipAddress);
	strcat(buffer," {\n");

	strcat(buffer,"\t\thardware ethernet ");
	strcat(buffer,hardwareAddress);
	strcat(buffer,";\n");

	strcat(buffer,"\t\tfixed-address ");
	strcat(buffer,ipAddress);
	strcat(buffer,";\n");

	strcat(buffer,"\t}\n");
}

int Bind(const char * ipAddress, const char * hardwareAddress)
{
	if(!IsFileExist(DhcpdConfigurationFileName))
	{
		printf("[Error] File is not exist: %s\n",DhcpdConfigurationFileName);
		return 1;
	}

	int fileSize=(int)GetFileSize(DhcpdConfigurationFileName);
	char * fileContent=malloc(fileSize);
	ReadAllText(DhcpdConfigurationFileName,fileContent);

	char * position=strstr(fileContent,"\n}");
	if(position==NULL)
	{
		printf("[Error] Malformed configuration file.\n");
		return 1;
	}
	*position='\0';

	char * newFileContent=malloc(fileSize+1000);
	memset(newFileContent,0,fileSize+1000);
	strcat(newFileContent,fileContent);
	strcat(newFileContent,"\n");

	char buffer[1000];
	GenerateConfiguration(buffer,ipAddress,hardwareAddress);
	strcat(newFileContent,buffer);
	strcat(newFileContent,position+1);

	WriteAllText(DhcpdConfigurationFileName,newFileContent);	
	system("service dhcpd restart");

	free(newFileContent);
	free(fileContent);
	return 0;
}

int Unbind(const char * ipAddress, const char * hardwareAddress)
{
	if(!IsFileExist(DhcpdConfigurationFileName))
	{
		printf("[Error] File is not exist: %s\n",DhcpdConfigurationFileName);
		return 1;
	}

	int fileSize=(int)GetFileSize(DhcpdConfigurationFileName);
	char * fileContent=malloc(fileSize);
	ReadAllText(DhcpdConfigurationFileName,fileContent);

	char * hostEntryStart=fileContent;
	char * hostEntryEnd=NULL;
	while((hostEntryStart=strstr(hostEntryStart,"\thost "))!=NULL)
	{
		char * position=NULL;
		if((position=strstr(hostEntryStart,"hardware ethernet "))!=NULL)
		{
			char * hardwareAddressStart=position+strlen("hardware ethernet ");
			if(strstr(hardwareAddressStart,hardwareAddress)==hardwareAddressStart)
			{
				position=strstr(hardwareAddressStart,"fixed-address ");
				char * ipAddressStart=position+strlen("fixed-address ");
				if(strstr(ipAddressStart,ipAddress)==ipAddressStart)
				{
					hostEntryEnd=strstr(hostEntryStart,"\t}\n");
					break;
				}
			}
		}
		hostEntryStart=hostEntryStart+strlen("\thost ");
	}

	if(hostEntryStart!=NULL && hostEntryEnd!=NULL)
	{
		// Found
		char * newFileContent=malloc(fileSize);
		memset(newFileContent,0,fileSize);

		*hostEntryStart='\0';
		strcat(newFileContent,fileContent);
		strcat(newFileContent,hostEntryEnd+strlen("\t}\n"));

		WriteAllText(DhcpdConfigurationFileName,newFileContent);
		system("service dhcpd restart");

		free(newFileContent);
	}
	else
	{
		// Not found
	}

	free(fileContent);
	return 0;
}

void GenerateInitialConfiguration(char * buffer, const char * subnet, const char * netmask, const char * router, const char * dns
	, const char * rangeStart, const char * rangeEnd, const char * defaultLease, const char * maxLease)
{
	buffer[0]='\0';
	strcat(buffer,"ddns-update-style interim;\n");
	strcat(buffer,"ignore client-updates;\n");
	strcat(buffer,"\n");

	strcat(buffer,"subnet ");
	strcat(buffer,subnet);
	strcat(buffer," netmask ");
	strcat(buffer,netmask);
	strcat(buffer," {\n");

	strcat(buffer,"\toption routers ");
	strcat(buffer,router);
	strcat(buffer,";\n");

	strcat(buffer,"\toption subnet-mask ");
	strcat(buffer,netmask);
	strcat(buffer,";\n");

	strcat(buffer,"\toption domain-name-servers ");
	strcat(buffer,dns);
	strcat(buffer,";\n");

	strcat(buffer,"\trange dynamic-bootp ");
	strcat(buffer,rangeStart);
	strcat(buffer," ");
	strcat(buffer,rangeEnd);
	strcat(buffer,";\n");

	strcat(buffer,"\tdefault-lease-time ");
	strcat(buffer,defaultLease);
	strcat(buffer,";\n");

	strcat(buffer,"\tmax-lease-time ");
	strcat(buffer,maxLease);
	strcat(buffer,";\n");

	strcat(buffer,"}\n");
}

int InitializeConfiguration(const char * subnet, const char * netmask, const char * router, const char * dns
	, const char * rangeStart, const char * rangeEnd, const char * defaultLease, const char * maxLease)
{
	char * fileContent=malloc(BUFFER_SIZE);
	GenerateInitialConfiguration(fileContent,subnet,netmask,router,dns,rangeStart,rangeEnd,defaultLease,maxLease);
	WriteAllText(DhcpdConfigurationFileName,fileContent);
	system("service dhcpd restart");

	free(fileContent);
	return 0;
}

int ListDhcpEntry(struct DhcpEntry * buffer, int * count)
{
	int i=0;

	int fileSize=(int)GetFileSize(DhcpdConfigurationFileName);
	char * fileContent=malloc(fileSize);
	ReadAllText(DhcpdConfigurationFileName,fileContent);

	char * hostEntryStart=fileContent;
	while((hostEntryStart=strstr(hostEntryStart,"\thost "))!=NULL)
	{
		char * position=NULL;
		if((position=strstr(hostEntryStart,"hardware ethernet "))!=NULL)
		{
			char * hardwareAddressStart=position+strlen("hardware ethernet ");
			char * hardwareAddressEnd=strstr(hardwareAddressStart,";");
			*hardwareAddressEnd='\0';
			strcpy(buffer[i].HardwareAddress,hardwareAddressStart);
			*hardwareAddressEnd=';';
		}
		if((position=strstr(hostEntryStart,"fixed-address "))!=NULL)
		{
			char * ipAddressStart=position+strlen("fixed-address ");
			char * ipAddressEnd=strstr(ipAddressStart,";");
			*ipAddressEnd='\0';
			strcpy(buffer[i].IPAddress,ipAddressStart);
			*ipAddressEnd=';';
		}
		i++;
		hostEntryStart=hostEntryStart+strlen("\thost ");
	}
	*count=i;

	return 0;
}
