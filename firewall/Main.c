#include <stdio.h>

int Activate(int count, char * values [])
{
	printf("Activating Firewall Plugin\n");
	printf("Count: %d\n",count);
	printf("Values:\n");
	int i=0;
	for(i=0;i<count;i++)
	{
		printf("Argument %d: %s\n",i+1,values[i]);
	}
	return 0;
}
