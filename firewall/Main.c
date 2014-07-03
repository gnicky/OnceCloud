#include <stdio.h>
#include <memory.h>

#include "File.h"
#include "Process.h"

void PrintUsage()
{
	printf("Usage:\n");
	printf("Add Rule:\n\tnetsh firewall add <tcp|udp> <internal ip[/netmask]> [external ip[/netmask]] <port>\n");
	printf("Remove Rule:\n\tnetsh firewall remove <tcp|udp> <internal ip[/netmask]> [external ip[/netmask]] <port>\n");
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

void GenerateFirewallRule(char * buffer, const char * protocol, const char * internal, const char * external, const char * port)
{

}

int AddRule(const char * protocol, const char * internal, const char * external, const char * port)
{
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
