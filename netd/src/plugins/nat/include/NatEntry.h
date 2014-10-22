#ifndef _NAT_ENTRY_H_
#define _NAT_ENTRY_H_

struct NatEntry
{
	char InternalIPAddress[30];
	char ExternalIPAddress[30];
};

#endif
