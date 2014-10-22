#ifndef _LOGGER_H_
#define _LOGGER_H_

#define	LOG_EMERG 0
#define	LOG_ALERT 1
#define	LOG_CRIT 2
#define	LOG_ERR 3
#define	LOG_WARNING 4
#define	LOG_NOTICE 5
#define	LOG_INFO 6
#define	LOG_DEBUG 7

void WriteLog(int priority, const char * format, ...);

#endif
