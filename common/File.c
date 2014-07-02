#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <unistd.h>
#include <memory.h>
#include <sys/stat.h>

#include <Type.h>

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
		// TODO: More error handling
		return -1;
	}
	return (unsigned long)fileStatus.st_size;
}

int CreateDirectory(const char * path)
{
	int status=mkdir(path,S_IRWXU|S_IRGRP|S_IXGRP|S_IROTH);
	if(status!=0)
	{
		// TODO: More error handling
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

int ReadAllText(const char * fileName, char * fileContent)
{
	int expectedReadCount;
	int actualReadCount;
	FILE * file=NULL;

	file=fopen(fileName,"r");
	if(file==NULL)
	{
		// TODO: More error handling
		return FALSE;
	}

	expectedReadCount=GetFileSize(fileName);
	actualReadCount=fread(fileContent,sizeof(char),expectedReadCount,file);
	if(actualReadCount!=expectedReadCount)
	{
		return FALSE;
	}

	fclose(file);
	return TRUE;
}

int WriteAllText(const char * fileName, const char * fileContent)
{
	FILE * file=NULL;
	int expectedWriteCount;
	int actualWriteCount;

	file=fopen(fileName,"w");
	if(file==NULL)
	{
		// TODO: More error handling
		return FALSE;
	}

	expectedWriteCount=strlen(fileContent);
	actualWriteCount=fwrite(fileContent,sizeof(char),expectedWriteCount,file);
	if(actualWriteCount!=expectedWriteCount)
	{
		return FALSE;
	}

	fclose(file);
	return TRUE;
}
