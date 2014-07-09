#ifndef _CORE_H_
#define _CORE_H_

#include "FirewallRule.h"

int AddRule(const char * protocol, const char * internal, const char * external, const char * port);
int RemoveRule(const char * protocol, const char * internal, const char * external, const char * port);
int InitFirewallRule();
int ListFirewallRule(struct FirewallRule * buffer, int * count);

#endif
