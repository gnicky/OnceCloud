#include <sys/stat.h>
#include <dirent.h>
#include <unistd.h>

#define TRUE 1
#define FALSE 0

int IsFileExist(const char * path)
{
	if(access(path,F_OK)==0)
	{
		return TRUE;
	}
	return FALSE;
}

int IsDirectoryExist(const char * path)
{
	DIR * pointer=opendir(path);
	if(pointer==NULL)
	{
		return FALSE;
	}
	closedir(pointer);
	return TRUE;
}

unsigned long GetFileSize(const char * fileName)
{
	struct stat fileStatus;
	if(stat(fileName,&fileStatus)<0)
	{
		return -1;
	}
	return (unsigned long)fileStatus.st_size;
}

int CreateDirectory(const char * path)
{
	int status=mkdir(path,S_IRWXU|S_IRGRP|S_IXGRP|S_IROTH);
	if(status!=0)
	{
		return FALSE;
	}
	return TRUE;
}

int CreateDirectoryIfNotExist(const char * path)
{
	if(IsDirectoryExist(path))
	{
		return TRUE;
	}
	return CreateDirectory(path);
}
