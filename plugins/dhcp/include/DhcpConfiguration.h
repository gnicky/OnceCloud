#ifndef _DHCP_CONFIGURATION_H_
#define _DHCP_CONFIGURATION_H_

#include "Host.h"

struct DhcpConfiguration
{
	char Subnet[20];
	char Netmask[20];
	char Router[20];
	char DNS[20];
	char RangeStart[20];
	char RangeEnd[20];
	char DefaultLease[20];
	char MaxLease[20];

	int HostCount;
	struct Host Hosts[255];
};

#endif
