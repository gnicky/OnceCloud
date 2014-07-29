#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <memory.h>
#include <regex.h>

#include "File.h"
#include "String.h"

#include "Parameter.h"
#include "HostConfiguration.h"
#include "SubnetConfiguration.h"
#include "DhcpConfiguration.h"

const char * DhcpdConfigurationFileName="/etc/dhcp/dhcpd.conf";

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
	int status=0;
	int i=0;
	int j=0;
	int k=0;

	regmatch_t subnetDefinition[3];
	regex_t subnetDefinitionPattern;
	regcomp(&subnetDefinitionPattern,"^subnet (\\S*?) netmask (\\S*?) \\{$",REG_EXTENDED);

	regmatch_t optionMatch[3];
	regex_t optionPattern;
	regcomp(&optionPattern,"^\toption (\\S*?) (.*);$",REG_EXTENDED);

	regex_t rangePattern;
	regmatch_t rangeMatch[3];
	regcomp(&rangePattern,"^\trange dynamic-bootp (\\S*?) (\\S*?);$",REG_EXTENDED);

	regex_t defaultLeasePattern;
	regmatch_t defaultLeaseMatch[2];
	regcomp(&defaultLeasePattern,"^\tdefault-lease-time (\\S*?);$",REG_EXTENDED);

	regex_t maxLeasePattern;
	regmatch_t maxLeaseMatch[2];
	regcomp(&maxLeasePattern,"^\tmax-lease-time (\\S*?);$",REG_EXTENDED);

	regex_t hostNamePattern;
	regmatch_t hostNameMatch[2];
	regcomp(&hostNamePattern,"^\thost (\\S*?) \\{$",REG_EXTENDED);

	regex_t hardwareAddressPattern;
	regmatch_t hardwareAddressMatch[2];
	regcomp(&hardwareAddressPattern,"^\t\thardware ethernet (\\S*?);$",REG_EXTENDED);

	regex_t ipAddressPattern;
	regmatch_t ipAddressMatch[2];
	regcomp(&ipAddressPattern,"^\t\tfixed-address (\\S*?);$",REG_EXTENDED);

	for(;i<result->Count;)
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
					else if(strcmp(key,"domain-name-servers")==0)
					{
						strcpy(configuration->SubnetConfiguration[j].DomainNameServers,value);
					}
					i++;
				}
			}
			// Parse range
			status=regexec(&rangePattern,result->Content[i],3,rangeMatch,0);
			char rangeStart[100]={0};
			char rangeEnd[100]={0};
			memcpy(rangeStart,result->Content[i]+rangeMatch[1].rm_so,rangeMatch[1].rm_eo-rangeMatch[1].rm_so);
			memcpy(rangeEnd,result->Content[i]+rangeMatch[2].rm_so,rangeMatch[2].rm_eo-rangeMatch[2].rm_so);
			rangeStart[rangeMatch[1].rm_eo-rangeMatch[1].rm_so]='\0';
			rangeEnd[rangeMatch[2].rm_eo-rangeMatch[2].rm_so]='\0';
			strcpy(configuration->SubnetConfiguration[j].RangeStart,rangeStart);
			strcpy(configuration->SubnetConfiguration[j].RangeEnd,rangeEnd);			
			i++;
			// Parse lease time
			status=regexec(&defaultLeasePattern,result->Content[i],2,defaultLeaseMatch,0);
			char defaultLease[100]={0};
			memcpy(defaultLease,result->Content[i]+defaultLeaseMatch[1].rm_so,defaultLeaseMatch[1].rm_eo-defaultLeaseMatch[1].rm_so);
			defaultLease[defaultLeaseMatch[1].rm_eo-defaultLeaseMatch[1].rm_so]='\0';
			strcpy(configuration->SubnetConfiguration[j].DefaultLeaseTime,defaultLease);
			i++;
			status=regexec(&maxLeasePattern,result->Content[i],2,maxLeaseMatch,0);
			char maxLease[100]={0};
			memcpy(maxLease,result->Content[i]+maxLeaseMatch[1].rm_so,maxLeaseMatch[1].rm_eo-maxLeaseMatch[1].rm_so);
			maxLease[maxLeaseMatch[1].rm_eo-maxLeaseMatch[1].rm_so]='\0';
			strcpy(configuration->SubnetConfiguration[j].MaxLeaseTime,maxLease);
			i++;
			// Parse hosts
			k=0;
			while(1)
			{
				status=regexec(&hostNamePattern,result->Content[i],2,hostNameMatch,0);
				if(status==REG_NOMATCH)
				{
					break;
				}
				else if(status==0)
				{
					char hostName[100]={0};
					char hardwareAddress[100]={0};
					char ipAddress[100]={0};
					memcpy(hostName,result->Content[i]+hostNameMatch[1].rm_so,hostNameMatch[1].rm_eo-hostNameMatch[1].rm_so);
					hostName[hostNameMatch[1].rm_eo-hostNameMatch[1].rm_so]='\0';
					strcpy(configuration->SubnetConfiguration[j].Hosts[k].Name,hostName);
					i++;

					status=regexec(&hardwareAddressPattern,result->Content[i],2,hardwareAddressMatch,0);
					memcpy(hardwareAddress,result->Content[i]+hardwareAddressMatch[1].rm_so,hardwareAddressMatch[1].rm_eo-hardwareAddressMatch[1].rm_so);
					hardwareAddress[hardwareAddressMatch[1].rm_eo-hardwareAddressMatch[1].rm_so]='\0';
					strcpy(configuration->SubnetConfiguration[j].Hosts[k].HardwareAddress,hardwareAddress);
					i++;

					status=regexec(&ipAddressPattern,result->Content[i],2,ipAddressMatch,0);
					memcpy(ipAddress,result->Content[i]+ipAddressMatch[1].rm_so,ipAddressMatch[1].rm_eo-ipAddressMatch[1].rm_so);
					hardwareAddress[ipAddressMatch[1].rm_eo-ipAddressMatch[1].rm_so]='\0';
					strcpy(configuration->SubnetConfiguration[j].Hosts[k].IPAddress,ipAddress);
					i++;

					// Skip the end of host definition
					i++;

					k++;
				}
			}
			configuration->SubnetConfiguration[j].HostsCount=k;
			j++;
		}
	}
	configuration->SubnetConfigurationCount=j;
}

