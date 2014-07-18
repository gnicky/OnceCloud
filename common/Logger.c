#include <stdio.h>
#include <memory.h>
#include <stdarg.h>
#include <stdlib.h>
#include <time.h>
#include <syslog.h>
#include <time.h>

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

void WriteLog(int priority, const char * format, ...)
{
	char log[MAX_LINE_SIZE];
	memset(log,0,sizeof(log));

	char message[MAX_LINE_SIZE];
	memset(message,0,sizeof(message));

	va_list list;
	va_start(list,format);
	vsprintf(message,format,list);
	va_end(list);

	time_t now;
	time(&now);
	struct tm * localTime=localtime(&now);

	sprintf(log,"[%04d-%02d-%02d %02d:%02d:%02d] [%s] "
		,(1900+localTime->tm_year)
		,(1+localTime->tm_mon)
		,localTime->tm_mday
		,localTime->tm_hour
		,localTime->tm_min
		,localTime->tm_sec
		,LogLevel[priority]);

	strcat(log,message);

	puts(log);
}

