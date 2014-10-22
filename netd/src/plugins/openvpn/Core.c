#include <stdio.h>
#include <string.h>

#include "Configuration.h"
#include "Process.h"
#include "File.h"

const char * OpenVPNProgramFileName="/usr/sbin/openvpn";
const char * ConfigurationDir="/etc/openvpn/";
const char * OpenVPNConfigurationFileName="/etc/openvpn/server.conf";
const char * OpenVPNLogFileName="/etc/openvpn/log";
const char * IfconfigPoolPersistFileName="/etc/openvpn/ipp.txt";
const char * CACertificateFileName="/etc/openvpn/ca.crt";
const char * ServerCertificateFileName="/etc/openvpn/server.crt";
const char * ServerPrivateKeyFileName="/etc/openvpn/server.key";
const char * DiffieHellmanParameterFileName="/etc/openvpn/dh.pem";
const char * TLSAuthKeyFileName="/etc/openvpn/ta.key";
const char * TLSTempAuthKeyFileName="/etc/openvpn/temp.key";

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
	char fileContent[1000]={0};
	char temp[1000];

	sprintf(temp,"port %d\nproto %s\n",configuration->Port,configuration->Protocol);
	strcat(fileContent,temp);
	strcat(fileContent,"dev tun\n");
	sprintf(temp,"ca %s\n",CACertificateFileName);
	strcat(fileContent,temp);
	sprintf(temp,"cert %s\n",ServerCertificateFileName);
	strcat(fileContent,temp);
	sprintf(temp,"key %s\n",ServerPrivateKeyFileName);
	strcat(fileContent,temp);
	sprintf(temp,"dh %s\n",DiffieHellmanParameterFileName);
	strcat(fileContent,temp);
	sprintf(temp,"server %s %s\n",configuration->NetworkAddress,configuration->Netmask);
	strcat(fileContent,temp);
	sprintf(temp,"ifconfig-pool-persist %s\n",IfconfigPoolPersistFileName);
	strcat(fileContent,temp);
	strcat(fileContent,"client-to-client\n");
	strcat(fileContent,"keepalive 10 120\n");
	sprintf(temp,"tls-auth %s 0\n",TLSAuthKeyFileName);
	strcat(fileContent,temp);
	strcat(fileContent,"comp-lzo\n");
	strcat(fileContent,"persist-key\n");
	strcat(fileContent,"persist-tun\n");
	strcat(fileContent,"status openvpn-status.log\n");
	strcat(fileContent,"verb 3\n");

	WriteFile(OpenVPNConfigurationFileName,fileContent);
	WriteFile(CACertificateFileName,configuration->CACertificate);
	WriteFile(ServerCertificateFileName,configuration->ServerCertificate);
	WriteFile(ServerPrivateKeyFileName,configuration->ServerPrivateKey);
	WriteFile(DiffieHellmanParameterFileName,configuration->DiffieHellmanParameter);
	WriteFile(TLSAuthKeyFileName,configuration->TLSAuthKey);

	sprintf(temp,"pkill openvpn");
	Execute(temp);
	sprintf(temp,"nohup %s --config %s> %s 2>&1 &"
		,OpenVPNProgramFileName,OpenVPNConfigurationFileName,OpenVPNLogFileName);
	Execute(temp);
}

void StopService()
{
	Execute("pkill openvpn");
}
