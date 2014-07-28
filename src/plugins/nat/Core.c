#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

#include "File.h"
#include "Process.h"
#include "NatEntry.h"

void LoadConfiguration(char * buffer)
{
	GetProcessOutput(buffer,"iptables-save");
}

void SaveConfiguration(char * buffer)
{
	SetProcessInput(buffer,"iptables-restore");
	WriteFile("/etc/sysconfig/iptables",buffer);
	GetProcessOutput(buffer,"iptables-save");
	WriteFile("/etc/sysconfig/iptables",buffer);
}

void GeneratePreRoutingRule(char * buffer, const char * internal, const char * external)
{
	buffer[0]='\0';
	strcat(buffer,"-A PREROUTING -d ");
	strcat(buffer,external);
	strcat(buffer,"/32 -j DNAT --to-destination ");
	strcat(buffer,internal);
}

void GeneratePostRoutingRule(char * buffer, const char * internal, const char * external)
{
	buffer[0]='\0';
	strcat(buffer,"-A POSTROUTING -s ");
	strcat(buffer,internal);
	strcat(buffer,"/32 -j SNAT --to-source ");
	strcat(buffer,external);
}

void GenerateDefaultConfiguration(char * buffer)
{
	buffer[0]='\0';
	strcat(buffer,"*nat\n");
	strcat(buffer,":PREROUTING ACCEPT [0:0]\n");
	strcat(buffer,":POSTROUTING ACCEPT [0:0]\n");
	strcat(buffer,":OUTPUT ACCEPT [0:0]\n");
	strcat(buffer,"COMMIT\n");
}

void RemoveIPAddress(const char * interface, const char * address)
{
	int i=0;
	char fileName[1000];
	for(i=0;i<255;i++)
	{	
		sprintf(fileName,"/etc/sysconfig/network-scripts/ifcfg-%s:%d",interface,i);
		if(!IsFileExist(fileName))
		{
			continue;
		}
		int fileSize=GetFileSize(fileName);
		char * fileContent=malloc(fileSize+1);
		ReadFile(fileName,fileContent);
		fileContent[fileSize]='\0';
		char keyword[1000];
		sprintf(keyword,"IPADDR=\"%s\"",address);
		char * position=strstr(fileContent,keyword);
		if(position!=NULL)
		{
			char temp[100]={0};
			sprintf(temp,"ifdown %s:%d",interface,i);
			system(temp);
			free(fileContent);
			RemoveFile(fileName);
			return;
		}
		free(fileContent);
	}
}

void AddIPAddress(const char * interface, const char * address)
{
	int i=0;
	char fileName[1000];
	for(i=0;i<255;i++)
	{
		sprintf(fileName,"/etc/sysconfig/network-scripts/ifcfg-%s:%d",interface,i);
		if(!IsFileExist(fileName))
		{
			break;
		}
	}

	printf("FileName: %s\n",fileName);

	char temp[1000]={0};
	char fileContent[1000]={0};
	sprintf(temp,"DEVICE=\"%s:%d\"\n",interface,i);
	strcat(fileContent,temp);
	sprintf(temp,"BOOTPROTO=\"static\"\n");
	strcat(fileContent,temp);
	sprintf(temp,"IPADDR=\"%s\"\n",address);
	strcat(fileContent,temp);
	sprintf(temp,"NETMASK=\"255.255.255.255\"\n");
	strcat(fileContent,temp);
	sprintf(temp,"ONBOOT=\"yes\"\n");
	strcat(fileContent,temp);

	WriteFile(fileName,fileContent);

	sprintf(temp,"ifup %s:%d",interface,i);
	system(temp);
}

