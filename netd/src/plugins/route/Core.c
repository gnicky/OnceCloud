#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Type.h"
#include "File.h"
#include "Process.h"

void FindInterface(char * interface, const char * mac)
{
	char fileName[1000];
	int i=0;
	for(i=0;i<255;i++)
	{
		sprintf(fileName,"/sys/class/net/eth%d/address",i);
		if(!IsFileExist(fileName))
		{
			continue;
		}
		int fileSize=GetFileSize(fileName);
		char * fileContent=malloc(fileSize+1);
		ReadFile(fileName,fileContent);
		fileContent[fileSize]='\0';
		char * position=strstr(fileContent,mac);
		if(position!=NULL)
		{
			sprintf(interface,"eth%d",i);
			free(fileContent);
			return;
		}
		free(fileContent);
	}
}

int FindFile(char * fileName, char * interface, const char * mac)
{
	int i=0;
	for(i=0;i<255;i++)
	{
		sprintf(fileName,"/etc/sysconfig/network-scripts/ifcfg-eth%d",i);
		if(!IsFileExist(fileName))
		{
			continue;
		}
		int fileSize=GetFileSize(fileName);
		char * fileContent=malloc(fileSize+1);
		ReadFile(fileName,fileContent);
		fileContent[fileSize]='\0';
		char * position=strstr(fileContent,mac);
		if(position!=NULL)
		{
			sprintf(interface,"eth%d",i);
			free(fileContent);
			return TRUE;
		}
		free(fileContent);
	}
	return FALSE;
}

void GetAvailableFileName(char * fileName, char * interface)
{
	int i=0;
	for(i=0;i<255;i++)
	{
		sprintf(fileName,"/etc/sysconfig/network-scripts/ifcfg-eth%d",i);
		if(!IsFileExist(fileName))
		{
			sprintf(interface,"eth%d",i);
			break;
		}
	}
}

void AddRoute(const char * mac, const char * address, const char * netmask)
{
	char fileName[1000];
	char temp[1000]={0};
	char fileContent[1000]={0};
	char interface[10];

	if(FindFile(fileName,interface,mac)!=TRUE)
	{
		GetAvailableFileName(fileName,interface);
	}
	if(IsFileExist(fileName))
	{
		sprintf(temp,"ifdown %s",interface);
		Execute(temp);
	}

	sprintf(temp,"HWADDR=\"%s\"\n",mac);
	strcat(fileContent,temp);
	sprintf(temp,"BOOTPROTO=\"static\"\n");
	strcat(fileContent,temp);
	sprintf(temp,"IPADDR=\"%s\"\n",address);
	strcat(fileContent,temp);
	sprintf(temp,"NETMASK=\"%s\"\n",netmask);
	strcat(fileContent,temp);
	sprintf(temp,"ONBOOT=\"yes\"\n");
	strcat(fileContent,temp);

	WriteFile(fileName,fileContent);

	sprintf(temp,"ifup %s",interface);
	Execute(temp);
}

void RemoveRoute(const char * mac)
{
	char fileName[1000];
	char temp[1000]={0};
	char fileInterface[10];

	if(FindFile(fileName,fileInterface,mac)!=TRUE)
	{
		return;
	}

	sprintf(temp,"ifdown %s",fileInterface);
	Execute(temp);
	RemoveFile(fileName);
}

