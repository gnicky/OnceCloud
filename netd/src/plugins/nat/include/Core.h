#ifndef _CORE_H_
#define _CORE_H_

#include "NatEntry.h"

int ListNatEntry(struct NatEntry * buffer, int * count);
int AddNat(const char * internal, const char * external, const char * interface);
int RemoveNat(const char * internal, const char * external, const char * interface);
int AddPortForwarding(const char * protocol, const char * internalAddress, const char * internalPort
	, const char * externalAddress, const char * externalPort);
int RemovePortForwarding(const char * protocol, const char * internalAddress, const char * internalPort
	, const char * externalAddress, const char * externalPort);
int InitializeNat();

#endif
