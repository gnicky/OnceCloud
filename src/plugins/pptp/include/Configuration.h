#ifndef _CONFIGURATION_H_
#define _CONFIGURATION_H_

#include "VpnUser.h"

struct Configuration
{
	char NetworkAddress[50];
	int MaxConnections;
	int UserCount;
	struct VpnUser Users[50];
};

#endif
