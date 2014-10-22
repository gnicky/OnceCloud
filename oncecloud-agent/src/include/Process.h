#pragma once

#include <string>

class Process
{
public:
	static bool Execute(const std::string & commandLine);
	static bool SetInputAndExecute(const std::string & input, const std::string & commandLine);
	static std::string ExecuteAndGetOutput(const std::string & commandLine);
private:
	Process();
	~Process();
	static bool CloseAllFiles();
};

