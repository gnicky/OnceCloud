#include <stdio.h>
#include <memory.h>

void AddLimitClass(const char * interface, const char * id, const char * speed)
{
	// TODO
	printf("Add Limit Class [Interface=%s; Id=%s; Speed=%s]\n",interface,id,speed);
}

void AddLimitFilter(const char * interface, const char * flowId)
{
	// TODO
	printf("Add Limit Filter [Interface=%s; FlowId=%s]\n",interface,flowId);
}

void AddLimitIP(const char * interface, const char * gateway, const char * ip, const char * id)
{
	// TODO
	printf("Add Limit IP [Interface=%s; Gateway=%s; IP=%s; Id=%s]\n",interface,gateway,ip,id);
}

void RemoveLimitClass(const char * interface, const char * id)
{
	// TODO
	printf("Remove Limit Class [Interface=%s; Id=%s]\n",interface,id);
}

void RemoveLimitIP(const char * interface, const char * gateway, const char * ip)
{
	// TODO
	printf("Remove Limit IP [Interface=%s; Gateway=%s; IP=%s]\n",interface,gateway,ip);
}

void LimitEthernet(const char * interface)
{
	// TODO
	printf("Limit Ethernet [Interface=%s]\n",interface);
}

void ShowLimitConfiguration(char * buffer, const char * interface)
{
	// TODO
	strcpy(buffer,"Test\n");
	printf("Show Limit Configuration [Interface=%s]\n",interface);
}
