#include "Plugin.h"
#include "PluginManager.h"

int main(int argc, char * argv [])
{
	LoadPlugins();
	struct Plugin * plugin=FindPlugin("DHCP");
	printf("%s\n",plugin->Path);
	UnloadPlugins();
	return 0;
}
