#ifndef _CORE_H_
#define _CORE_H_

int AddRoute(const char * destination, const char * netmask, const char * gateway);
int RemoveRoute(const char * destination, const char * netmask, const char * gateway);

#endif
