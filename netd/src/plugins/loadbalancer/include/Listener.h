#ifndef _LISTENER_H_
#define _LISTENER_H_

#include "Rule.h"

struct Listener
{
	char Port[10];
	char Protocol[10];
	char Policy[10];

	int RuleCount;
	struct Rule Rules[100];
};

#endif
