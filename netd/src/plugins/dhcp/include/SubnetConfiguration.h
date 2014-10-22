#ifndef _SUBNET_CONFIGURATION_H_
#define _SUBNET_CONFIGURATION_H_

#include "HostConfiguration.h"

struct SubnetConfiguration
{
	char SubnetAddress[50];
	char Netmask[50];
	char Routers[50];
	char SubnetMask[50];
	char DomainNameServers[50];
	char RangeStart[50];
	char RangeEnd[50];
	char DefaultLeaseTime[50];
	char MaxLeaseTime[50];
};

#endif
