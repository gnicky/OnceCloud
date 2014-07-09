#ifndef _CORE_H_
#define _CORE_H_

#include "NatEntry.h"

int ListNatEntry(struct NatEntry * buffer, int * count);
int AddNat(const char * internal, const char * external);
int RemoveNat(const char * internal, const char * external);
int InitializeNat();

#endif
