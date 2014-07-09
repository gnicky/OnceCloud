#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

#include "File.h"
#include "Process.h"
#include "NatEntry.h"

void LoadConfiguration(char * buffer)
{
	GetOutput(buffer,"iptables-save");
}

void SaveConfiguration(char * buffer)
{
	SetInput(buffer,"iptables-restore");
	WriteAllText("/etc/sysconfig/iptables",buffer);
	GetOutput(buffer,"iptables-save");
	WriteAllText("/etc/sysconfig/iptables",buffer);
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

int AddNat(const char * internal, const char * external)
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

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int RemoveNat(const char * internal, const char * external)
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

	SaveConfiguration(newConfiguration);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int ListNatEntry(struct NatEntry * buffer, int * count)
{
	// TODO
	*count=0;
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
