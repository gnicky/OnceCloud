# OnceCloud Gateway Makefile

CXX=g++
LD=g++

GATEWAY_CXX=$(QUIET_CXX)$(CXX)
GATEWAY_LD=$(QUIET_LD)$(LD)

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

GATEWAY=gateway
COMPONENT=Main.o 

all: everything
clean:
	@rm -rf $(GATEWAY)
	@rm -rf *.o

.PHONY: all clean

everything: $(GATEWAY)

# Component
Main.o: Main.cxx
	$(GATEWAY_CXX) $(CXXFLAGS) -o $@ $<

# Main
$(GATEWAY): $(COMPONENT)
	$(GATEWAY_LD) $(LDFLAGS) -o $@ $^
