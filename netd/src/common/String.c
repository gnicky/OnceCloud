#include <stdlib.h>
#include <string.h>

#include "SplitResult.h"

struct SplitResult * Split(const char * source, const char * delimiter)
{
	char * temp=malloc(strlen(source)+1);
	strcpy(temp,source);

	// Get Count
	int i=0;
	char * current=temp;
	char * result=NULL;
	char * lastPosition=NULL;
	while((result=strtok_r(current,delimiter,&lastPosition))!=NULL)
	{
		i++;
		current=NULL;
	}

	struct SplitResult * ret=malloc(sizeof(struct SplitResult));
	ret->Count=i;
	ret->Delimiter=malloc(strlen(delimiter)+1);
	strcpy(ret->Delimiter,delimiter);

	// Split
	char ** content=malloc(sizeof(char *)*(ret->Count));
	i=0;
	current=temp;
	result=NULL;
	lastPosition=NULL;
	strcpy(temp,source);
	while((result=strtok_r(current,delimiter,&lastPosition))!=NULL)
	{	
		content[i]=malloc(strlen(result)+1);
		strcpy(content[i],result);
		i++;
		current=NULL;	
	}

	ret->Content=content;

	free(temp);
	return ret;
}

void FreeSplitResult(struct SplitResult * result)
{
	free(result->Delimiter);
	int i=0;
	for(i=0;i<result->Count;i++)
	{
		free(result->Content[i]);
	}
	free(result);
}

char * Replace(const char * source, const char * subString, const char * replacement)
{
	char * token=NULL;
	char * newString=NULL;
	char * oldString=NULL;  

	if(subString==NULL || replacement==NULL)
	{
		return strdup(source);
	}

	newString=strdup(source);
	while((token=strstr(newString,subString))!=NULL)
	{
		oldString=newString;
		newString=malloc(strlen(oldString)-strlen(subString)+strlen(replacement)+1);  
		if(newString==NULL)
		{
			free(oldString);
			return NULL;
		}
		memcpy(newString,oldString,token-oldString);
		memcpy(newString+(token-oldString),replacement,strlen(replacement));
		memcpy(newString+(token-oldString)+strlen(replacement),token+strlen(subString),strlen(oldString)-strlen(subString)-(token-oldString));
		memset(newString+strlen(oldString)-strlen(subString)+strlen(replacement),0,1);
		free(oldString);  
    }

    return newString;
}

