#include <dirent.h>
#include <stdlib.h>
#include <memory.h>
#include <errno.h>
#include <dlfcn.h>

#include "Plugin.h"
#include "PluginManager.h"

const char * PluginBasePath="/usr/local/netd/plugins";

struct Plugin Plugins[MAX_PLUGIN_COUNT];
int PluginCount;

int ListFiles(const char * path, const char * suffix, char * buffer []);
struct Plugin DoLoadPlugin(const char * path);
void LoadPlugins();
void UnloadPlugins();
void InitializePlugins();
void DestroyPlugins();

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
	PluginCount=count;
	for(i=0;i<count;i++)
	{
		Plugins[i]=DoLoadPlugin(pluginFileNames[i]);
	}

	for(i=0;i<MAX_PLUGIN_COUNT;i++)
	{
		free(pluginFileNames[i]);
	}
	free(pluginFileNames);

	InitializePlugins();
}

void UnloadPlugins()
{
	char * error=NULL;
	int i=0;

	DestroyPlugins();

	for(i=0;i<PluginCount;i++)
	{
		dlclose(Plugins[i].Handle);
		if((error=dlerror())!=NULL)
		{
			printf("Error: Cannot destroy plugin %s. ",Plugins[i].Name);
			printf("(%s)\n",error);
			printf("Halt.\n");
			exit(1);
		}
	}
}

void InitializePlugins()
{
	int i=0;

	for(i=0;i<PluginCount;i++)
	{
		Plugins[i].Initialize();
	}
}

void DestroyPlugins()
{
	int i=0;

	for(i=0;i<PluginCount;i++)
	{
		Plugins[i].Destroy();
	}
}

struct Plugin DoLoadPlugin(const char * path)
{
	char * error=NULL;

	FILE * file=NULL;
	if((file=fopen(path,"rb"))==NULL)
	{
		printf("Error: Cannot open plugin file %s. ",path);
		printf("(%s)\n",strerror(errno));
		printf("Halt.\n");
		exit(1);
	}
	fclose(file);

	void * handle=dlopen(path,RTLD_LAZY);
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot load plugin file %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	const char ** name=(const char **)dlsym(handle,"PluginName");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot read plugin name from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}
	
	const char ** version=(const char **)dlsym(handle,"PluginVersion");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot read plugin version from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	struct Plugin plugin;
	memset(&plugin,0,sizeof(struct Plugin));

	plugin.Handle=handle;
	strcpy(plugin.Path,path);
	strcpy(plugin.Name,*name);
	strcpy(plugin.Version,*version);

	*(void **)(&(plugin.Initialize))=dlsym(handle,"Initialize");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"Initialize\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}	

	*(void **)(&(plugin.Destroy))=dlsym(handle,"Destroy");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"Destroy\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	*(void **)(&(plugin.HandleGetRequest))=dlsym(handle,"HandleGetRequest");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"HandleGetRequest\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	*(void **)(&(plugin.HandleHeadRequest))=dlsym(handle,"HandleHeadRequest");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"HandleHeadRequest\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	*(void **)(&(plugin.HandlePostRequest))=dlsym(handle,"HandlePostRequest");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"HandlePostRequest\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	*(void **)(&(plugin.HandlePutRequest))=dlsym(handle,"HandlePutRequest");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"HandlePutRequest\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	*(void **)(&(plugin.HandleDeleteRequest))=dlsym(handle,"HandleDeleteRequest");
	if((error=dlerror())!=NULL)
	{
		printf("Error: Cannot get function \"HandleDeleteRequest\" from %s. ",path);
		printf("(%s)\n",error);
		printf("Halt.\n");
		exit(1);
	}

	printf("Plugin file loaded: %s\n",plugin.Path);
	printf("Name=%s, Version=%s\n\n",plugin.Name,plugin.Version);
	return plugin;
}

struct Plugin * FindPlugin(const char * pluginName)
{
	int i=0;
	for(i=0;i<PluginCount;i++)
	{
		if(strcmp(Plugins[i].Name,pluginName)==0)
		{
			return &(Plugins[i]);
		}
	}
	return NULL;
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
