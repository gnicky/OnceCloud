#pragma once

#include <string>
#include "LogLevel.h"

class Logger
{
public:
	Logger(const LogLevel & maxLevel);
	~Logger();

	const LogLevel & GetMaxLevel() const;

	void Write(const LogLevel & level, const std::string & message);

protected:
	void SetMaxLevel(const LogLevel & maxLevel);

private:
	LogLevel maxLevel;	
};

