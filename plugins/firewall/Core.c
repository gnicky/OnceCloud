#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "File.h"
#include "Process.h"
#include "FirewallRule.h"

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

int InitializeFirewallRule()
{
	char * originalConfiguration=malloc(1048576);
	char * initialConfiguration=malloc(1048576);
	initialConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);

	char * filterStart=strstr(originalConfiguration,"*filter");
	if(filterStart==NULL)
	{
		strcat(initialConfiguration,originalConfiguration);
	}
	else
	{
		*filterStart='\0';
		strcat(initialConfiguration,originalConfiguration);		
		char * filterEnd=strstr(filterStart+1,"COMMIT\n")+strlen("COMMIT\n");
		strcat(initialConfiguration,filterEnd);
	}
	
	// Initialize "filter" table - DEFAULT ALL DROP
	strcat(initialConfiguration,"*filter\n");
	strcat(initialConfiguration,":INPUT DROP [0:0]\n");
	strcat(initialConfiguration,":FORWARD DROP [0:0]\n");
	strcat(initialConfiguration,":OUTPUT DROP [0:0]\n");

	// Rules
	// Allow Local Loopback
	strcat(initialConfiguration,"-A INPUT -i lo -j ACCEPT\n");
	strcat(initialConfiguration,"-A OUTPUT -o lo -j ACCEPT\n");
	// Allow Ping
	strcat(initialConfiguration,"-A OUTPUT -p icmp --icmp-type echo-request -j ACCEPT\n");
	strcat(initialConfiguration,"-A INPUT -p icmp --icmp-type echo-reply -j ACCEPT\n");
	// Allow SSH Connection
	strcat(initialConfiguration,"-A INPUT -p tcp -m tcp --dport 22 -j ACCEPT\n");
	// Allow Netd
	strcat(initialConfiguration,"-A INPUT -p tcp -m tcp --dport 9090 -j ACCEPT\n");
	// Allow Established Connection
	strcat(initialConfiguration,"-A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT\n");
	strcat(initialConfiguration,"-A FORWARD -m state --state RELATED,ESTABLISHED -j ACCEPT\n");
	strcat(initialConfiguration,"-A OUTPUT -m state --state RELATED,ESTABLISHED -j ACCEPT\n");
	strcat(initialConfiguration,"COMMIT\n");

	SaveConfiguration(initialConfiguration);

	free(originalConfiguration);
	free(initialConfiguration);
	return 0;
}

int ListFirewallRule(struct FirewallRule * buffer, int * count)
{
	char * configuration=malloc(1048576);
	LoadConfiguration(configuration);

	int i=0;
	char * filterStart=strstr(configuration,"*filter");
	if(filterStart!=NULL)
	{
		char * filterEnd=strstr(filterStart,"COMMIT\n");
		*filterEnd='\0';
		char * ruleStart=strstr(filterStart,"-A FORWARD ");
		if(ruleStart!=NULL)
		{
			char * position=NULL;
			while((position=strstr(ruleStart,"-A FORWARD "))!=NULL)
			{
				char * lineEnd=strstr(ruleStart,"\n");
				*lineEnd='\0';
				if(strstr(position,"--sport ")!=NULL)
				{
					char protocol[10];
					char internalIPRange[30];
					char externalIPRange[30];
					char port[10];

					sscanf(strstr(position,"-s "),"-s %s ",internalIPRange);
					if(strstr(position,"-d ")!=NULL)
					{
						sscanf(strstr(position,"-d "),"-d %s ",externalIPRange);
					}
					else
					{
						externalIPRange[0]='\0';
					}
					sscanf(strstr(position,"-p "),"-p %s ",protocol);
					sscanf(strstr(position,"--sport "),"--sport %s ",port);

					strcpy(buffer[i].Protocol,protocol);
					strcpy(buffer[i].InternalIPRange,internalIPRange);
					strcpy(buffer[i].ExternalIPRange,externalIPRange);
					strcpy(buffer[i].Port,port);

					i++;
				}
				*lineEnd='\n';
				ruleStart=strstr(position,"\n")+1;
			}
		}
	}

	*count=i;
	free(configuration);
	return 0;
}
