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

FIREWALL=output/plugins/firewall.o \
	output/plugins/firewall.so

ROUTE=output/plugins/route.o \
	output/plugins/route.so

all: prepare everything package

everything: output/main \
	output/plugins/dhcp.so output/plugins/firewall.so output/plugins/route.so

main: output/main

plugin: output/plugins/dhcp.so

dhcp: output/plugins/dhcp.so

firewall: output/plugins/firewall.so

route: output/plugins/route.so

prepare:
	if [ ! -d output ]; then mkdir output; fi
	if [ ! -d output/plugins ]; then mkdir output/plugins; fi

package:
	if [ ! -d package ]; then mkdir package; fi
	if [ ! -d package/plugins ]; then mkdir package/plugins; fi
	cp output/main package/
	cp output/plugins/*.so package/plugins/

clean:
	rm -rf $(MAIN)
	rm -rf $(DHCP)
	rm -rf $(FIREWALL)
	rm -rf $(ROUTE)
	rm -rf output

allclean:
	rm -rf $(MAIN)
	rm -rf $(DHCP)
	rm -rf $(FIREWALL)
	rm -rf $(ROUTE)
	rm -rf output
	rm -rf package

.PHONY: all everything prepare package main plugins dhcp firewall route

#Main
output/main.o: main/Main.c
	$(CC) $(CFLAGS) -o $@ $<

output/main: output/main.o
	$(LD) $(LDFLAGS) $(DLFLAGS) -o $@ $^

#Plugins
#DHCP
output/plugins/dhcp.o: dhcp/Main.c
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/dhcp.so: output/plugins/dhcp.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

#Firewall
output/plugins/firewall.o: firewall/Main.c
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/firewall.so: output/plugins/firewall.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

#Route
output/plugins/route.o: route/Main.c
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/route.so: output/plugins/route.o
	$(LD) $(LDLIBFLAGS) -o $@ $^


