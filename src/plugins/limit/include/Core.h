#ifndef _CORE_H_
#define _CORE_H_

void AddLimitClass(const char * interface, const char * classId, const char * speed);
void AddLimitFilter(const char * interface, const char * flowId);
void AddLimitIP(const char * interface, const char * gateway, const char * ip, const char * flowId);

void RemoveLimitClass(const char * interface, const char * classId);
void RemoveLimitFilter(const char * interface, const char * flowId);
void RemoveLimitIP(const char * interface, const char * gateway, const char * ip);

void LimitEthernet(const char * interface);

void ShowLimitConfiguration(char * buffer, const char * interface);

#endif
