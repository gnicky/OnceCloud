#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include "File.h"

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
	printf("Bind IP and MAC:\n\tnetsh dhcp bind [IP] [MAC]\n");
	printf("Unbind IP:\n\tnetsh dhcp unbind [IP] [MAC]\n");
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

	PrintUsage();
	return 1;
}
