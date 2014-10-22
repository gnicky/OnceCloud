#ifndef _CORE_H_
#define _CORE_H_

#include "FirewallRule.h"
#include "PingRule.h"
#include "Rule.h"
#include "FirewallConfiguration.h"

int AddRule(const char * protocol, const char * internal, const char * external, const char * port);
int RemoveRule(const char * protocol, const char * internal, const char * external, const char * port);

int AllowPing(const char * target, const char * from);
int DenyPing(const char * target, const char * from);

int InitializeFirewall();

int SetFirewallRules(struct FirewallConfiguration * configuration);

int ListFirewallRule(struct FirewallRule * buffer, int * count);
int ListPingRule(struct PingRule * buffer, int * count);

#endif
