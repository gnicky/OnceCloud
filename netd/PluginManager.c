#include <stdio.h>
#include <dirent.h>
#include <stdlib.h>
#include <memory.h>
#include <errno.h>
#include <dlfcn.h>

#include "Plugin.h"
#include "File.h"
#include "PluginManager.h"
#include "Type.h"
#include "Logger.h"

static const char * PluginBasePath="/usr/local/netd/plugins";

static struct Plugin Plugins[MAX_PLUGIN_COUNT];
static int PluginCount;

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

	int status;
	status=ListFiles(PluginBasePath,".so",&count,pluginFileNames);
	if(status!=TRUE)
	{
		WriteLog(LOG_ERR,"Cannot list plugin files. Exiting.");
		exit(1);
	}
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
			WriteLog(LOG_ERR,"Cannot destroy plugin %s (%s). Exiting.",Plugins[i].Name,error);
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
		WriteLog(LOG_ERR,"Cannot open plugin file %s (%s). Exiting.",path,strerror(errno));
		exit(1);
	}
	fclose(file);

	void * handle=dlopen(path,RTLD_NOW);
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot load plugin file %s (%s). Exiting.",path,error);
		exit(1);
	}

	const char ** name=(const char **)dlsym(handle,"PluginName");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot read plugin name from %s. Exiting.",path);
		exit(1);
	}
	
	const char ** version=(const char **)dlsym(handle,"PluginVersion");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot read plugin version from %s. Exiting.",path);
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
		WriteLog(LOG_ERR,"Cannot get function \"Initialize\" from %s. Exiting.",path);
		exit(1);
	}	

	*(void **)(&(plugin.Destroy))=dlsym(handle,"Destroy");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot get function \"Destroy\" from %s. Exiting.",path);
		exit(1);
	}

	*(void **)(&(plugin.HandleGetRequest))=dlsym(handle,"HandleGetRequest");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot get function \"HandleGetRequest\" from %s. Exiting.",path);
		exit(1);
	}

	*(void **)(&(plugin.HandleHeadRequest))=dlsym(handle,"HandleHeadRequest");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot get function \"HandleHeadRequest\" from %s. Exiting.",path);
		exit(1);
	}

	*(void **)(&(plugin.HandlePostRequest))=dlsym(handle,"HandlePostRequest");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot get function \"HandlePostRequest\" from %s. Exiting.",path);
		exit(1);
	}

	*(void **)(&(plugin.HandlePutRequest))=dlsym(handle,"HandlePutRequest");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot get function \"HandlePutRequest\" from %s. Exiting.",path);
		exit(1);
	}

	*(void **)(&(plugin.HandleDeleteRequest))=dlsym(handle,"HandleDeleteRequest");
	if((error=dlerror())!=NULL)
	{
		WriteLog(LOG_ERR,"Cannot get function \"HandleDeleteRequest\" from %s. Exiting.",path);
		exit(1);
	}

	WriteLog(LOG_INFO,"Plugin loaded: %s (%s)",plugin.Name,plugin.Version);

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

