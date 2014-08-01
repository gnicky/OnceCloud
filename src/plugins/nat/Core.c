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

void GeneratePortForwardingRule(char * buffer, const char * protocol, const char * internalAddress
	, const char * internalPort, const char * externalAddress, const char * externalPort)
{
	// -A PREROUTING -d 192.168.118.20/32 -p tcp -m tcp --dport 80 -j DNAT --to-destination 192.168.121.2:8080
	sprintf(buffer,"-A PREROUTING -d %s/32 -p %s -m %s --dport %s -j DNAT --to-destination %s:%s"
		,externalAddress,protocol,protocol,externalPort,internalAddress,internalPort);
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
			Execute(temp);
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
	Execute(temp);
}

int DoAddRule(const char * rule)
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

	char * ruleStart=strstr(natStart,":POSTROUTING");
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

	// Check existance here

	int exist=0;
	char * rulePosition=strstr(ruleStart,rule);
	if(rulePosition!=NULL)
	{
		exist=1;
	}

	*ruleEnd=savedChar;


	// End of the rule

	if(!exist)
	{
		strcat(newConfiguration,rule);
		strcat(newConfiguration,"\n");
	}

	strcat(newConfiguration,ruleEnd);

	SaveConfiguration(newConfiguration);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int AddNat(const char * internal, const char * external, const char * interface)
{
	char preRoutingRule[1000]={0};
	char postRoutingRule[1000]={0};

	GeneratePreRoutingRule(preRoutingRule,internal,external);
	GeneratePostRoutingRule(postRoutingRule,internal,external);

	DoAddRule(preRoutingRule);
	DoAddRule(postRoutingRule);

	RemoveIPAddress(interface,external);
	AddIPAddress(interface,external);

	return 0;
}

int DoRemoveRule(const char * rule)
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

	strcat(newConfiguration,"\n");
	char * position=ruleStart+1;

	while(strstr(position,"COMMIT")!=NULL && strstr(position,"COMMIT")!=position)
	{
		char * end=strstr(position,"\n");
		*end='\0';
		char temp[1000];
		strcpy(temp,position);
		*end='\n';
		if(strstr(temp,rule)!=NULL)
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

	SaveConfiguration(newConfiguration);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int RemoveNat(const char * internal, const char * external, const char * interface)
{
	char preRoutingRule[1000]={0};
	char postRoutingRule[1000]={0};

	GeneratePreRoutingRule(preRoutingRule,internal,external);
	GeneratePostRoutingRule(postRoutingRule,internal,external);

	DoRemoveRule(preRoutingRule);
	DoRemoveRule(postRoutingRule);

	RemoveIPAddress(interface,external);

	return 0;
}

int AddPortForwarding(const char * protocol, const char * internalAddress, const char * internalPort
	, const char * externalAddress, const char * externalPort)
{
	char portForwardingRule[1000]={0};
	GeneratePortForwardingRule(portForwardingRule,protocol,internalAddress,internalPort,externalAddress,externalPort);
	DoAddRule(portForwardingRule);
	return 0;
}

int RemovePortForwarding(const char * protocol, const char * internalAddress, const char * internalPort
	, const char * externalAddress, const char * externalPort)
{
	char portForwardingRule[1000]={0};
	GeneratePortForwardingRule(portForwardingRule,protocol,internalAddress,internalPort,externalAddress,externalPort);
	DoRemoveRule(portForwardingRule);
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
