#ifndef _RULE_H_
#define _RULE_H_

struct Rule
{
	char Protocol[20];
	int StartPort;
	int EndPort;
	char ToIPAddress[20];
};

#endif
