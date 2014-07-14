#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "File.h"
#include "Process.h"
#include "FirewallRule.h"
#include "Core.h"
#include "FirewallConfiguration.h"

int DoAddRule(const char * rule);
int DoRemoveRule(const char * rule);
void GenerateDefaultConfiguration(char * buffer);

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
	if(strstr(internal,"/")==NULL)
	{
		strcat(buffer,"/32");
	}
	strcat(buffer," ");
	
	if(external!=NULL)
	{
		strcat(buffer,"-d ");
		strcat(buffer,external);
		if(strstr(external,"/")==NULL)
		{
			strcat(buffer,"/32");
		}
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
		if(strstr(external,"/")==NULL)
		{
			strcat(buffer,"/32");
		}
		strcat(buffer," ");
	}

	strcat(buffer,"-d ");
	strcat(buffer,internal);
	if(strstr(internal,"/")==NULL)
	{
		strcat(buffer,"/32");
	}
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

void GenerateInboundPingRule(char * buffer, const char * target, const char * from)
{
	buffer[0]='\0';

	strcat(buffer,"-A FORWARD ");

	if(from!=NULL)
	{
		strcat(buffer,"-s ");
		strcat(buffer,from);
		strcat(buffer," ");
	}

	strcat(buffer,"-d ");
	strcat(buffer,target);
	strcat(buffer," ");

	// echo request
	strcat(buffer,"-p icmp -m icmp --icmp-type 8 ");
	strcat(buffer,"-j ACCEPT");
}

void GenerateOutboundPingRule(char * buffer, const char * target, const char * from)
{
	buffer[0]='\0';

	strcat(buffer,"-A FORWARD ");

	strcat(buffer,"-s ");
	strcat(buffer,target);
	strcat(buffer," ");

	if(from!=NULL)
	{
		strcat(buffer,"-d ");
		strcat(buffer,from);
		strcat(buffer," ");
	}

	// echo reply
	strcat(buffer,"-p icmp -m icmp --icmp-type 0 ");
	strcat(buffer,"-j ACCEPT");
}

int AllowPing(const char * target, const char * from)
{
	char outbound[1000]={0};
	char inbound[1000]={0};

	GenerateOutboundPingRule(outbound,target,from);
	GenerateInboundPingRule(inbound,target,from);

	DoAddRule(outbound);
	DoAddRule(inbound);

	return 0;
}

int DenyPing(const char * target, const char * from)
{
	char outbound[1000]={0};
	char inbound[1000]={0};

	GenerateOutboundPingRule(outbound,target,from);
	GenerateInboundPingRule(inbound,target,from);

	DoRemoveRule(outbound);
	DoRemoveRule(inbound);

	return 0;
}

int AddRule(const char * protocol, const char * internal, const char * external, const char * port)
{
	char outbound[1000]={0};
	char inbound[1000]={0};

	GenerateOutboundRule(outbound,protocol,internal,external,port);
	GenerateInboundRule(inbound,protocol,internal,external,port);

	DoAddRule(outbound);
	DoAddRule(inbound);	

	return 0;
}

int DoAddRule(const char * rule)
{
	char savedChar;
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);

	char * filterStart=strstr(originalConfiguration,"*filter");

	if(filterStart==NULL)
	{
		char * defaultConfiguration=malloc(1000);
		GenerateDefaultConfiguration(defaultConfiguration);
		strcat(originalConfiguration,defaultConfiguration);
		free(defaultConfiguration);
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

	free(newConfiguration);
	free(originalConfiguration);
	return 0;
}

