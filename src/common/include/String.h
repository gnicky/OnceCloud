#ifndef _STRING_H_
#define _STRING_H_

#include "SplitResult.h"

struct SplitResult * Split(const char * source, const char * delimiter);
void FreeSplitResult(struct SplitResult * result);
char * Replace(const char * source, const char * subString, const char * replacement);

#endif
