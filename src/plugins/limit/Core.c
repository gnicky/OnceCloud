#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "Process.h"
#include "Logger.h"
#include "File.h"

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

int DoAddRule(const char * rule)
{
	char savedChar;
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);

	char * filterStart=strstr(originalConfiguration,"*filter");

	savedChar=*filterStart;
	*filterStart='\0';
	strcat(newConfiguration,originalConfiguration);
	*filterStart=savedChar;

	char * ruleStart=strstr(filterStart,":OUTPUT");
	ruleStart=strstr(ruleStart,"\n");
	ruleStart=ruleStart+strlen("\n");

	savedChar=*ruleStart;
	*ruleStart='\0';
	strcat(newConfiguration,filterStart);
	*ruleStart=savedChar;

	// Start of the filter chain
	
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

	// End of the filter chain

	if(!exist)
	{
		strcat(newConfiguration,rule);
		strcat(newConfiguration,"\n");
	}

	strcat(newConfiguration,ruleEnd);
	SaveConfiguration(newConfiguration);

	WriteLog(LOG_NOTICE,"Limit.DoAddRule: Rule added: \"%s\"",rule);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int DoRemoveRule(const char * rule)
{
	char savedChar;
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);
	char * filterStart=strstr(originalConfiguration,"*filter");

	if(filterStart==NULL)
	{
		free(newConfiguration);
		free(originalConfiguration);
		return 0;
	}

	savedChar=*filterStart;
	*filterStart='\0';
	strcat(newConfiguration,originalConfiguration);
	*filterStart=savedChar;

	char * ruleStart=strstr(filterStart,":OUTPUT");
	ruleStart=strstr(ruleStart,"\n");
	savedChar=*ruleStart;
	*ruleStart='\0';
	strcat(newConfiguration,filterStart);
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

	WriteLog(LOG_NOTICE,"Limit.DoRemoveRule: Rule removed: \"%s\"",rule);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

void DoRemoveLimit(const char * ip)
{
	char temp[1000];
	sprintf(temp,"-A FORWARD -s %s/32 -m limit --limit",ip);
	DoRemoveRule(temp);
	sprintf(temp,"-A FORWARD -d %s/32 -m limit --limit",ip);
	DoRemoveRule(temp);
	sprintf(temp,"-A FORWARD -s %s/32 -j",ip);
	DoRemoveRule(temp);
	sprintf(temp,"-A FORWARD -d %s/32 -j",ip);
	DoRemoveRule(temp);
}

void SetLimit(const char * ip, const char * speed)
{
	DoRemoveLimit(ip);

	int convertedSpeed;
	sscanf(speed,"%d",&convertedSpeed);
	convertedSpeed=convertedSpeed*175;
	char temp[1000];
	sprintf(temp,"-A FORWARD -s %s/32 -m limit --limit %d/sec -j RULE",ip,convertedSpeed);
	DoAddRule(temp);
	sprintf(temp,"-A FORWARD -d %s/32 -m limit --limit %d/sec -j RULE",ip,convertedSpeed);
	DoAddRule(temp);
	sprintf(temp,"-A FORWARD -s %s/32 -j DROP",ip);
	DoAddRule(temp);
	sprintf(temp,"-A FORWARD -d %s/32 -j DROP",ip);
	DoAddRule(temp);
}


