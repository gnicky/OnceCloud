#include <string>
#include "LogLevel.h"

LogLevel::LogLevel()
{

}

LogLevel::LogLevel(int priority, const std::string & level)
{
	this->SetPriority(priority);
	this->SetLevel(level);
}

LogLevel::~LogLevel()
{

}

int LogLevel::GetPriority() const
{
	return this->priority;
}

void LogLevel::SetPriority(int priority)
{
	this->priority=priority;
}

const std::string & LogLevel::GetLevel() const
{
	return this->level;
}

void LogLevel::SetLevel(const std::string & level)
{
	this->level=level;
}

const LogLevel LogLevel::Emergency(0,"EMERG");
const LogLevel LogLevel::Alert(1,"ALERT");
const LogLevel LogLevel::Critical(2,"CRIT");
const LogLevel LogLevel::Error(3,"ERROR");
const LogLevel LogLevel::Warning(4,"WARN");
const LogLevel LogLevel::Notice(5,"NOTICE");
const LogLevel LogLevel::Information(6,"INFO");
const LogLevel LogLevel::Debug(7,"DEBUG");
