#ifndef _FILE_H_
#define _FILE_H_

#include "String.h"

int IsFileExist(const char * path);
int IsDirectoryExist(const char * path);

int GetFileSize(const char * fileName);

int CreateDirectory(const char * path);
int CreateDirectoryIfNotExist(const char * path);

int ListFiles(const char * path, const char * suffix, int * count, char ** buffer);

int ReadFile(const char * fileName, char * fileContent);
int ReadFileAndSplit(const char * fileName, const char * delimiter, struct SplitResult ** result);
int WriteFile(const char * fileName, const char * fileContent);
int WriteSplitResultToFile(const char * fileName, struct SplitResult * result);

int RemoveFile(const char * fileName);

#endif
