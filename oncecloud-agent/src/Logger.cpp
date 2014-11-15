#include <iostream>
#include <iomanip>
#include <sstream>
#include <fstream>
#include <string>

#include <unistd.h>

#include "LogLevel.h"
#include "Logger.h"

#define MAX_LINE_SIZE 1024

bool EnableLogger=false;

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
	if(!EnableLogger)
	{
		return;
	}

	if(level.GetPriority()>this->GetMaxLevel().GetPriority())
	{
		return;
	}

	time_t now=time(NULL);
	struct tm * localTime=localtime(&now);

	std::stringstream log;
	log<<"["<<std::setw(4)<<std::setfill('0')<<(1900+localTime->tm_year)<<"-"
		<<std::setw(2)<<std::setfill('0')<<(1+localTime->tm_mon)<<"-"
		<<std::setw(2)<<std::setfill('0')<<localTime->tm_mday<<" "
		<<std::setw(2)<<std::setfill('0')<<localTime->tm_hour<<":"
		<<std::setw(2)<<std::setfill('0')<<localTime->tm_min<<":"
		<<std::setw(2)<<std::setfill('0')<<localTime->tm_sec<<"]"
		<<" ["<<getpid()<<" agent] ["<<level.GetLevel()<<"] "
		<<message<<std::endl;
	std::cout<<log.str();
	std::stringstream fileName;
	fileName<<"/var/log/agent-log-"
		<<std::setw(4)<<std::setfill('0')<<(1900+localTime->tm_year)<<"-"
		<<std::setw(2)<<std::setfill('0')<<(1+localTime->tm_mon)<<"-"
		<<std::setw(2)<<std::setfill('0')<<localTime->tm_mday<<".log";
	std::fstream writeFile;
	writeFile.open(fileName.str().c_str(),std::ios::app|std::ios::out);
	writeFile<<log.str();
	writeFile.close();
}


