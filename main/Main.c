#include <stdio.h>
#include <string.h>
#include <dlfcn.h>

void * PluginHandle=NULL;
int (* PluginFunction) (int argc, char * argv [])=NULL;
char * ErrorMessage=NULL;

int ActivatePlugin(char * pluginName, int count, char * values [])
{
	char pluginPath[256]="plugins/";
	strcat(pluginPath,pluginName);
	strcat(pluginPath,".so");	

	FILE * file=NULL;
	if((file=fopen(pluginPath,"rb"))==NULL)
	{
		fprintf(stderr,"Error: Cannot find plugin %s\n",pluginName);
		return -1;
	}
	fclose(file);

	PluginHandle=dlopen(pluginPath,RTLD_LAZY);
	ErrorMessage=dlerror();
	if(ErrorMessage!=NULL)
	{
		fprintf(stderr,"Error: %s\n",ErrorMessage);
		return -1;
	}

	*(void **)(&PluginFunction)=dlsym(PluginHandle,"Activate");
	ErrorMessage=dlerror();
	if(ErrorMessage!=NULL)
	{
		fprintf(stderr,"Error: %s\n",ErrorMessage);
		return -1;
	}

	fprintf(stderr,"Info: Activating plugin \"%s\"\n",pluginName);
	int returnValue=PluginFunction(count,values);
	fprintf(stderr,"Info: Plugin \"%s\" returns %d\n",pluginName,returnValue);

	dlclose(PluginHandle);
	ErrorMessage=dlerror();
	if(ErrorMessage!=NULL)
	{
		fprintf(stderr,"Error: %s\n",ErrorMessage);
		return -1;
	}
	return returnValue;
}

int main(int argc, char * argv [])
{
	if(argc<2)
	{
		fprintf(stderr,"Error: Missing required arguments.\n");
		return -1;
	}
	char * pluginName=argv[1];
	return ActivatePlugin(pluginName,argc-2,&(argv[2]));
}
