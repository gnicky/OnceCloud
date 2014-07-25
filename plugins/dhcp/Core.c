#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "Type.h"
#include "File.h"
#include "DhcpEntry.h"
#include "Core.h"
#include "Logger.h"

const char * DhcpdConfigurationFileName="/etc/dhcp/dhcpd.conf";

void GenerateHostConfiguration(char * buffer, const char * ipAddress, const char * hardwareAddress)
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

void DoAddHostEntry(const char * ipAddress, const char * hardwareAddress)
{
	if(!IsFileExist(DhcpdConfigurationFileName))
	{
		WriteLog(LOG_ERR,"DHCP configuration file is not exist: %s. Exiting.",DhcpdConfigurationFileName);
		exit(1);
	}

	char * fileContent=malloc(1048576);
	memset(fileContent,0,1048576);
	ReadFile(DhcpdConfigurationFileName,fileContent);

	char temp[1000];
	sprintf(temp,"fixed-address %s",ipAddress);
	if(strstr(fileContent,temp)!=NULL)
	{
		return;
	}
	sprintf(temp,"hardware ethernet %s",hardwareAddress);
	if(strstr(fileContent,temp)!=NULL)
	{
		return;
	}

	char * position=strstr(fileContent,"\n}");
	if(position==NULL)
	{
		WriteLog(LOG_ERR,"Malformed configuration file %s. Exiting.",DhcpdConfigurationFileName);
		exit(1);
	}
	*position='\0';

	char * newFileContent=malloc(1048576);
	newFileContent[0]='\0';

	strcat(newFileContent,fileContent);
	strcat(newFileContent,"\n");

	char buffer[1000];
	GenerateHostConfiguration(buffer,ipAddress,hardwareAddress);
	strcat(newFileContent,buffer);
	strcat(newFileContent,position+1);

	WriteFile(DhcpdConfigurationFileName,newFileContent);	

	free(newFileContent);
	free(fileContent);
}

int Bind(const char * ipAddress, const char * hardwareAddress)
{
	DoAddHostEntry(ipAddress,hardwareAddress);
	system("service dhcpd restart > /dev/null");
	return 0;
}

int AddBindings(int hostsCount, struct Host * hosts)
{
	int i=0;
	for(i=0;i<hostsCount;i++)
	{
		DoAddHostEntry(hosts[i].IPAddress,hosts[i].HardwareAddress);
	}
	system("service dhcpd restart > /dev/null");
	return 0;
}

void DoRemoveHostEntry(const char * ipAddress, const char * hardwareAddress)
{
	if(!IsFileExist(DhcpdConfigurationFileName))
	{
		WriteLog(LOG_ERR,"DHCP configuration file is not exist: %s. Exiting.",DhcpdConfigurationFileName);
		exit(1);
	}

	char * fileContent=malloc(1048576);
	memset(fileContent,0,1048576);
	ReadFile(DhcpdConfigurationFileName,fileContent);

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
		char * newFileContent=malloc(1048576);
		newFileContent[0]='\0';

		*hostEntryStart='\0';
		strcat(newFileContent,fileContent);
		strcat(newFileContent,hostEntryEnd+strlen("\t}\n"));

		WriteFile(DhcpdConfigurationFileName,newFileContent);
		system("service dhcpd restart > /dev/null");

		free(newFileContent);
	}
	else
	{
		// Not found
	}

	free(fileContent);
}

int Unbind(const char * ipAddress, const char * hardwareAddress)
{
	DoRemoveHostEntry(ipAddress,hardwareAddress);
	system("service dhcpd restart > /dev/null");
	return 0;
}

int RemoveBindings(int hostsCount, struct Host * hosts)
{
	int i=0;
	for(i=0;i<hostsCount;i++)
	{
		DoRemoveHostEntry(hosts[i].IPAddress,hosts[i].HardwareAddress);
	}
	system("service dhcpd restart > /dev/null");
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
	WriteFile(DhcpdConfigurationFileName,fileContent);
	system("service dhcpd restart > /dev/null");

	free(fileContent);
	return 0;
}

int ListDhcpEntry(struct DhcpEntry * buffer, int * count)
{
	int i=0;

	char * fileContent=malloc(1048576);
	memset(fileContent,0,1048576);
	ReadFile(DhcpdConfigurationFileName,fileContent);

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
	free(fileContent);

	return 0;
}

int GenerateConfiguration(struct DhcpConfiguration * configuration)
{
	int i=0;
	char * fileContent=malloc(1048576);
	GenerateInitialConfiguration(fileContent,configuration->Subnet,configuration->Netmask,configuration->Router
		,configuration->DNS,configuration->RangeStart,configuration->RangeEnd,configuration->DefaultLease
		,configuration->MaxLease);

	char * position=strstr(fileContent,"\n}");
	*position='\0';
	strcat(fileContent,"\n");

	char temp[1000];
	for(i=0;i<configuration->HostCount;i++)
	{
		GenerateHostConfiguration(temp,configuration->Hosts[i].IPAddress,configuration->Hosts[i].HardwareAddress);
		strcat(fileContent,temp);
	}

	strcat(fileContent,"}\n");

	WriteFile(DhcpdConfigurationFileName,fileContent);

	system("service dhcpd restart > /dev/null");

	free(fileContent);
	return 0;
}

