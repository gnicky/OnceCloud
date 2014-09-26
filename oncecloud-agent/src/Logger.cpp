#include <stdio.h>
#include <memory.h>
#include <stdarg.h>
#include <stdlib.h>
#include <time.h>
#include <syslog.h>
#include <time.h>
#include <errno.h>
#include <unistd.h>

#include <iostream>
#include <string>
#include "LogLevel.h"
#include "Logger.h"

#define MAX_LINE_SIZE 1024

Logger::Logger(const LogLevel & maxLevel)
{
	this->SetMaxLevel(maxLevel);
}

Logger::~Logger()
{

}

const LogLevel & Logger::GetMaxLevel() const
{
	return this->maxLevel;
}

void Logger::SetMaxLevel(const LogLevel & maxLevel)
{
	this->maxLevel=maxLevel;
}

void Logger::Write(const LogLevel & level, const std::string & message)
{
	if(level.GetPriority()>this->GetMaxLevel().GetPriority())
	{
		return;
	}

	char header[MAX_LINE_SIZE];
	memset(header,0,sizeof(header));

	time_t now;
	time(&now);
	struct tm * localTime=localtime(&now);

	sprintf(header,"[%04d-%02d-%02d %02d:%02d:%02d] [%d agent] [%s] "
		,(1900+localTime->tm_year),(1+localTime->tm_mon)
		,localTime->tm_mday,localTime->tm_hour
		,localTime->tm_min,localTime->tm_sec
		,getpid(),level.GetLevel().c_str());

	std::cout<<header<<message<<std::endl;
}

