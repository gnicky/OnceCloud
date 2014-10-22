#ifndef _DHCP_CONFIGURATION_H_
#define _DHCP_CONFIGURATION_H_

#include "Option.h"
#include "SubnetConfiguration.h"

struct DhcpConfiguration
{
	struct Option GlobalConfiguration[10];
	int GlobalConfigurationCount;
	struct SubnetConfiguration SubnetConfiguration[10];
	int SubnetConfigurationCount;
	struct HostConfiguration HostConfiguration[1000];
	int HostConfigurationCount;
};

#endif
