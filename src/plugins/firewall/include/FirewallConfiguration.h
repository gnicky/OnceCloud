#ifndef _FIREWALL_CONFIGURATION_H_
#define _FIREWALL_CONFIGURATION_H_

#include "Rule.h"

#define IP_LENGTH 30

struct FirewallConfiguration
{
	int IPCount;
	char FromIPAddress[255][IP_LENGTH];
	int RuleCount;
	struct Rule Rules[255];
};

#endif
