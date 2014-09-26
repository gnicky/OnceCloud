#pragma once

#include <string>

class LogLevel
{
public:
	LogLevel();
	LogLevel(int priority, const std::string & level);
	~LogLevel();

	int GetPriority() const;
	const std::string & GetLevel() const;

	static const LogLevel Emergency;
	static const LogLevel Alert;
	static const LogLevel Critical;
	static const LogLevel Error;
	static const LogLevel Warning;
	static const LogLevel Notice;
	static const LogLevel Information;
	static const LogLevel Debug;
protected:
	void SetPriority(int priority);
	void SetLevel(const std::string & level);
private:
	int priority;
	std::string level;
};
