#ifndef _PLUGIN_MANAGER_H_
#define _PLUGIN_MANAGER_H_

#define MAX_PLUGIN_COUNT 100

void LoadPlugins();
void UnloadPlugins();

struct Plugin * FindPlugin(const char * pluginName);

#endif
