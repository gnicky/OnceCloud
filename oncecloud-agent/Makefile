# OnceCloud Agent Makefile

CXX=g++
LD=g++

AGENT_CXX=$(QUIET_CXX)$(CXX)
AGENT_LD=$(QUIET_LD)$(LD)

CXXFLAGS=-g -Wall -Werror -c
LDFLAGS=-g -Wall -Werror

BOOSTFLAGS=-lboost_filesystem -pthread
LIBFLAGS=-lcrypt

CXXCOLOR="\033[35m"
LINKCOLOR="\033[35;1m"
SRCCOLOR="\033[33m"
BINCOLOR="\033[37;1m"
MAKECOLOR="\033[32;1m"
ENDCOLOR="\033[0m"

ifndef V
QUIET_CXX = @printf '    %b %b\n' $(CXXCOLOR)COMPILE$(ENDCOLOR) $(SRCCOLOR)$@$(ENDCOLOR) 1>&2;
QUIET_LD = @printf '    %b %b\n' $(LINKCOLOR)LINK$(ENDCOLOR) $(BINCOLOR)$@$(ENDCOLOR) 1>&2;
endif

AGENT=agent
COMPONENT=Main.o \
	Process.o \
	Request.o Response.o \
	SetPasswordRequest.o SetPasswordResponse.o SetPasswordHandler.o \
	ConfigureInterfaceRequest.o ConfigureInterfaceResponse.o ConfigureInterfaceHandler.o \
	RemoveInterfaceRequest.o RemoveInterfaceResponse.o RemoveInterfaceHandler.o

all: everything
clean:
	@rm -rf $(AGENT)
	@rm -rf *.o

.PHONY: all clean

everything: $(AGENT)

# Component
Main.o: Main.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

Process.o: Process.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

Request.o: Request.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

Response.o: Response.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

SetPasswordRequest.o: SetPasswordRequest.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

SetPasswordResponse.o: SetPasswordResponse.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

SetPasswordHandler.o: SetPasswordHandler.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

ConfigureInterfaceRequest.o: ConfigureInterfaceRequest.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

ConfigureInterfaceResponse.o: ConfigureInterfaceResponse.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

ConfigureInterfaceHandler.o: ConfigureInterfaceHandler.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

RemoveInterfaceRequest.o: RemoveInterfaceRequest.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

RemoveInterfaceResponse.o: RemoveInterfaceResponse.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

RemoveInterfaceHandler.o: RemoveInterfaceHandler.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

# Main
$(AGENT): $(COMPONENT)
	$(AGENT_LD) $(LDFLAGS) $(BOOSTFLAGS) $(LIBFLAGS) -o $@ $^

