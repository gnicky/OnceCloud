#include <stdio.h>
#include <dirent.h>
#include <unistd.h>
#include <memory.h>
#include <errno.h>
#include <sys/stat.h>

#include "Type.h"
#include "Logger.h"

int IsFileExist(const char * path)
{
	if(path==NULL)
	{
		return FALSE;
	}

	if(access(path,F_OK)==0)
	{
		return TRUE;
	}
	return FALSE;
}

int IsDirectoryExist(const char * path)
{
	if(path==NULL)
	{
		return FALSE;
	}

	DIR * pointer=opendir(path);
	if(pointer==NULL)
	{
		WriteLog(LOG_ERR,strerror(errno));
		return FALSE;
	}
	closedir(pointer);
	return TRUE;
}

int GetFileSize(const char * fileName)
{
	if(fileName==NULL)
	{
		return -1;
	}

	struct stat fileStatus;
	if(stat(fileName,&fileStatus)<0)
	{
		WriteLog(LOG_ERR,strerror(errno));
		return -1;
	}
	return (int)fileStatus.st_size;
}

int CreateDirectory(const char * path)
{
	int status=mkdir(path,S_IRWXU|S_IRGRP|S_IXGRP|S_IROTH);
	if(status!=0)
	{
		WriteLog(LOG_ERR,strerror(errno));
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

int ReadFile(const char * fileName, char * fileContent)
{
	int expectedReadCount;
	int actualReadCount;
	FILE * file=NULL;

	file=fopen(fileName,"r");
	if(file==NULL)
	{
		WriteLog(LOG_ERR,strerror(errno));	
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

int WriteFile(const char * fileName, const char * fileContent)
{
	FILE * file=NULL;
	int expectedWriteCount;
	int actualWriteCount;

	file=fopen(fileName,"w");
	if(file==NULL)
	{
		WriteLog(LOG_ERR,strerror(errno));
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


int ListFiles(const char * path, const char * suffix, int * count, char ** buffer)
{
	int i=0;
	DIR * directory=NULL;
	struct dirent * entry=NULL;

	directory=opendir(path);
	if(directory==NULL)
	{
		WriteLog(LOG_ERR,strerror(errno));
		return FALSE;
	}

	while((entry=readdir(directory))!=NULL)
	{
		if(strcmp(entry->d_name,".")==0)
		{
			continue;
		}
		if(strcmp(entry->d_name,"..")==0)
		{
			continue;
		}
		const char * suffixBegin=entry->d_name+strlen(entry->d_name)-strlen(suffix);
		if(strcmp(suffixBegin,suffix)==0)
		{
			buffer[i][0]='\0';
			strcat(buffer[i],path);
			strcat(buffer[i],"/");
			strcat(buffer[i],entry->d_name);
			i++;
		}
	}

	closedir(directory);
	*count=i;
	return TRUE;
}
