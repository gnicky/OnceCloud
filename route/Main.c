#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "Process.h"

void PrintUsage()
{
	printf("Usage:\n");
	printf("Add NAT:\n\tnetsh route add [internal ip] [external ip]\n");
	printf("Remove NAT:\n\tnetsh route remove [internal ip] [external ip]\n");
}

void LoadConfiguration(char * buffer)
{
	GetOutput(buffer,"iptables-save");
}

void SaveConfiguration(char * buffer)
{
	SetInput(buffer,"iptables-restore");
}

void GeneratePreRoutingRule(char * buffer, const char * internal, const char * external)
{
	buffer[0]='\0';
	strcat(buffer,"-A PREROUTING -d ");
	strcat(buffer,external);
	strcat(buffer,"/32 -j DNAT --to-destination ");
	strcat(buffer,internal);
	strcat(buffer,"\n");
}

void GeneratePostRoutingRule(char * buffer, const char * internal, const char * external)
{
	buffer[0]='\0';
	strcat(buffer,"-A POSTROUTING -s ");
	strcat(buffer,internal);
	strcat(buffer,"/32 -j SNAT --to-source ");
	strcat(buffer,external);
	strcat(buffer,"\n");
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
		strcat(originalConfiguration,"*nat\n");
		strcat(originalConfiguration,":PREROUTING ACCEPT [0:0]\n");
		strcat(originalConfiguration,":POSTROUTING ACCEPT [0:0]\n");
		strcat(originalConfiguration,":OUTPUT ACCEPT [0:0]\n");
		strcat(originalConfiguration,"COMMIT\n");
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
		strcat(newConfiguration,postRoutingRule);
	}
	
	strcat(newConfiguration,ruleEnd);

	int length=strlen(newConfiguration);
	int i=0;
	for(i=0;i<length;i++)
	{
		printf("%c",newConfiguration[i]);
	}	
	
	SaveConfiguration(newConfiguration);
	
	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int RemoveNat(const char * internal, const char * external)
{
	return 0;
}

int Activate(int count, char * values [])
{
	if(count==3)
	{
		if(strcmp(values[0],"add")==0)
		{
			return AddNat(values[1],values[2]);
		}
		if(strcmp(values[0],"remove")==0)
		{
			return RemoveNat(values[1],values[2]);
		}
	}

	PrintUsage();
	return 1;
}

