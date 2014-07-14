#ifndef _RULE_H_
#define _RULE_H_

struct Rule
{
	char Protocol[10];
	int StartPort;
	int EndPort;
	char ToIPAddress[10];
};

#endif
