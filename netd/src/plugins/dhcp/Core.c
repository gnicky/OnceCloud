#include <string.h>
#include <stdio.h>

#include "DhcpConfiguration.h"
#include "Type.h"
#include "Logger.h"

int FindHost(struct DhcpConfiguration * configuration, const char * hardwareAddress)
{
	int position=0;
	for(position=0;position<configuration->HostConfigurationCount;position++)
	{
		if(strcmp(configuration->HostConfiguration[position].HardwareAddress,hardwareAddress)==0)
		{
			WriteLog(LOG_NOTICE,"DHCP.FindHost: mac=%s, position=%d",hardwareAddress,position);
			return position;
		}
	}
	WriteLog(LOG_NOTICE,"DHCP.FindHost: mac=%s, not found",hardwareAddress);
	return -1;
}

int IsIPAddressExist(struct DhcpConfiguration * configuration, const char * ipAddress)
{
	int position=0;
	for(position=0;position<configuration->HostConfigurationCount;position++)
	{
		if(strcmp(configuration->HostConfiguration[position].IPAddress,ipAddress)==0)
		{
			return TRUE;
		}
	}
	return FALSE;
}

int FindSubnet(struct DhcpConfiguration * configuration, const char * subnetAddress, const char * netmask)
{
	int position=0;
	for(position=0;position<configuration->SubnetConfigurationCount;position++)
	{
		if(strcmp(configuration->SubnetConfiguration[position].SubnetAddress,subnetAddress)==0
			&& strcmp(configuration->SubnetConfiguration[position].Netmask,netmask)==0)
		{
			WriteLog(LOG_NOTICE,"DHCP.FindSubnet: subnet=%s, mac=%s, position=%d",subnetAddress,netmask,position);
			return position;
		}
	}
	WriteLog(LOG_NOTICE,"DHCP.FindSubnet: subnet=%s, mac=%s, not found",subnetAddress,netmask,position);
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
		WriteLog(LOG_NOTICE,"DHCP.AddOrUpdateSubnet: Add new subnet");
		position=configuration->SubnetConfigurationCount;
		configuration->SubnetConfigurationCount++;
	}

	WriteLog(LOG_NOTICE,"DHCP.AddOrUpdateSubnet: Updating existing subnet");
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
		WriteLog(LOG_NOTICE,"DHCP.RemoveSubnet: Removing subnet");
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

	WriteLog(LOG_NOTICE,"DHCP.RemoveSubnet: Cannot find subnet.");
	return TRUE;
}

int AddOrUpdateHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * ipAddress)
{
	int position=FindHost(configuration,hardwareAddress);
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

int AssignIPAddressForHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * subnetAddress, char * assignedIPAddress)
{
	int assignStart=2;
	int assignEnd=254;

	char subnet[100];
	char temp[100];
	strcpy(subnet,subnetAddress);
	char * position=strstr(subnet,".");
	position=strstr(position+1,".");
	position=strstr(position+1,".");
	*position='\0';

	int i=0;
	for(i=assignStart;i<=assignEnd;i++)
	{
		sprintf(temp,"%s.%d",subnet,i);
		if(IsIPAddressExist(configuration,temp)==FALSE)
		{
			strcpy(assignedIPAddress,temp);
			return AddOrUpdateHost(configuration,hardwareAddress,temp);
		}
	}
	assignedIPAddress[0]='\0';
	return FALSE;
}

int RemoveHost(struct DhcpConfiguration * configuration, const char * hardwareAddress)
{
	int position=FindHost(configuration,hardwareAddress);
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

