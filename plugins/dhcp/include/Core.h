#ifndef _CORE_H_
#define _CORE_H_

#include "DhcpEntry.h"
#include "DhcpConfiguration.h"

int GenerateConfiguration(struct DhcpConfiguration * configuration);

int Bind(const char * ipAddress, const char * hardwareAddress);
int Unbind(const char * ipAddress, const char * hardwareAddress);
int InitializeConfiguration(const char * subnet, const char * netmask, const char * router, const char * dns
	, const char * rangeStart, const char * rangeEnd, const char * defaultLease, const char * maxLease);
int ListDhcpEntry(struct DhcpEntry * buffer, int * count);

#endif
