#include <dirent.h>
#include <stdlib.h>
#include <memory.h>
#include <errno.h>

#include "Plugin.h"
#include "PluginManager.h"

const char * PluginBasePath="/usr/local/netd/plugins";

int ListFiles(const char * path, const char * suffix, char * buffer []);


void LoadPlugins()
{
	char ** pluginFileNames=NULL;
	int i=0;
	int count=0;

	pluginFileNames=malloc(MAX_PLUGIN_COUNT*sizeof(char *));
	for(i=0;i<MAX_PLUGIN_COUNT;i++)
	{
		pluginFileNames[i]=malloc(256);
	}

	count=ListFiles(PluginBasePath,".so",pluginFileNames);
	for(i=0;i<count;i++)
	{
		printf("%s\n",pluginFileNames[i]);
	}

	for(i=0;i<MAX_PLUGIN_COUNT;i++)
	{
		free(pluginFileNames[i]);
	}
	free(pluginFileNames);
}

int ListFiles(const char * path, const char * suffix, char ** buffer)
{
	int i=0;
	DIR * directory=NULL;
	struct dirent * entry=NULL;

	directory=opendir(path);
	if(directory==NULL)
	{
		printf("Error: Cannot open directory %s. ",path);
		printf("(%s)\n",strerror(errno));
		printf("Halt.\n");
		exit(1);
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
	return i;
}
