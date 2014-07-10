#ifndef _CORE_H_
#define _CORE_H_

void AddLimitClass(const char * interface, const char * id, const char * speed);
void AddLimitFilter(const char * interface, const char * flowId);
void AddLimitIP(const char * interface, const char * gateway, const char * ip, const char * id);
void RemoveLimitClass(const char * interface, const char * id);
void RemoveLimitIP(const char * interface, const char * gateway, const char * ip);
void LimitEhternet(const char * interface);
void ShowLimitConfiguration(char * buffer, const char * interface);

#endif
