#ifndef _LISTENER_H_
#define _LISTENER_H_

#include "Rule.h"

struct Listener
{
	char Port[10];
	char Protocol[10];
	int Policy;

	int RuleCount;
	struct Rule Rules[100];
};

#endif
