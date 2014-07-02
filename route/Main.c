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

	char preRoutingRule[1000]={0};
	char postRoutingRule[1000]={0};
	
	strcat(preRoutingRule,"-A PREROUTING -d ");
	strcat(preRoutingRule,external);
	strcat(preRoutingRule,"/32 -j DNAT --to-destination ");
	strcat(preRoutingRule,internal);
	strcat(preRoutingRule,"\n");

	strcat(postRoutingRule,"-A POSTROUTING -s ");
	strcat(postRoutingRule,internal);
	strcat(postRoutingRule,"/32 -j SNAT --to-source ");
	strcat(postRoutingRule,external);
	strcat(postRoutingRule,"\n");

	if(!exist)
	{
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

