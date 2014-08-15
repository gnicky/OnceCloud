#include <stdio.h>

#include "Configuration.h"
#include "Process.h"
#include "File.h"

const char * OpenVPNProgramFileName="/usr/local/openvpn/sbin/openvpn";
const char * ConfigurationDir="/usr/local/openvpn/conf";
const char * TLSAuthKeyFileName="/usr/local/openvpn/conf/ta.key";
const char * TLSTempAuthKeyFileName="/usr/local/openvpn/conf/temp.key";

void GenerateTLSAuthKey(char * buffer)
{
	CreateDirectoryIfNotExist(ConfigurationDir);
	char temp[1000];
	sprintf(temp,"%s --genkey --secret %s",OpenVPNProgramFileName,TLSTempAuthKeyFileName);
	Execute(temp);
	ReadFile(TLSTempAuthKeyFileName,buffer);
	RemoveFile(TLSTempAuthKeyFileName);
}

void Configure(struct Configuration * configuration)
{
	CreateDirectoryIfNotExist(ConfigurationDir);

}

