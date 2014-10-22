#ifndef _PROCESS_H_
#define _PROCESS_H_

int Execute(const char * commandLine);
int SetProcessInput(char * buffer, const char * commandLine);
int GetProcessOutput(char * buffer, const char * commandLine);

#endif
