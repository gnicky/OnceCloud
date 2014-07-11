#ifndef _CONFIGURATION_H_
#define _CONFIGURATION_H_

#include "Listener.h"

struct Configuration
{
	char WorkerProcesses[10];
	char WorkerConnections[10];

	int ListenerCount;
	struct Listener Listeners[100];
};

#endif