void ReadDhcpConfiguration(struct DhcpConfiguration * configuration)
{
	int fileSize=GetFileSize(DhcpdConfigurationFileName);
	char * fileContent=malloc(fileSize+1);
	ReadFile(DhcpdConfigurationFileName,fileContent);
	fileContent[fileSize]='\0';

	struct SplitResult * result=Split(fileContent,"\n");

	ParseAllGlobalConfiguration(result,configuration);
	ParseAllSubnetConfiguration(result,configuration);

	FreeSplitResult(result);
	free(fileContent);
}

void SaveDhcpConfiguration(struct DhcpConfiguration * configuration)
{
	char * fileContent=malloc(1048576);
	fileContent[0]='\0';

	int i=0;
	int j=0;
	char temp[1000];

	strcat(fileContent,"# Generated by Net Daemon\n");
	
	for(i=0;i<configuration->GlobalConfigurationCount;i++)
	{
		sprintf(temp,"%s %s;\n",configuration->GlobalConfiguration[i].Key,configuration->GlobalConfiguration[i].Value);
		strcat(fileContent,temp);
	}

	for(i=0;i<configuration->SubnetConfigurationCount;i++)
	{
		sprintf(temp,"subnet %s netmask %s {\n",configuration->SubnetConfiguration[i].SubnetAddress,configuration->SubnetConfiguration[i].Netmask);
		strcat(fileContent,temp);
		sprintf(temp,"\toption routers %s;\n",configuration->SubnetConfiguration[i].Routers);
		strcat(fileContent,temp);
		sprintf(temp,"\toption subnet-mask %s;\n",configuration->SubnetConfiguration[i].SubnetMask);
		strcat(fileContent,temp);
		sprintf(temp,"\toption domain-name-servers %s;\n",configuration->SubnetConfiguration[i].DomainNameServers);
		strcat(fileContent,temp);
		sprintf(temp,"\trange dynamic-bootp %s %s;\n",configuration->SubnetConfiguration[i].RangeStart,configuration->SubnetConfiguration[i].RangeEnd);
		strcat(fileContent,temp);
		sprintf(temp,"\tdefault-lease-time %s;\n",configuration->SubnetConfiguration[i].DefaultLeaseTime);
		strcat(fileContent,temp);
		sprintf(temp,"\tmax-lease-time %s;\n",configuration->SubnetConfiguration[i].MaxLeaseTime);
		strcat(fileContent,temp);
		for(j=0;j<configuration->SubnetConfiguration[i].HostsCount;j++)
		{
			sprintf(temp,"\thost %s {\n",configuration->SubnetConfiguration[i].Hosts[j].Name);
			strcat(fileContent,temp);
			sprintf(temp,"\t\thardware ethernet %s;\n",configuration->SubnetConfiguration[i].Hosts[j].HardwareAddress);
			strcat(fileContent,temp);
			sprintf(temp,"\t\tfixed-address %s;\n",configuration->SubnetConfiguration[i].Hosts[j].IPAddress);
			strcat(fileContent,temp);
			sprintf(temp,"\t}\n");
			strcat(fileContent,temp);
		}
		sprintf(temp,"}\n\n");
		strcat(fileContent,temp);
	}

	WriteFile(DhcpdConfigurationFileName,fileContent);
	system("service dhcpd restart > /dev/null");
	free(fileContent);
}

