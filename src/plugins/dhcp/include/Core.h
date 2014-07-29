#ifndef _CORE_H_
#define _CORE_H_

#include "DhcpConfiguration.h"

void InitializeDhcpConfiguration(struct DhcpConfiguration * configuration);

int AddOrUpdateSubnet(struct DhcpConfiguration * configuration, const char * subnetAddress, const char * netmask
	, const char * routers, const char * subnetMask, const char * domainNameServers, const char * rangeStart
	, const char * rangeEnd, const char * defaultLeaseTime, const char * maxLeaseTime);
int RemoveSubnet(struct DhcpConfiguration * configuration, const char * subnetAddress, const char * netmask);

int AddOrUpdateHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * ipAddress);
int AssignIPAddressForHost(struct DhcpConfiguration * configuration, const char * hardwareAddress, const char * subnetAddress, char * assignedIPAddress);
int RemoveHost(struct DhcpConfiguration * configuration, const char * hardwareAddress);

#endif
