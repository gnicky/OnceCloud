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

struct SplitResult
{
	char * Delimiter;
	char ** Content;
	int Count;
};

struct SplitResult * Split(const char * source, const char * delimiter)
{
	char * temp=malloc(strlen(source)+1);
	strcpy(temp,source);

	// Get Count
	int i=0;
	char * current=temp;
	char * result=NULL;
	char * lastPosition=NULL;
	while((result=strtok_r(current,delimiter,&lastPosition))!=NULL)
	{
		i++;
		current=NULL;
	}

	struct SplitResult * ret=malloc(sizeof(struct SplitResult));
	ret->Count=i;
	ret->Delimiter=malloc(strlen(delimiter)+1);
	strcpy(ret->Delimiter,delimiter);

	// Split
	char ** content=malloc(sizeof(char *)*(ret->Count));
	i=0;
	current=temp;
	result=NULL;
	lastPosition=NULL;
	strcpy(temp,source);
	while((result=strtok_r(current,delimiter,&lastPosition))!=NULL)
	{	
		content[i]=malloc(strlen(result)+1);
		strcpy(content[i],result);
		i++;
		current=NULL;	
	}

	ret->Content=content;

	free(temp);
	return ret;
}

void FreeSplitResult(struct SplitResult * result)
{
	free(result->Delimiter);
	int i=0;
	for(i=0;i<result->Count;i++)
	{
		free(result->Content[i]);
	}
	free(result);
}

void ParseAllGlobalConfiguration(struct SplitResult * result, struct DhcpConfiguration * configuration)
{
	int status;
	int i;
	regmatch_t matchResult[3];
	regex_t regex;
	regcomp(&regex,"^(\\S*) (.*?);$",REG_EXTENDED);
	for(i=0;i<result->Count;i++)
	{
		status=regexec(&regex,result->Content[i],3,matchResult,0);
		if(status==REG_NOMATCH)
		{
			break;
		}
		else if(status==0)
		{
			memcpy(configuration->GlobalConfiguration[i].Key
				,result->Content[i]+matchResult[1].rm_so,matchResult[1].rm_eo-matchResult[1].rm_so);
			memcpy(configuration->GlobalConfiguration[i].Value
				,result->Content[i]+matchResult[2].rm_so,matchResult[2].rm_eo-matchResult[2].rm_so);
			configuration->GlobalConfiguration[i].Key[matchResult[1].rm_eo-matchResult[1].rm_so]='\0';
			configuration->GlobalConfiguration[i].Value[matchResult[2].rm_eo-matchResult[2].rm_so]='\0';
		}
	}
	configuration->GlobalConfigurationCount=i;
}

void ParseAllSubnetConfiguration(struct SplitResult * result, struct DhcpConfiguration * configuration)
{
	int status;
	int i;
	regmatch_t subnetDefinition[3];
	regmatch_t optionMatch[3];
	regex_t subnetDefinitionPattern;
	regex_t optionPattern;
	regcomp(&subnetDefinitionPattern,"^subnet (\\S*?) netmask (\\S*?) \\{$",REG_EXTENDED);
	regcomp(&optionPattern,"^\toption (\\S*?) (.*);",REG_EXTENDED);
	int j=0;
	int k;
	for(i=0;i<result->Count;)
	{
		// Parse Subnet definition
		status=regexec(&subnetDefinitionPattern,result->Content[i],3,subnetDefinition,0);
		if(status==REG_NOMATCH)
		{
			i++;
			continue;
		}
		else if(status==0)
		{
			memcpy(configuration->SubnetConfiguration[j].SubnetAddress
				,result->Content[i]+subnetDefinition[1].rm_so,subnetDefinition[1].rm_eo-subnetDefinition[1].rm_so);
			memcpy(configuration->SubnetConfiguration[j].Netmask
				,result->Content[i]+subnetDefinition[2].rm_so,subnetDefinition[2].rm_eo-subnetDefinition[2].rm_so);
			configuration->SubnetConfiguration[j].SubnetAddress[subnetDefinition[1].rm_eo-subnetDefinition[1].rm_so]='\0';
			configuration->SubnetConfiguration[j].Netmask[subnetDefinition[2].rm_eo-subnetDefinition[2].rm_so]='\0';
			i++;
			// Parse options
			while(1)
			{
				status=regexec(&optionPattern,result->Content[i],3,optionMatch,0);
				if(status==REG_NOMATCH)
				{
					i++;
					break;
				}
				else if(status==0)
				{
					char key[100]={0};
					char value[100]={0};
					memcpy(key,result->Content[i]+optionMatch[1].rm_so,optionMatch[1].rm_eo-optionMatch[1].rm_so);
					memcpy(value,result->Content[i]+optionMatch[2].rm_so,optionMatch[2].rm_eo-optionMatch[2].rm_so);
					key[optionMatch[1].rm_eo-optionMatch[1].rm_so]='\0';
					value[optionMatch[2].rm_eo-optionMatch[2].rm_so]='\0';
					if(strcmp(key,"routers")==0)
					{
						strcpy(configuration->SubnetConfiguration[j].Routers,value);
					}
					else if(strcmp(key,"subnet-mask")==0)
					{
						strcpy(configuration->SubnetConfiguration[j].SubnetMask,value);
					}
					i++;
				}
			}
			j++;
		}
	}
	configuration->SubnetConfigurationCount=j;
}

void ReadDhcpConfiguration(char * fileContent, struct DhcpConfiguration * configuration)
{
	int count=0;
	int i=0;
	struct SplitResult * result=Split(fileContent,"\n");

	ParseAllGlobalConfiguration(result,configuration);
	ParseAllSubnetConfiguration(result,configuration);

	FreeSplitResult(result);
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
		sprintf(temp,"\toption subnet-mask %s;\n",configuration->SubnetConfiguration[i].SubnetMask);
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
	memset(&configuration,0,sizeof(struct DhcpConfiguration));
	ReadDhcpConfiguration(fileContent,&configuration);
	SaveDhcpConfiguration(&configuration);

	return 0;
}
