#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include "File.h"

#include "PluginInterface.h"

#define BUFFER_SIZE 65536

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

void PrintUsage()
{
	printf("Usage:\n");
	printf("Init config:\n\tnetsh dhcp init <subnet> <netmask> <routers> <dns> <range start> <range end> <default lease time> <max lease time>\n");
	printf("Bind IP and MAC:\n\tnetsh dhcp bind <ip> <mac>\n");
	printf("Unbind IP:\n\tnetsh dhcp unbind <ip> <mac>\n");
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

		free(newFileContent);
	}
	else
	{
		// Not found
	}

	free(fileContent);
	return 0;
}

void GenerateInitialConfiguration(char * buffer, const char * subnet, const char * netmask, const char * routers, const char * dns
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
	strcat(buffer,routers);
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

int Init(const char * subnet, const char * netmask, const char * routers, const char * dns
	, const char * rangeStart, const char * rangeEnd, const char * defaultLease, const char * maxLease)
{
	char * fileContent=malloc(BUFFER_SIZE);
	GenerateInitialConfiguration(fileContent,subnet,netmask,routers,dns,rangeStart,rangeEnd,defaultLease,maxLease);
	WriteAllText(DhcpdConfigurationFileName,fileContent);
	free(fileContent);
	return 0;
}

int Activate(int count, char * values [])
{
	if(count==3)
	{
		if(strcmp(values[0],"bind")==0)
		{
			return Bind(values[1],values[2]);
		}
		if(strcmp(values[0],"unbind")==0)
		{
			return Unbind(values[1],values[2]);
		}
	}
	if(count==9)
	{
		if(strcmp(values[0],"init")==0)
		{
			return Init(values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8]);
		}
	}

	PrintUsage();
	return 1;
}
