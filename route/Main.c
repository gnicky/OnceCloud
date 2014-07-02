#include <stdio.h>
#include <memory.h>

#include "Process.h"

void PrintUsage()
{
	printf("Usage:\n");
	printf("Add NAT:\n\tnetsh route add [internal ip] [external ip]\n");
	printf("Remove NAT:\n\tnetsh route remove [internal ip] [external ip]\n");
}

int AddNat(const char * internal, const char * external)
{
	return 0;
}

int RemoveNat(const char * internal, const char * external)
{
	return 0;
}

int Activate(int count, char * values [])
{
	char buffer[1048576];
	GetOutput(buffer,"iptables-save");
	int length=strlen(buffer);
	int i=0;

	for(i=0;i<length;i++)
	{
		printf("%c",buffer[i]);
	}

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

