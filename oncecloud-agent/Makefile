# OnceCloud Agent Makefile

CPP=g++
LD=g++

#CPP=/root/buildroot/output/host/usr/bin/x86_64-beyond-linux-uclibc-g++
#LD=/root/buildroot/output/host/usr/bin/x86_64-beyond-linux-uclibc-g++

AGENT_CPP=$(QUIET_CPP)$(CPP)
AGENT_LD=$(QUIET_LD)$(LD)

CPPFLAGS=-g -Wall -Werror -Ilib/include/ -Isrc/include/ -c
LDFLAGS=-g

LIBFLAGS=-lcrypt

CPPCOLOR="\033[35m"
LINKCOLOR="\033[35;1m"
SRCCOLOR="\033[33m"
BINCOLOR="\033[37;1m"
MAKECOLOR="\033[32;1m"
ENDCOLOR="\033[0m"

ifndef V
QUIET_CPP = @printf '    %b %b\n' $(CPPCOLOR)COMPILE$(ENDCOLOR) $(SRCCOLOR)$@$(ENDCOLOR) 1>&2;
QUIET_LD = @printf '    %b %b\n' $(LINKCOLOR)LINK$(ENDCOLOR) $(BINCOLOR)$@$(ENDCOLOR) 1>&2;
endif

AGENT=output/agent
LIB=output/jsoncpp.o
COMPONENT=output/Main.o \
	output/Process.o output/String.o output/LogLevel.o output/Logger.o \
	output/Request.o output/Response.o \
	output/PingRequest.o output/PingResponse.o output/PingHandler.o \
	output/RestartNetworkRequest.o output/RestartNetworkResponse.o output/RestartNetworkHandler.o \
	output/SetPasswordRequest.o output/SetPasswordResponse.o output/SetPasswordHandler.o \
	output/ConfigureInterfaceRequest.o output/ConfigureInterfaceResponse.o output/ConfigureInterfaceHandler.o \
	output/RemoveInterfaceRequest.o output/RemoveInterfaceResponse.o output/RemoveInterfaceHandler.o

all: prepare everything
prepare:
	@mkdir -p output
clean:
	@rm -rf $(AGENT)
	@rm -rf $(LIB)
	@rm -rf $(COMPONENT)
	@rm -rf output

.PHONY: all clean

everything: $(AGENT)

# Agent
$(AGENT): $(COMPONENT) $(LIB)
	$(AGENT_LD) $(LDFLAGS) $(LIBFLAGS) -o $@ $^

# Library
output/jsoncpp.o: lib/jsoncpp.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

# Component
output/Main.o: src/Main.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/Process.o: src/Process.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/String.o: src/String.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/LogLevel.o: src/LogLevel.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/Logger.o: src/Logger.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/Request.o: src/Request.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/Response.o: src/Response.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/PingRequest.o: src/PingRequest.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/PingResponse.o: src/PingResponse.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/PingHandler.o: src/PingHandler.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/RestartNetworkRequest.o: src/RestartNetworkRequest.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/RestartNetworkResponse.o: src/RestartNetworkResponse.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/RestartNetworkHandler.o: src/RestartNetworkHandler.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/SetPasswordRequest.o: src/SetPasswordRequest.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/SetPasswordResponse.o: src/SetPasswordResponse.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/SetPasswordHandler.o: src/SetPasswordHandler.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/ConfigureInterfaceRequest.o: src/ConfigureInterfaceRequest.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/ConfigureInterfaceResponse.o: src/ConfigureInterfaceResponse.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/ConfigureInterfaceHandler.o: src/ConfigureInterfaceHandler.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/RemoveInterfaceRequest.o: src/RemoveInterfaceRequest.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/RemoveInterfaceResponse.o: src/RemoveInterfaceResponse.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<

output/RemoveInterfaceHandler.o: src/RemoveInterfaceHandler.cpp
	$(AGENT_CPP) $(CPPFLAGS) -o $@ $<


