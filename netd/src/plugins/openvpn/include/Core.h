#ifndef _CORE_H_
#define _CORE_H_

#include "Configuration.h"

void GenerateTLSAuthKey(char * buffer);
void Configure(struct Configuration * configuration);
void StopService();

#endif
