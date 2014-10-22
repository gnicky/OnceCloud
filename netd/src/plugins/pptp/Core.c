#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "File.h"
#include "VpnUser.h"
#include "Configuration.h"
#include "Process.h"

const char * PPTPConfigurationFileName="/etc/pptpd.conf";
const char * SecretFileName="/etc/ppp/chap-secrets";

void ConfigureAddress(struct Configuration * configuration)
{
	char * content=malloc(1048576);
	char temp[100];
	content[0]='\0';
	int address[3];
	sscanf(configuration->NetworkAddress,"%d.%d.%d",&address[0],&address[1],&address[2]);
	strcat(content,"option /etc/ppp/options.pptpd\n");
	strcat(content,"logwtmp\n");
	sprintf(temp,"connections %d\n",configuration->MaxConnections);
	strcat(content,temp);
	sprintf(temp,"localip %d.%d.%d.1\n",address[0],address[1],address[2]);
	strcat(content,temp);
	sprintf(temp,"remoteip %d.%d.%d.2-254\n",address[0],address[1],address[2]);
	strcat(content,temp);
	WriteFile(PPTPConfigurationFileName,content);
	free(content);
}

void ConfigureUsers(struct Configuration * configuration)
{
	char * content=malloc(1048576);
	content[0]='\0';
	char temp[1000];
	int i=0;
	for(i=0;i<configuration->UserCount;i++)
	{
		sprintf(temp,"%s\t%s\t%s\t%s\n"
			,configuration->Users[i].UserName,configuration->Users[i].Server
			,configuration->Users[i].Password,configuration->Users[i].IPAddress);
		strcat(content,temp);
	}
	WriteFile(SecretFileName,content);
	free(content);
}

void Configure(struct Configuration * configuration)
{
	ConfigureAddress(configuration);
	ConfigureUsers(configuration);
	Execute("chkconfig pptpd on");
	Execute("service pptpd stop");
	Execute("service pptpd start");
}

void StopService()
{
	Execute("chkconfig pptpd off");
	Execute("service pptpd stop");
}
