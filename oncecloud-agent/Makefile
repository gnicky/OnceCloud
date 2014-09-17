# OnceCloud Agent Makefile

CXX=g++
LD=g++

AGENT_CXX=$(QUIET_CXX)$(CXX)
AGENT_LD=$(QUIET_LD)$(LD)

CXXFLAGS=-g -Wall -Werror -c -Iinclude/
LDFLAGS=-g -Wall -Werror

CXXCOLOR="\033[34m"
LINKCOLOR="\033[34;1m"
SRCCOLOR="\033[33m"
BINCOLOR="\033[37;1m"
MAKECOLOR="\033[32;1m"
ENDCOLOR="\033[0m"

ifndef V
QUIET_CXX = @printf '    %b %b\n' $(CXXCOLOR)COMPILE$(ENDCOLOR) $(SRCCOLOR)$@$(ENDCOLOR) 1>&2;
QUIET_LD = @printf '    %b %b\n' $(LINKCOLOR)LINK$(ENDCOLOR) $(BINCOLOR)$@$(ENDCOLOR) 1>&2;
endif

AGENT=agent
COMPONENT=Main.o 

all: everything
clean:
	@rm -rf $(AGENT)
	@rm -rf *.o

.PHONY: all clean

everything: $(AGENT)

# Component
Main.o: Main.cxx
	$(AGENT_CXX) $(CXXFLAGS) -o $@ $<

# Main
$(AGENT): $(COMPONENT)
	$(AGENT_LD) $(LDFLAGS) -o $@ $^
