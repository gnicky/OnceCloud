#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <memory.h>
#include <regex.h>

#define TRUE 1
#define FALSE 0

int GetFileSize(const char * fileName)
{
	struct stat fileStatus;
	if(stat(fileName,&fileStatus)<0)
	{
 		return -1;
	}
	return (int)fileStatus.st_size;
}

int ReadFile(const char * fileName, char * fileContent)
{
	int expectedReadCount;
	int actualReadCount;
	FILE * file=NULL;

	file=fopen(fileName,"r");
	if(file==NULL)
	{
		return FALSE;
	}
		
	expectedReadCount=GetFileSize(fileName);
	actualReadCount=fread(fileContent,sizeof(char),expectedReadCount,file);
	if(actualReadCount!=expectedReadCount)
	{
		return FALSE;
	}

	fclose(file);
	return TRUE;
}

struct Parameter;
struct SubnetConfiguration;
struct DhcpConfiguration;

struct Parameter
{
	char Key[50];
	char Value[50];
};

struct SubnetConfiguration
{
	char SubnetAddress[50];
	char Netmask[50];
	char Routers[50];
	char SubnetMask[50];
	char DomainNameServers[50];
};

struct DhcpConfiguration
{
	struct Parameter GlobalConfiguration[10];
	int GlobalConfigurationCount;
	struct SubnetConfiguration SubnetConfiguration[10];
	int SubnetConfigurationCount;
};

void ReadDhcpConfiguration(char * fileContent, struct DhcpConfiguration * configuration)
{
	// Read All Global Configuration
	char * subnetStart=strstr(fileContent,"subnet ");
	char * lineStart=fileContent;
	char * lineEnd=strstr(lineStart,"\n");
	int i=0;
	while(lineEnd<subnetStart)
	{
		*lineEnd='\0';
		
		int number;
		number=sscanf(lineStart,"%s %s;",configuration->GlobalConfiguration[i].Key,configuration->GlobalConfiguration[i].Value);
		printf("%s\n",configuration->GlobalConfiguration[i].Value);
		if(number==2)
		{
			i++;
		}
	
		*lineEnd='\n';
		lineStart=lineEnd+1;
		lineEnd=strstr(lineStart,"\n");	
	}
	configuration->GlobalConfigurationCount=i;

	i=0;
	char * subnetEnd=strstr(subnetStart+strlen("subnet "),"subnet ");
	while(subnetStart!=NULL)
	{
		if(subnetEnd!=NULL)
		{
			*subnetEnd='\0';
		}

		int number;
		number=sscanf(subnetStart,"subnet %s netmask %s {",configuration->SubnetConfiguration[i].SubnetAddress,configuration->SubnetConfiguration[i].Netmask);
		if(number==2)
		{
			i++;
			char * position=strstr(subnetStart,"option routers");
			printf("%s\n",position);
			sscanf(position,"option routers %s;",configuration->SubnetConfiguration[i].Routers);
			
		}
		if(subnetEnd!=NULL)
		{
			*subnetEnd='s';
		}
		subnetStart=subnetEnd;
		if(subnetStart!=NULL)
		{
			subnetEnd=strstr(subnetStart+strlen("subnet "),"subnet ");
		}
	}
	configuration->SubnetConfigurationCount=i;
}

void SaveDhcpConfiguration(struct DhcpConfiguration * configuration)
{
	char * fileContent=malloc(1048576);
	fileContent[0]='\0';

	int i=0;
	for(i=0;i<configuration->GlobalConfigurationCount;i++)
	{
		char temp[1000];
		sprintf(temp,"%s %s;\n",configuration->GlobalConfiguration[i].Key,configuration->GlobalConfiguration[i].Value);
		strcat(fileContent,temp);
	}

	printf("\n");

	for(i=0;i<configuration->SubnetConfigurationCount;i++)
	{
		char temp[1000];
		sprintf(temp,"subnet %s netmask %s {\n",configuration->SubnetConfiguration[i].SubnetAddress,configuration->SubnetConfiguration[i].Netmask);
		strcat(fileContent,temp);
		sprintf(temp,"\toption routers %s;\n",configuration->SubnetConfiguration[i].Routers);
		strcat(fileContent,temp);
		sprintf(temp,"}\n\n");
		strcat(fileContent,temp);
	}

	printf("%s",fileContent);
	free(fileContent);
}

int main(int argc, char * argv [])
{
	const char * FileName="/etc/dhcp/dhcpd.conf";
	int fileSize=GetFileSize(FileName);
	char * fileContent=malloc(fileSize+1);
	ReadFile(FileName,fileContent);
	fileContent[fileSize]='\0';

	struct DhcpConfiguration configuration;
	ReadDhcpConfiguration(fileContent,&configuration);

	int i=0;
	printf("Global Configuration:\n");
	for(i=0;i<configuration.GlobalConfigurationCount;i++)
	{
		printf("Key: %s, Value: %s\n",configuration.GlobalConfiguration[i].Key,configuration.GlobalConfiguration[i].Value);
	}

	printf("\n");

	for(i=0;i<configuration.SubnetConfigurationCount;i++)
	{
		printf("Subnet: %s Netmask: %s\n",configuration.SubnetConfiguration[i].SubnetAddress,configuration.SubnetConfiguration[i].Netmask);
	}

	SaveDhcpConfiguration(&configuration);

	return 0;
}
