#ifndef _CONFIGURATION_H_
#define _CONFIGURATION_H_

struct Configuration
{
	char Protocol[10];
	int Port;
	char CACertificate[32768];
	char ServerCertificate[32768];
	char ServerPrivateKey[32768];
	char DiffieHellmanParameter[32768];
	char TLSAuthKey[32768];
	char NetworkAddress[50];
	char Netmask[50];
};

#endif
