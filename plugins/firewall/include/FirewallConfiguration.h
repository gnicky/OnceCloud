#ifndef _FIREWALL_CONFIGURATION_H_
#define _FIREWALL_CONFIGURATION_H_

#include "Rule.h"

struct FirewallConfiguration
{
	char FromIPAddress[20];
	int RuleCount;
	struct Rule Rules[255];
};

#endif
