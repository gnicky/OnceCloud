#ifndef _DHCP_CONFIGURATION_H_
#define _DHCP_CONFIGURATION_H_

#include "Parameter.h"
#include "SubnetConfiguration.h"

struct DhcpConfiguration
{
	struct Parameter GlobalConfiguration[10];
	int GlobalConfigurationCount;
	struct SubnetConfiguration SubnetConfiguration[10];
	int SubnetConfigurationCount;
};

#endif
