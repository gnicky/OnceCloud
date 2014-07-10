#ifndef _CORE_H_
#define _CORE_H_

#include "FirewallRule.h"

int AddRule(const char * protocol, const char * internal, const char * external, const char * port);
int RemoveRule(const char * protocol, const char * internal, const char * external, const char * port);

int AllowPing(const char * target, const char * from);
int DenyPing(const char * target, const char * from);

int InitializeFirewall();

int ListFirewallRule(struct FirewallRule * buffer, int * count);
int ListPingRule(struct PingRule * buffer, int * count)

#endif
