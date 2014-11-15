#include <stdio.h>
#include <memory.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/ioctl.h>

int main(int argc, char * argv [])
{
	int i;
	int sock;
	struct sockaddr_in sin;
	struct ifreq ifr;

	char g_eth_name[16];
	char g_macaddr[6];
	int g_subnetmask;
	int g_ipaddr;
	int g_broadcast_ipaddr;

	sock=socket(AF_INET,SOCK_DGRAM,0);
	if(sock==-1)
	{
		perror("socket");
	}
	strcpy(g_eth_name,"eth0");
	strcpy(ifr.ifr_name, g_eth_name);
 	printf("eth name:\t%s\n", g_eth_name);
 
	if(ioctl(sock,SIOCGIFHWADDR,&ifr)<0)
	{
		perror("ioctl");
	}
	memcpy(g_macaddr,ifr.ifr_hwaddr.sa_data,6);
 	printf("local mac:\t");
 	for(i=0;i<5;i++)
	{
		printf("%.2x:", (unsigned char)g_macaddr[i]);
 	}
	printf("%.2x\n",(unsigned char)g_macaddr[i]);
 
	if(ioctl(sock,SIOCGIFADDR,&ifr)<0)
	{
		perror("ioctl");
	}
	memcpy(&sin,&ifr.ifr_addr,sizeof(sin));
	g_ipaddr=sin.sin_addr.s_addr;
 	printf("local eth0:\t%s\n",inet_ntoa(sin.sin_addr));
 
	if(ioctl(sock,SIOCGIFBRDADDR,&ifr)<0)
	{
		perror("ioctl");
	}
	memcpy(&sin,&ifr.ifr_addr,sizeof(sin));
	g_broadcast_ipaddr=sin.sin_addr.s_addr;
	printf("broadcast:\t%s\n",inet_ntoa(sin.sin_addr));
 
	if(ioctl(sock,SIOCGIFNETMASK,&ifr)<0)
	{
		perror("ioctl");
	}
	memcpy(&sin,&ifr.ifr_addr,sizeof(sin));
	g_subnetmask=sin.sin_addr.s_addr;
	printf("subnetmask:\t%s\n",inet_ntoa(sin.sin_addr));
 
	close(sock);
	return 0;
}
