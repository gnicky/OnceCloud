#ifndef _CONFIGURATION_H_
#define _CONFIGURATION_H_

#include "Listener.h"

struct Configuration
{
	int WorkerProcesses;
	int WorkerConnections;

	int ListenerCount;
	struct Listener Listeners[100];
};

#endif
