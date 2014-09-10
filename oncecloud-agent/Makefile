# OnceCloud Agent Makefile

CC=g++
LD=g++

AGENT_CC=$(QUIET_CC)$(CC)
AGENT_LD=$(QUIET_LD)$(LD)

CFLAGS=-g -Wall -Werror -c -Isrc/include/ -Ilib/include/
CXXFLAGS=-g -Wall -Werror -c -Isrc/include/ -Ilib/include/
LDFLAGS=-g -Wall -Werror
LIBFLAGS=-lcrypt -pthread

CCCOLOR="\033[34m"
LINKCOLOR="\033[34;1m"
SRCCOLOR="\033[33m"
BINCOLOR="\033[37;1m"
MAKECOLOR="\033[32;1m"
ENDCOLOR="\033[0m"

ifndef V
QUIET_CC = @printf '    %b %b\n' $(CCCOLOR)COMPILE$(ENDCOLOR) $(SRCCOLOR)$@$(ENDCOLOR) 1>&2;
QUIET_LD = @printf '    %b %b\n' $(LINKCOLOR)LINK$(ENDCOLOR) $(BINCOLOR)$@$(ENDCOLOR) 1>&2;
endif

AGENT=output/agent
COMPONENT=output/Main.o output/HttpServer.o
LIB=output/mongoose.o

all: prepare everything
prepare:
	@mkdir -p output
clean:
	@rm -rf output

.PHONY: all clean

everything: $(AGENT)

# Component
output/HttpServer.o: src/HttpServer.cpp src/include/HttpServer.h lib/include/mongoose.h
	$(AGENT_CC) $(CXXFLAGS) -o $@ $<

output/Main.o: src/Main.cpp
	$(AGENT_CC) $(CXXFLAGS) -o $@ $<

# Library
output/mongoose.o: lib/mongoose.c lib/include/mongoose.h
	$(AGENT_CC) $(CFLAGS) -o $@ $<

# Main
$(AGENT): $(COMPONENT) $(LIB)
	$(AGENT_LD) $(LDFLAGS) $(LIBFLAGS) -o $@ $^
