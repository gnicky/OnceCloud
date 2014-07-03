#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "File.h"
#include "Process.h"

void PrintUsage()
{
	printf("Usage:\n");
	printf("Add Rule:\n\tnetsh firewall add <tcp|udp> <internal ip/netmask]> [external ip/netmask]] <port>\n");
	printf("Remove Rule:\n\tnetsh firewall remove <tcp|udp> <internal ip/netmask]> [external ip/netmask]] <port>\n");
}

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

void GenerateOutboundRule(char * buffer, const char * protocol, const char * internal, const char * external, const char * port)
{
	buffer[0]='\0';

	strcat(buffer,"-A FORWARD ");

	strcat(buffer,"-s ");
	strcat(buffer,internal);
	strcat(buffer," ");

	if(external!=NULL)
	{
		strcat(buffer,"-d ");
		strcat(buffer,external);
		strcat(buffer," ");
	}

	strcat(buffer,"-p ");
	strcat(buffer,protocol);
	strcat(buffer," ");

	strcat(buffer,"-m ");
	strcat(buffer,protocol);
	strcat(buffer," ");

	strcat(buffer,"--sport ");
	strcat(buffer,port);
	strcat(buffer," ");

	strcat(buffer,"-j ACCEPT");
}

void GenerateInboundRule(char * buffer, const char * protocol, const char * internal, const char * external, const char * port)
{
	buffer[0]='\0';

	strcat(buffer,"-A FORWARD ");

	if(external!=NULL)
	{
		strcat(buffer,"-s ");
		strcat(buffer,external);
		strcat(buffer," ");
	}

	strcat(buffer,"-d ");
	strcat(buffer,internal);
	strcat(buffer," ");

	strcat(buffer,"-p ");
	strcat(buffer,protocol);
	strcat(buffer," ");

	strcat(buffer,"-m ");
	strcat(buffer,protocol);
	strcat(buffer," ");

	strcat(buffer,"--dport ");
	strcat(buffer,port);
	strcat(buffer," ");

	strcat(buffer,"-j ACCEPT");
}

int AddRule(const char * protocol, const char * internal, const char * external, const char * port)
{
	char savedChar;
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);

	char * filterStart=strstr(originalConfiguration,"*filter");
	if(filterStart==NULL)
	{
		strcat(originalConfiguration,"*filter\n");
		strcat(originalConfiguration,":INPUT ACCEPT [28:1848]\n");
		strcat(originalConfiguration,":FORWARD ACCEPT [0:0]\n");
		strcat(originalConfiguration,":OUTPUT ACCEPT [15:1444]\n");
		strcat(originalConfiguration,"COMMIT\n");
		filterStart=strstr(originalConfiguration,"*filter");
	}

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
	
	char outbound[1000]={0};
	char inbound[1000]={0};
	GenerateOutboundRule(outbound,protocol,internal,external,port);
	GenerateInboundRule(inbound,protocol,internal,external,port);
	
	int outboundExist=0;
	int inboundExist=0;
	char * outboundPosition=strstr(ruleStart,outbound);
	char * inboundPosition=strstr(ruleStart,inbound);
	if(outboundPosition!=NULL)
	{
		outboundExist=1;
	}
	if(inboundPosition!=NULL)
	{
		inboundExist=1;
	}

	*ruleEnd=savedChar;

	// End of the filter chain

	if(!outboundExist)
	{
		strcat(newConfiguration,outbound);
		strcat(newConfiguration,"\n");
	}
	if(!inboundExist)
	{
		strcat(newConfiguration,inbound);
		strcat(newConfiguration,"\n");
	}

	strcat(newConfiguration,ruleEnd);

	SaveConfiguration(newConfiguration);

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int RemoveRule(const char * protocol, const char * internal, const char * external, const char * port)
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

	char outbound[1000];
	char inbound[1000];
	GenerateOutboundRule(outbound,protocol,internal,external,port);
	GenerateInboundRule(inbound,protocol,internal,external,port);

	strcat(newConfiguration,"\n");
	char * position=ruleStart+1;

	while(strstr(position,"COMMIT")!=NULL && strstr(position,"COMMIT")!=position)
	{
		if(strstr(position,outbound)==position
			|| strstr(position,inbound)==position)
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

int Activate(int count, char * values [])
{
	if(count==4 || count==5)
	{
		char * command=values[0];
		char * protocol=values[1];
		char * internal=values[2];
		char * external=NULL;
		char * port=NULL;
		if(count==4)
		{
			port=values[3];
		}
		if(count==5)
		{
			external=values[3];
			port=values[4];
		}

		if(strcmp(command,"add")==0)
		{
			return AddRule(protocol,internal,external,port);
		}
		if(strcmp(command,"remove")==0)
		{
			return RemoveRule(protocol,internal,external,port);
		}
	}
	if(count==1)
	{
		if(strcmp(values[0],"init")==0)
		{
			return InitFirewallRule();
		}
	}

	PrintUsage();
	return 1;
}
