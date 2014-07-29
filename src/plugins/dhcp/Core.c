#include <string.h>

#include "DhcpConfiguration.h"
#include "Type.h"

int FindHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * ipAddress)
{
	int position=0;
	for(position=0;position<configuration->HostConfigurationCount;position++)
	{
		if(strcmp(configuration->HostConfiguration[position].HardwareAddress,hardwareAddress)==0
			&& strcmp(configuration->HostConfiguration[position].IPAddress,ipAddress)==0)
		{
			return position;
		}
	}
	return -1;
}

int FindSubnet(struct DhcpConfiguration * configuration, const char * subnetAddress, const char * netmask)
{
	int position=0;
	for(position=0;position<configuration->SubnetConfigurationCount;position++)
	{
		if(strcmp(configuration->SubnetConfiguration[position].SubnetAddress,subnetAddress)==0
			&& strcmp(configuration->SubnetConfiguration[position].Netmask,netmask)==0)
		{
			return position;
		}
	}
	return -1;
}

void InitializeDhcpConfiguration(struct DhcpConfiguration * configuration)
{
	memset(configuration,0,sizeof(struct DhcpConfiguration));
	configuration->GlobalConfigurationCount=2;
	configuration->SubnetConfigurationCount=0;
	configuration->HostConfigurationCount=0;
	strcpy(configuration->GlobalConfiguration[0].Key,"ddns-update-style");
	strcpy(configuration->GlobalConfiguration[0].Value,"interim");
	strcpy(configuration->GlobalConfiguration[1].Key,"ignore");
	strcpy(configuration->GlobalConfiguration[1].Value,"client-updates");
}

int AddOrUpdateSubnet(struct DhcpConfiguration * configuration, const char * subnetAddress, const char * netmask
	, const char * routers, const char * subnetMask, const char * domainNameServers, const char * rangeStart
	, const char * rangeEnd, const char * defaultLeaseTime, const char * maxLeaseTime)
{
	int position=FindSubnet(configuration,subnetAddress,netmask);
	if(position==-1)
	{
		position=configuration->SubnetConfigurationCount;
		configuration->SubnetConfigurationCount++;
	}

	strcpy(configuration->SubnetConfiguration[position].SubnetAddress,subnetAddress);
	strcpy(configuration->SubnetConfiguration[position].Netmask,netmask);
	strcpy(configuration->SubnetConfiguration[position].Routers,routers);
	strcpy(configuration->SubnetConfiguration[position].SubnetMask,subnetMask);
	strcpy(configuration->SubnetConfiguration[position].DomainNameServers,domainNameServers);
	strcpy(configuration->SubnetConfiguration[position].RangeStart,rangeStart);
	strcpy(configuration->SubnetConfiguration[position].RangeEnd,rangeEnd);
	strcpy(configuration->SubnetConfiguration[position].DefaultLeaseTime,defaultLeaseTime);
	strcpy(configuration->SubnetConfiguration[position].MaxLeaseTime,maxLeaseTime);

	return TRUE;	
}

int RemoveSubnet(struct DhcpConfiguration * configuration, const char * subnetAddress, const char * netmask)
{
	int position=FindSubnet(configuration,subnetAddress,netmask);
	int i=0;

	if(position!=-1)
	{
		for(i=position+1;i<configuration->SubnetConfigurationCount;i++)
		{
			strcpy(configuration->SubnetConfiguration[i-1].SubnetAddress,configuration->SubnetConfiguration[i].SubnetAddress);
			strcpy(configuration->SubnetConfiguration[i-1].Netmask,configuration->SubnetConfiguration[i].Netmask);
			strcpy(configuration->SubnetConfiguration[i-1].Routers,configuration->SubnetConfiguration[i].Routers);
			strcpy(configuration->SubnetConfiguration[i-1].SubnetMask,configuration->SubnetConfiguration[i].SubnetMask);
			strcpy(configuration->SubnetConfiguration[i-1].DomainNameServers,configuration->SubnetConfiguration[i].DomainNameServers);
			strcpy(configuration->SubnetConfiguration[i-1].RangeStart,configuration->SubnetConfiguration[i].RangeStart);
			strcpy(configuration->SubnetConfiguration[i-1].RangeEnd,configuration->SubnetConfiguration[i].RangeEnd);
			strcpy(configuration->SubnetConfiguration[i-1].DefaultLeaseTime,configuration->SubnetConfiguration[i].DefaultLeaseTime);
			strcpy(configuration->SubnetConfiguration[i-1].MaxLeaseTime,configuration->SubnetConfiguration[i].MaxLeaseTime);
		}
		configuration->SubnetConfigurationCount--;
	}

	return TRUE;
}

int AddOrUpdateHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * ipAddress)
{
	int position=FindHost(configuration,hardwareAddress,ipAddress);
	if(position==-1)
	{
		position=configuration->HostConfigurationCount;
		configuration->HostConfigurationCount++;
	}

	strcpy(configuration->HostConfiguration[position].Name,ipAddress);
	strcpy(configuration->HostConfiguration[position].HardwareAddress,hardwareAddress);
	strcpy(configuration->HostConfiguration[position].IPAddress,ipAddress);

	return TRUE;
}

int RemoveHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * ipAddress)
{
	int position=FindHost(configuration,hardwareAddress,ipAddress);
	int i=0;

	if(position!=-1)
	{
		for(i=position+1;i<configuration->HostConfigurationCount;i++)
		{
			strcpy(configuration->HostConfiguration[i-1].Name,configuration->HostConfiguration[i].Name);
			strcpy(configuration->HostConfiguration[i-1].HardwareAddress,configuration->HostConfiguration[i].HardwareAddress);
			strcpy(configuration->HostConfiguration[i-1].IPAddress,configuration->HostConfiguration[i].IPAddress);
		}
		configuration->HostConfigurationCount--;
	}

	return TRUE;
}

