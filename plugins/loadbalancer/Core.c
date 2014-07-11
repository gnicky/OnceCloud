#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "Configuration.h"

void InitializeConfiguration(char * buffer, int workerProcesses, int workerConnections)
{
	buffer[0]='\0';
	char temp[1000];
	sprintf(temp,"worker_processes %d;\n\n",workerProcesses);
	strcat(buffer,temp);

	sprintf(temp,"events {\n"
		"\tworker_connections %d;\n"
		"}\n",workerConnections);
	strcat(buffer,temp);
}

void DoAppendListener(char * buffer, struct Listener * listener)
{
	int i=0;

	strcat(buffer,listener->Protocol);
	strcat(buffer," {\n");
	strcat(buffer,"\tupstream cluster {\n");

	if(strcmp(listener->Protocol,"http")==0 && listener->Policy==1)
	{
		strcat(buffer,"\t\tfair;\n\n");
	}

	for(i=0;i<listener->RuleCount;i++)
	{
		strcat(buffer,"\t\tserver ");
		strcat(buffer,listener->Rules[i].IPAddress);
		strcat(buffer,":");
		strcat(buffer,listener->Rules[i].Port);
		strcat(buffer,";\n");
	}
	strcat(buffer,"\n");

	// TODO
	strcat(buffer,"\t\tcheck interval=3000 rise=2 fall=5 timeout=1000;\n");

	strcat(buffer,"\t}\n");
	strcat(buffer,"\n");

	strcat(buffer,"\tserver {\n");
	strcat(buffer,"\t\tlisten ");
	strcat(buffer,listener->Port);
	strcat(buffer,";\n");

	if(strcmp(listener->Protocol,"tcp")==0)
	{
		strcat(buffer,"\t\tproxy_pass cluster;\n");
	}

	if(strcmp(listener->Protocol,"http")==0)
	{
		strcat(buffer,"\n\t\tlocation / {\n");
		strcat(buffer,"\t\t\tproxy_pass http://cluster;\n");
		strcat(buffer,"\t\t}\n");
	}

	strcat(buffer,"\t}\n");

	strcat(buffer,"}\n");
}

void AppendListeners(char * buffer, int count, struct Listener * listeners)
{
	int i=0;
	for(i=0;i<count;i++)
	{
		strcat(buffer,"\n");
		DoAppendListener(buffer,&listeners[i]);
	}
}

void GenerateConfiguration(char * buffer, struct Configuration * configuration)
{
	InitializeConfiguration(buffer,configuration->WorkerProcesses,configuration->WorkerConnections);
	AppendListeners(buffer,configuration->ListenerCount,configuration->Listeners);
}

int Test()
{
	struct Configuration configuration;
	configuration.WorkerProcesses=5;
	configuration.WorkerConnections=1000;
	configuration.ListenerCount=2;

	strcpy(configuration.Listeners[0].Port,"80");
	strcpy(configuration.Listeners[0].Protocol,"http");
	configuration.Listeners[0].Policy=1;
	configuration.Listeners[0].RuleCount=2;
	strcpy(configuration.Listeners[0].Rules[0].Port,"8080");
	strcpy(configuration.Listeners[0].Rules[0].IPAddress,"10.0.0.1");
	strcpy(configuration.Listeners[0].Rules[1].Port,"8090");
	strcpy(configuration.Listeners[0].Rules[1].IPAddress,"10.0.0.2");

	strcpy(configuration.Listeners[1].Port,"443");
	strcpy(configuration.Listeners[1].Protocol,"tcp");
	configuration.Listeners[1].Policy=0;
	configuration.Listeners[1].RuleCount=2;
	strcpy(configuration.Listeners[1].Rules[0].Port,"9080");
	strcpy(configuration.Listeners[1].Rules[0].IPAddress,"10.0.0.3");
	strcpy(configuration.Listeners[1].Rules[1].Port,"9090");
	strcpy(configuration.Listeners[1].Rules[1].IPAddress,"10.0.0.4");

	char * buffer=malloc(1048576);
	GenerateConfiguration(buffer,&configuration);
	printf("%s\n",buffer);
	free(buffer);

	return 0;
}
