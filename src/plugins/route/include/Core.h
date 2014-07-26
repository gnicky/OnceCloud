#ifndef _CORE_H_
#define _CORE_H_

void AddRoute(const char * interface, const char * address, const char * netmask);
void RemoveRoute(const char * interface);
void ConfigureNat(const char * interface);

#endif
