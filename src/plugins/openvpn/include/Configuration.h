#ifndef _CONFIGURATION_H_
#define _CONFIGURATION_H_

struct Configuration
{
	char CACertificate[4096];
	char ServerCertificate[4096];
	char ServerPrivateKey[4096];
	char DiffieHellmanParameter[4096];
	char NetworkAddress[50];
	char Netmask[50];
};

#endif