int AddNat(const char * internal, const char * external, const char * interface)
{
	char savedChar;
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);
	char * natStart=strstr(originalConfiguration,"*nat");

	if(natStart==NULL)
	{
		char * defaultConfiguration=malloc(1000);
		GenerateDefaultConfiguration(defaultConfiguration);
		strcat(originalConfiguration,defaultConfiguration);
		free(defaultConfiguration);
		natStart=strstr(originalConfiguration,"*nat");
	}

	savedChar=*natStart;
	*natStart='\0';
	strcat(newConfiguration,originalConfiguration);
	*natStart=savedChar;

	char * ruleStart=strstr(natStart,":OUTPUT");
	ruleStart=strstr(ruleStart,"\n");
	ruleStart=ruleStart+strlen("\n");

	savedChar=*ruleStart;
	*ruleStart='\0';
	strcat(newConfiguration,natStart);
	*ruleStart=savedChar;

	// Start of the rule

	char * ruleEnd=strstr(ruleStart,"COMMIT");
	savedChar=*ruleEnd;
	*ruleEnd='\0';
	strcat(newConfiguration,ruleStart);

	int exist=0;
	char * internalExist=strstr(ruleStart,internal);
	char * externalExist=strstr(ruleStart,external);
	if(internalExist!=NULL || externalExist!=NULL)
	{
		exist=1;
	}

	*ruleEnd=savedChar;

	// End of the rule

	if(!exist)
	{
		char preRoutingRule[1000]={0};
		char postRoutingRule[1000]={0};
		GeneratePreRoutingRule(preRoutingRule,internal,external);
		GeneratePostRoutingRule(postRoutingRule,internal,external);
		strcat(newConfiguration,preRoutingRule);
		strcat(newConfiguration,"\n");
		strcat(newConfiguration,postRoutingRule);
		strcat(newConfiguration,"\n");
	}

	strcat(newConfiguration,ruleEnd);

	SaveConfiguration(newConfiguration);

	RemoveIPAddress(interface,external);
	AddIPAddress(interface,external);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int RemoveNat(const char * internal, const char * external, const char * interface)
{
	char savedChar;
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);
	char * natStart=strstr(originalConfiguration,"*nat");

	if(natStart==NULL)
	{
		free(newConfiguration);
		free(originalConfiguration);
		return 0;
	}

	savedChar=*natStart;
	*natStart='\0';
	strcat(newConfiguration,originalConfiguration);
	*natStart=savedChar;

	char * ruleStart=strstr(natStart,":OUTPUT");
	ruleStart=strstr(ruleStart,"\n");
	savedChar=*ruleStart;
	*ruleStart='\0';
	strcat(newConfiguration,natStart);
	*ruleStart=savedChar;

	// From the start of the rule

	char preRoutingRule[1000]={0};
	char postRoutingRule[1000]={0};
	GeneratePreRoutingRule(preRoutingRule,internal,external);
	GeneratePostRoutingRule(postRoutingRule,internal,external);

	strcat(newConfiguration,"\n");
	char * position=ruleStart+1;

	while(strstr(position,"COMMIT")!=NULL && strstr(position,"COMMIT")!=position)
	{
		if(strstr(position,preRoutingRule)==position
			|| strstr(position,postRoutingRule)==position)
		{
			position=strstr(position,"\n");
			position=position+strlen("\n");
		}
		else
		{
			char * lineEnd=strstr(position,"\n");
			*lineEnd='\0';
			strcat(newConfiguration,position);
			strcat(newConfiguration,"\n");
			*lineEnd='\n';
			position=lineEnd;
			position=position+strlen("\n");
		}
	}

	char * ruleEnd=position;
	strcat(newConfiguration,ruleEnd);

	RemoveIPAddress(interface,external);
	SaveConfiguration(newConfiguration);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int ListNatEntry(struct NatEntry * buffer, int * count)
{
	char * configuration=malloc(1048576);
	LoadConfiguration(configuration);

	int i=0;
	char * natStart=strstr(configuration,"*nat");
	if(natStart!=NULL)
	{
		char * natEnd=strstr(configuration,"COMMIT\n");
		*natEnd='\0';
		char * position=NULL;
		while((position=strstr(natStart,"-A POSTROUTING -s "))!=NULL)
		{
			char internal[30];
			char external[30];
			char * lineEnd=strstr(natStart,"\n");
			*lineEnd='\0';
			sscanf(position,"-A POSTROUTING -s %s -j SNAT --to-source %s",internal,external);
			char * stop=strstr(internal,"/");
			*stop='\0';
			strcpy(buffer[i].InternalIPAddress,internal);
			strcpy(buffer[i].ExternalIPAddress,external);
			*lineEnd='\n';
			i++;
			natStart=strstr(position,"\n")+1;
		}
	}
	*count=i;
	free(configuration);
	return 0;
}

int InitializeNat()
{
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	char * defaultConfiguration=malloc(1000);
	GenerateDefaultConfiguration(defaultConfiguration);
	
	LoadConfiguration(originalConfiguration);
	char * natStart=strstr(originalConfiguration,"*nat");
	if(natStart==NULL)
	{
		strcat(newConfiguration,originalConfiguration);
		strcat(newConfiguration,defaultConfiguration);
	}
	else
	{
		char * natEnd=strstr(natStart,"COMMIT\n");
		*natStart='\0';
		strcat(newConfiguration,originalConfiguration);
		strcat(newConfiguration,defaultConfiguration);
		strcat(newConfiguration,natEnd+strlen("COMMIT\n"));
	}

	SaveConfiguration(newConfiguration);

	free(originalConfiguration);
	free(newConfiguration);
	free(defaultConfiguration);

	return 0;
}
