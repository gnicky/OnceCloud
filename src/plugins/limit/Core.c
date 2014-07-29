#include <stdio.h>
#include <memory.h>

#include "Process.h"

void AddLimitClass(const char * interface, const char * classId, const char * speed)
{
	printf("Add Limit Class [Interface=%s; ClassId=%s; Speed=%s]\n",interface,classId,speed);

	char commandLine[1000];
	sprintf(commandLine,"tc class add dev %s "
		"parent 1:1 classid 1:%s "
		"cbq bandwidth %sbit rate %sbit "
		"maxburst 20 allot 1514 prio 2 avpkt 1024 split 1:0 bounded"
		,interface,classId,speed,speed);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);

	sprintf(commandLine,"tc filter add dev %s "
		"parent 1:0 protocol ip "
		"prio 100 route to %s flowid 1:%s"
		,interface,classId,classId);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void AddLimitFilter(const char * interface, const char * flowId)
{
	printf("Add Limit Filter [Interface=%s; FlowId=%s]\n",interface,flowId);

	char commandLine[1000];
	sprintf(commandLine,"tc filter add dev %s "
		"parent 1:0 protocol ip "
		"prio 100 route to %s flowid 1:%s"
		,interface,flowId,flowId);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void AddLimitIP(const char * interface, const char * gateway, const char * ip, const char * flowId)
{
	printf("Add Limit IP [Interface=%s; Gateway=%s; IP=%s; FlowId=%s]\n",interface,gateway,ip,flowId);

	char commandLine[1000];
	sprintf(commandLine,"ip route add %s dev %s via %s realm %s",ip,interface,gateway,flowId);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void RemoveLimitClass(const char * interface, const char * classId)
{
	printf("Remove Limit Class [Interface=%s; ClassId=%s]\n",interface,classId);

	char commandLine[1000];
	sprintf(commandLine,"tc filter del dev %s parent 1:0 protocol ip prio 100 route to %s",interface,classId);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);

	sprintf(commandLine,"tc class del dev %s parent 1:1 classid 1:%s",interface,classId);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void RemoveLimitFilter(const char * interface, const char * flowId)
{
	printf("Remove Limit Filter [Interface=%s; FlowId=%s]",interface,flowId);

	char commandLine[1000];
	sprintf(commandLine,"tc filter del dev %s parent 1:0 protocol ip prio 100 route to %s",interface,flowId);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void RemoveLimitIP(const char * interface, const char * gateway, const char * ip)
{
	printf("Remove Limit IP [Interface=%s; Gateway=%s; IP=%s]\n",interface,gateway,ip);

	char commandLine[1000];
	sprintf(commandLine,"ip route del %s dev %s via %s",ip,interface,gateway);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void LimitEthernet(const char * interface)
{
	printf("Limit Ethernet [Interface=%s]\n",interface);

	char commandLine[1000];
	sprintf(commandLine,"tc qdisc add dev %s root handle 1:0 cbq bandwidth 1000Mbit avpkt 1024",interface);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);

	sprintf(commandLine,"tc class add dev %s parent 1:0 classid 1:1 cbq bandwidth 1000Mbit rate 1000Mbit maxburst 20 allot 1514 prio 8 avpkt 1024",interface);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);

	sprintf(commandLine,"tc filter add dev %s parent 1:0 protocol ip prio 100 route",interface);
	printf("Command: %s\n",commandLine);
	Execute(commandLine);
}

void ShowLimitConfiguration(char * buffer, const char * interface)
{
	printf("Show Limit Configuration [Interface=%s]\n",interface);

	char commandLine[1000];
	char outputBuffer[1000];

	buffer[0]='\0';
	strcat(buffer,"======qdisc========\n");
	sprintf(commandLine,"tc qdisc ls dev %s",interface);
	GetProcessOutput(outputBuffer,commandLine);
	strcat(buffer,outputBuffer);

	strcat(buffer,"======class========\n");
	sprintf(commandLine,"tc class ls dev %s",interface);
	GetProcessOutput(outputBuffer,commandLine);
	strcat(buffer,outputBuffer);

	strcat(buffer,"======filter=======\n");
	sprintf(commandLine,"tc filter ls dev %s",interface);
	GetProcessOutput(outputBuffer,commandLine);
	strcat(buffer,outputBuffer);

	strcat(buffer,"======ip route=====\n");
	sprintf(commandLine,"ip route");
	GetProcessOutput(outputBuffer,commandLine);
	strcat(buffer,outputBuffer);
}
