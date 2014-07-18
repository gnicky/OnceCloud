#ifndef _FILE_H_
#define _FILE_H_

int IsFileExist(const char * path);
int IsDirectoryExist(const char * path);

unsigned long GetFileSize(const char * fileName);

int CreateDirectory(const char * path);
int CreateDirectoryIfNotExist(const char * path);

int ReadFile(const char * fileName, char * fileContent);
int WriteFile(const char * fileName, const char * fileContent);

#endif
