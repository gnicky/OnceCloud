#include <stdio.h>
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
	char buffer[1000];
	char outbound[1000];
	char inbound[1000];
	buffer[0]='\0';
	GenerateOutboundRule(outbound,protocol,internal,external,port);
	GenerateInboundRule(inbound,protocol,internal,external,port);
	strcat(buffer,outbound);
	strcat(buffer,"\n");
	strcat(buffer,inbound);
	strcat(buffer,"\n");

	int length=strlen(buffer);
	int i=0;
	for(i=0;i<length;i++)
	{
		printf("%c",buffer[i]);
	}
	return 0;
}

int RemoveRule(const char * protocol, const char * internal, const char * external, const char * port)
{
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
			AddRule(protocol,internal,external,port);
		}
		if(strcmp(command,"remove")==0)
		{
			RemoveRule(protocol,internal,external,port);
		}
	}

	PrintUsage();
	return 1;
}
