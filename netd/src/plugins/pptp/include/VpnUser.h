#ifndef _VPN_USER_H_
#define _VPN_USER_H_

struct VpnUser
{
	char UserName[30];
	char Password[30];
	char Server[30];
	char IPAddress[30];
};

#endif
