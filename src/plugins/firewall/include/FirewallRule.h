#ifndef _FIREWALL_RULE_H_
#define _FIREWALL_RULE_H_

struct FirewallRule
{
	char Protocol[10];
	char InternalIPRange[30];
	char ExternalIPRange[30];
	char Port[10];
};

#endif
