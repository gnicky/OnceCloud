#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <unistd.h>
#include <memory.h>
#include <errno.h>
#include <sys/stat.h>

#include "Type.h"
#include "Logger.h"
#include "String.h"

int IsFileExist(const char * path)
{
	if(path==NULL)
	{
		WriteLog(LOG_ERR,"IsFileExist: Argument null: path");
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
		WriteLog(LOG_ERR,"IsDirectoryExist: Argument null: path");
		return FALSE;
	}

	DIR * pointer=opendir(path);
	if(pointer==NULL)
	{
		return FALSE;
	}

	closedir(pointer);
	return TRUE;
}

int GetFileSize(const char * fileName)
{
	if(fileName==NULL)
	{
		WriteLog(LOG_ERR,"GetFileSize: Argument null: fileName");
		return -1;
	}

	struct stat fileStatus;
	if(stat(fileName,&fileStatus)<0)
	{
		WriteLog(LOG_ERR,"GetFileSize: Cannot get status of %s (%s)",fileName,strerror(errno));
		return -1;
	}
	return (int)fileStatus.st_size;
}

int CreateDirectory(const char * path)
{
	if(path==NULL)
	{
		WriteLog(LOG_ERR,"CreateDirectory: Argument null: path");
		return FALSE;
	}

	int status=mkdir(path,S_IRWXU|S_IRGRP|S_IXGRP|S_IROTH);
	if(status!=0)
	{
		WriteLog(LOG_ERR,"CreateDirectory: Cannot create directory %s (%s)",path,strerror(errno));
		return FALSE;
	}
	return TRUE;
}

int CreateDirectoryIfNotExist(const char * path)
{
	if(path==NULL)
	{
		WriteLog(LOG_ERR,"CreateDirectoryIfNotExist: Argument null: path");	
		return FALSE;
	}

	if(IsDirectoryExist(path))
	{
		return TRUE;
	}

	return CreateDirectory(path);
}

int ReadFile(const char * fileName, char * fileContent)
{
	if(fileName==NULL)
	{
		WriteLog(LOG_ERR,"ReadFile: Argument null: fileName");
		return FALSE;
	}

	if(fileContent==NULL)
	{
		WriteLog(LOG_ERR,"ReadFile: Argument null: fileContent");
		return FALSE;
	}

	int expectedReadCount;
	int actualReadCount;
	FILE * file=NULL;

	file=fopen(fileName,"rb");
	if(file==NULL)
	{
		WriteLog(LOG_ERR,"ReadFile: Cannot open file %s (%s)",fileName,strerror(errno));	
		return FALSE;
	}

	expectedReadCount=GetFileSize(fileName);
	actualReadCount=fread(fileContent,sizeof(char),expectedReadCount,file);
	if(actualReadCount!=expectedReadCount)
	{
		WriteLog(LOG_ERR,"ReadFile: Expected and actual read bytes don't match: %s",fileName);	
		return FALSE;
	}

	fclose(file);
	return TRUE;
}

int ReadFileAndSplit(const char * fileName, const char * delimiter, struct SplitResult ** result)
{
	int fileSize=GetFileSize(fileName);
	char * temp=malloc(fileSize+1);
	int ret=ReadFile(fileName,temp);
	if(ret!=TRUE)
	{
		free(temp);
		return FALSE;
	}
	temp[fileSize]='\0';
	*result=Split(temp,delimiter);
	free(temp);
	return TRUE;
}

int WriteFile(const char * fileName, const char * fileContent)
{
	if(fileName==NULL)
	{
		WriteLog(LOG_ERR,"WriteFile: Argument null: fileName");
		return FALSE;
	}

	if(fileContent==NULL)
	{
		WriteLog(LOG_ERR,"WriteFile: Argument null: fileContent");
		return FALSE;
	}

	FILE * file=NULL;
	int expectedWriteCount;
	int actualWriteCount;

	file=fopen(fileName,"w");
	if(file==NULL)
	{
		WriteLog(LOG_ERR,"WriteFile: Cannot open file %s (%s)",fileName,strerror(errno));
		return FALSE;
	}

	expectedWriteCount=strlen(fileContent);
	actualWriteCount=fwrite(fileContent,sizeof(char),expectedWriteCount,file);
	if(actualWriteCount!=expectedWriteCount)
	{
		WriteLog(LOG_ERR,"WriteFile: Expected and actual write bytes don't match: %s",fileName);
		return FALSE;
	}

	fclose(file);
	return TRUE;
}

int WriteSplitResultToFile(const char * fileName, struct SplitResult * result)
{
	char * temp=malloc(1048576);
	temp[0]='\0';
	int i=0;
	for(i=0;i<result->Count;i++)
	{
		strcat(temp,result->Content[i]);
		strcat(temp,result->Delimiter);
	}
	int ret=WriteFile(fileName,temp);
	free(temp);
	return ret;
}

int ListFiles(const char * path, const char * suffix, int * count, char ** buffer)
{
	if(path==NULL)
	{
		WriteLog(LOG_ERR,"ListFiles: Argument null: path");
		return FALSE;
	}

	if(suffix==NULL)
	{
		WriteLog(LOG_ERR,"ListFiles: Argument null: suffix");
		return FALSE;
	}

	if(count==NULL)
	{
		WriteLog(LOG_ERR,"ListFiles: Argument null: count");
		return FALSE;
	}

	if(buffer==NULL)
	{
		WriteLog(LOG_ERR,"ListFiles: Argument null: buffer");
		return FALSE;
	}

	int i=0;
	DIR * directory=NULL;
	struct dirent * entry=NULL;

	directory=opendir(path);
	if(directory==NULL)
	{
		WriteLog(LOG_ERR,"ListFiles: Cannot open directory %s (%s)",path,strerror(errno));
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

int RemoveFile(const char * fileName)
{
	if(fileName==NULL)
	{
		WriteLog(LOG_ERR,"RemoveFile: Argument null: fileName");
		return FALSE;
	}

	int status=remove(fileName);
	if(status!=0)
	{
		WriteLog(LOG_ERR,"RemoveFile: Cannot remove file %s (%s)",fileName,strerror(errno));
		return FALSE;
	}

	return TRUE;
}

