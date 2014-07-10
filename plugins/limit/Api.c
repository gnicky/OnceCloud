#include <stdio.h>
#include <memory.h>
#include <stdlib.h>

#include "PluginInterface.h"
#include "Core.h"

const char * PluginName="Limit";
const char * PluginVersion="1.0.0.0";

int Initialize()
{
	return 0;
}

int Destroy()
{
	return 0;
}

int HandleGetRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * interface=request->GetHeader(request,"x-bws-interface");
	if(interface==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify ethernet interface.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;		
	}

	char * buffer=malloc(1048576);
	buffer[0]='\0';

	ShowLimitConfiguration(buffer,interface);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","text/plain");
	response->SetContent(response,buffer);

	free(buffer);

	return TRUE;
}

int HandleHeadRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * interface=request->GetHeader(request,"x-bws-interface");
	if(interface==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify ethernet interface.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;		
	}

	char * buffer=malloc(1048576);
	buffer[0]='\0';

	ShowLimitConfiguration(buffer,interface);

	response->StatusCode=200;
	response->SetHeader(response,"Content-Type","text/plain");
	response->SetContent(response,buffer);

	free(buffer);

	return TRUE;
}

int HandlePostRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->QueryString==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify function you need.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;		
	}

	if(strcmp(request->QueryString,"class")==0)
	{
		const char * interface=request->GetHeader(request,"x-bws-interface");
		const char * id=request->GetHeader(request,"x-bws-id");
		const char * speed=request->GetHeader(request,"x-bws-speed");

		if(interface!=NULL && id!=NULL && speed!=NULL)
		{
			AddLimitClass(interface,id,speed);

			response->StatusCode=200;
			response->SetContent(response,"");
			return TRUE;
		}
	}

	if(strcmp(request->QueryString,"filter")==0)
	{
		const char * interface=request->GetHeader(request,"x-bws-interface");
		const char * flowId=request->GetHeader(request,"x-bws-flow-id");

		if(interface!=NULL && flowId!=NULL)
		{
			AddLimitFilter(interface,flowId);

			response->StatusCode=200;
			response->SetContent(response,"");
			return TRUE;
		}
	}

	if(strcmp(request->QueryString,"ip")==0)
	{
		const char * interface=request->GetHeader(request,"x-bws-interface");
		const char * gateway=request->GetHeader(request,"x-bws-gateway");
		const char * ip=request->GetHeader(request,"x-bws-ip");
		const char * id=request->GetHeader(request,"x-bws-id");

		if(interface!=NULL && gateway!=NULL && ip!=NULL && id!=NULL)
		{
			AddLimitIP(interface,gateway,ip,id);

			response->StatusCode=200;
			response->SetContent(response,"");
			return TRUE;
		}
	}

	char ErrorMessage[]=
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		"<Error>\n\tInvalid parameters.\n</Error>\n";

	response->StatusCode=400;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,ErrorMessage);

	return TRUE;
}

int HandlePutRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	const char * interface=request->GetHeader(request,"x-bws-interface");
	if(interface==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify ethernet interface.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;		
	}

	LimitEthernet(interface);

	response->StatusCode=200;
	response->SetContent(response,"");

	return TRUE;
}

int HandleDeleteRequest(struct HttpRequest * request, struct HttpResponse * response)
{
	if(request->QueryString==NULL)
	{
		char ErrorMessage[]=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			"<Error>\n\tPlease specify function you need.\n</Error>\n";

		response->StatusCode=400;
		response->SetHeader(response,"Content-Type","application/xml");
		response->SetContent(response,ErrorMessage);
		return TRUE;		
	}

	if(strcmp(request->QueryString,"class")==0)
	{
		const char * interface=request->GetHeader(request,"x-bws-interface");
		const char * id=request->GetHeader(request,"x-bws-id");

		if(interface!=NULL && id!=NULL)
		{
			RemoveLimitClass(interface,id);

			response->StatusCode=200;
			response->SetContent(response,"");
			return TRUE;
		}
	}

	if(strcmp(request->QueryString,"ip")==0)
	{
		const char * interface=request->GetHeader(request,"x-bws-interface");
		const char * gateway=request->GetHeader(request,"x-bws-gateway");
		const char * ip=request->GetHeader(request,"x-bws-ip");

		if(interface!=NULL && gateway!=NULL && ip!=NULL)
		{
			RemoveLimitIP(interface,gateway,ip);

			response->StatusCode=200;
			response->SetContent(response,"");
			return TRUE;
		}
	}

	char ErrorMessage[]=
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		"<Error>\n\tInvalid parameters.\n</Error>\n";

	response->StatusCode=400;
	response->SetHeader(response,"Content-Type","application/xml");
	response->SetContent(response,ErrorMessage);

	return TRUE;
}
