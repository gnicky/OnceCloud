CC=gcc
LD=gcc

CFLAGS=-Wall -Werror -c
CLIBFLAGS=-Wall -Werror -c -fpic
LDFLAGS=-Wall -Werror
LDLIBFLAGS=-Wall -Werror -shared -lc

DLFLAGS=-ldl

MAIN=output/main.o \
	output/main

DHCP=output/plugins/dhcp.o \
	output/plugins/dhcp.so

all: prepare everything

everything: output/main output/plugins/dhcp.so

main: output/main

plugin: output/plugins/dhcp.so

dhcp: output/plugins/dhcp.so

prepare:
	if [ ! -d output ]; then mkdir output; fi
	if [ ! -d output/plugins ]; then mkdir output/plugins; fi

clean:
	rm -rf $(MAIN)
	rm -rf $(DHCP)
	rm -rf output

.PHONY: all everything prepare main plugins dhcp

#Main
output/main.o: main/Main.c
	$(CC) $(CFLAGS) -o $@ $<

output/main: output/main.o
	$(LD) $(LDFLAGS) $(DLFLAGS) -o $@ $^

#Plugins
output/plugins/dhcp.o: dhcp/Main.c
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/dhcp.so: output/plugins/dhcp.o
	$(LD) $(LDLIBFLAGS) -o $@ $^
