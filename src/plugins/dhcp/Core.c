#include "DhcpConfiguration.h"
#include "Parser.h"

void GetDhcpConfiguration(struct DhcpConfiguration * configuration)
{
	ReadDhcpConfiguration(configuration);
	SaveDhcpConfiguration(configuration);
}
