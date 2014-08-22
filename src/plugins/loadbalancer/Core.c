#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "Configuration.h"
#include "File.h"
#include "Process.h"

const char * NginxConfigurationFile="/etc/nginx/nginx.conf";
const char * NginxProgramFile="/usr/sbin/nginx";

void InitializeConfiguration(char * buffer, const char * workerProcesses, const char * workerConnections)
{
	buffer[0]='\0';
	char temp[1000];
	sprintf(temp,"worker_processes %s;\n\n",workerProcesses);
	strcat(buffer,temp);

	sprintf(temp,"events {\n"
		"\tworker_connections %s;\n"
		"}\n",workerConnections);
	strcat(buffer,temp);
}

void DoAppendListener(char * buffer, struct Listener * listener)
{
	int i=0;

	strcat(buffer,listener->Protocol);
	strcat(buffer," {\n");
	strcat(buffer,"\tupstream cluster {\n");

	if(strcmp(listener->Protocol,"http")==0)
	{
		if(strcmp(listener->Policy,"0")==0)
		{
			strcat(buffer,"\t\tsticky;\n\n");
		}
		else if(strcmp(listener->Policy,"1")==0)
		{
			strcat(buffer,"\t\tfair;\n\n");
		}		
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

void SaveConfiguration(struct Configuration * configuration)
{
	char * buffer=malloc(1048576);
	InitializeConfiguration(buffer,configuration->WorkerProcesses,configuration->WorkerConnections);
	AppendListeners(buffer,configuration->ListenerCount,configuration->Listeners);
	WriteFile(NginxConfigurationFile,buffer);
	free(buffer);
}

void RestartService()
{
	Execute("pkill nginx");
	Execute(NginxProgramFile);
}
