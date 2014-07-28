#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "File.h"

void AddRoute(const char * interface, const char * address, const char * netmask)
{
	char fileName[1000];
	char temp[1000]={0};
	char fileContent[1000]={0};
	sprintf(fileName,"/etc/sysconfig/network-scripts/ifcfg-%s",interface);
	if(IsFileExist(fileName))
	{
		sprintf(temp,"ifdown %s",interface);
		system(temp);	
	}

	sprintf(temp,"DEVICE=\"%s\"\n",interface);
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
	system(temp);
}

void RemoveRoute(const char * interface)
{
	char fileName[1000];
	char temp[1000]={0};
	sprintf(fileName,"/etc/sysconfig/network-scripts/ifcfg-%s",interface);
	if(IsFileExist(fileName))
	{
		sprintf(temp,"ifdown %s",interface);
		system(temp);	
		RemoveFile(fileName);
		return;
	}
}