int RemoveRule(const char * protocol, const char * internal, const char * external, const char * port)
{
	char outbound[1000]={0};
	char inbound[1000]={0};

	GenerateOutboundRule(outbound,protocol,internal,external,port);
	GenerateInboundRule(inbound,protocol,internal,external,port);

	DoRemoveRule(outbound);
	DoRemoveRule(inbound);	

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
		if(strstr(position,rule)==position)
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

void GenerateDefaultConfiguration(char * buffer)
{
	buffer[0]='\0';

	// Initialize "filter" table - DEFAULT ALL DROP
	strcat(buffer,"*filter\n");
	strcat(buffer,":INPUT DROP [0:0]\n");
	strcat(buffer,":FORWARD DROP [0:0]\n");
	strcat(buffer,":OUTPUT DROP [0:0]\n");

	// Rules
	// Allow Local Loopback
	strcat(buffer,"-A INPUT -i lo -j ACCEPT\n");
	strcat(buffer,"-A OUTPUT -o lo -j ACCEPT\n");

	// Allow Outbound Ping
	strcat(buffer,"-A OUTPUT -p icmp --icmp-type echo-request -j ACCEPT\n");
	strcat(buffer,"-A INPUT -p icmp --icmp-type echo-reply -j ACCEPT\n");

	// Allow Inbound Ping
	strcat(buffer,"-A INPUT -p icmp --icmp-type echo-request -j ACCEPT\n");	
	strcat(buffer,"-A OUTPUT -p icmp --icmp-type echo-reply -j ACCEPT\n");

	// Allow SSH Connection
	strcat(buffer,"-A INPUT -p tcp -m tcp --dport 22 -j ACCEPT\n");

	// Allow DNS
	strcat(buffer,"-A INPUT -p udp --sport 53 -j ACCEPT\n");
	strcat(buffer,"-A INPUT -p tcp --sport 53 -j ACCEPT\n");
	strcat(buffer,"-A OUTPUT -p udp --dport 53 -j ACCEPT\n");
	strcat(buffer,"-A OUTPUT -p tcp --dport 53 -j ACCEPT\n");

	// Allow Netd
	strcat(buffer,"-A INPUT -p tcp -m tcp --dport 9090 -j ACCEPT\n");

	// Allow Established Connection
	strcat(buffer,"-A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT\n");
	strcat(buffer,"-A FORWARD -m state --state RELATED,ESTABLISHED -j ACCEPT\n");
	strcat(buffer,"-A OUTPUT -m state --state RELATED,ESTABLISHED -j ACCEPT\n");
	strcat(buffer,"COMMIT\n");
}

int InitializeFirewall()
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

	char * defaultConfiguration=malloc(1000);
	GenerateDefaultConfiguration(defaultConfiguration);
	strcat(initialConfiguration,defaultConfiguration);

	SaveConfiguration(initialConfiguration);

	free(defaultConfiguration);
	free(originalConfiguration);
	free(initialConfiguration);
	return 0;
}

int ListPingRule(struct PingRule * buffer, int * count)
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
				if(strstr(position,"--icmp-type 8")!=NULL)
				{
					char targetIPRange[30];
					char fromIPRange[30];

					if(strstr(position,"-s ")!=NULL)
					{
						sscanf(strstr(position,"-s "),"-s %s ",fromIPRange);
					}
					else
					{
						fromIPRange[0]='\0';
					}

					sscanf(strstr(position,"-d "),"-d %s ",targetIPRange);
					
					strcpy(buffer[i].TargetIPRange,targetIPRange);
					strcpy(buffer[i].FromIPRange,fromIPRange);

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
						strcpy(externalIPRange,"0.0.0.0/0");
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

int SetFirewallRules(struct FirewallConfiguration * configuration)
{
	char * originalConfiguration=malloc(1048576);
	char * newConfiguration=malloc(1048576);
	newConfiguration[0]='\0';

	LoadConfiguration(originalConfiguration);

	char * filterStart=strstr(originalConfiguration,"*filter");
	if(filterStart==NULL)
	{
		strcat(newConfiguration,originalConfiguration);
	}
	else
	{
		*filterStart='\0';
		strcat(newConfiguration,originalConfiguration);
		char * filterEnd=strstr(filterStart+1,"COMMIT\n")+strlen("COMMIT\n");
		strcat(newConfiguration,filterEnd);
	}

	char * defaultConfiguration=malloc(1000);
	GenerateDefaultConfiguration(defaultConfiguration);
	char * rulesEnd=strstr(defaultConfiguration,"COMMIT\n");
	*rulesEnd='\0';
	strcat(newConfiguration,defaultConfiguration);

	int i=0;
	int j=0;
	for(i=0;i<configuration->RuleCount;i++)
	{
		char outbound[1000];
		char inbound[1000];

		char * protocol=configuration->Rules[i].Protocol;
		char * internal=configuration->FromIPAddress;
		char * external=configuration->Rules[i].ToIPAddress;
		if(strlen(external)==0)
		{
			external=NULL;
		}

		if(strcmp(protocol,"icmp")==0)
		{
			GenerateOutboundPingRule(outbound,internal,external);
			GenerateInboundPingRule(inbound,internal,external);
			strcat(newConfiguration,outbound);
			strcat(newConfiguration,"\n");
			strcat(newConfiguration,inbound);
			strcat(newConfiguration,"\n");
		}
		else if(strcmp(protocol,"tcp")==0 || strcmp(protocol,"udp")==0)
		{
			for(j=configuration->Rules[i].StartPort;j<=configuration->Rules[i].EndPort;j++)
			{
				char port[100];
				sprintf(port,"%d",j);

				GenerateOutboundRule(outbound,protocol,internal,external,port);
				GenerateInboundRule(inbound,protocol,internal,external,port);
				strcat(newConfiguration,outbound);
				strcat(newConfiguration,"\n");
				strcat(newConfiguration,inbound);
				strcat(newConfiguration,"\n");
			}
		}
	}

	strcat(newConfiguration,"COMMIT\n");

	SaveConfiguration(newConfiguration);

	free(defaultConfiguration);
	free(originalConfiguration);
	free(newConfiguration);
	return 0;
}
