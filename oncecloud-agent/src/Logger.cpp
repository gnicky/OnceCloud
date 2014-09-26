#include <stdio.h>
#include <memory.h>
#include <stdarg.h>
#include <stdlib.h>
#include <time.h>
#include <syslog.h>
#include <time.h>
#include <errno.h>
#include <unistd.h>

#include <string>

using namespace std;

#define MAX_LINE_SIZE 1024

const char LogLevel[8][10]=
{
	"EMERGENCY",
	"ALERT",
	"CRITICAL",
	"ERROR",
	"WARNING",
	"NOTICE",
	"INFO",
	"DEBUG"
};

void WriteLog(int priority, const string & message)
{
	char log[MAX_LINE_SIZE];
	memset(log,0,sizeof(log));

	time_t now;
	time(&now);
	struct tm * localTime=localtime(&now);

	sprintf(log,"[%04d-%02d-%02d %02d:%02d:%02d] [%d agent] [%s] "
		,(1900+localTime->tm_year)
		,(1+localTime->tm_mon)
		,localTime->tm_mday
		,localTime->tm_hour
		,localTime->tm_min
		,localTime->tm_sec
		,getpid()
		,LogLevel[priority]);

	strcat(log,message.c_str());

	// char fileName[100]={0};
	// sprintf(fileName,"/usr/local/agent/log/log-%04d-%02d-%02d.log",(1900+localTime->tm_year),(1+localTime->tm_mon),localTime->tm_mday);

	// FILE * logFile=fopen(fileName,"at");
	// if(logFile==NULL)
	// {
	// 	printf("WriteLog: Cannot open log file: %s (%s). Aborting.\n",fileName,strerror(errno));
	// 	abort();
	// }
	// fprintf(logFile,"%s\n",log);
	// fclose(logFile);

	puts(log);
}

