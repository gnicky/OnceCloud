CC=gcc
LD=gcc

CFLAGS=-Wall -Werror -c -Icommon/include/
CLIBFLAGS=-Wall -Werror -c -fpic -Icommon/include/
LDFLAGS=-Wall -Werror
LDLIBFLAGS=-Wall -Werror -shared -lc

DLFLAGS=-ldl
PTHREADFLAGS=-pthread

COMMON=output/common-File.o output/common-Process.o output/common-Mongoose.o

NETSH=output/netsh-Main.o \
	output/netsh

NETD=output/netd-Main.o \
	output/netd

DHCP=output/plugins/dhcp-Main.o \
	output/plugins/dhcp.so

FIREWALL=output/plugins/firewall-Main.o \
	output/plugins/firewall.so

NAT=output/plugins/nat-Main.o \
	output/plugins/nat.so

all: do_prepare everything do_package do_install

package: do_prepare everything do_package

install: do_prepare everything do_package do_install

everything: output/netsh output/netd \
	output/plugins/dhcp.so output/plugins/firewall.so output/plugins/nat.so

do_prepare:
	if [ ! -d output ]; then mkdir output; fi
	if [ ! -d output/plugins ]; then mkdir output/plugins; fi

do_package:
	cp -r doc output/
	tar --create --file=output/netsh.tar.gz --gzip --directory=output netsh netd doc plugins/dhcp.so plugins/firewall.so plugins/nat.so
	cat installer/installer.sh output/netsh.tar.gz > output/netsh-installer
	chmod +x output/netsh-installer

do_install:
	output/netsh-installer

clean:
	rm -rf $(NETSH)
	rm -rf $(DHCP)
	rm -rf $(FIREWALL)
	rm -rf $(NAT)
	rm -rf output

.PHONY: all everything prepare package clean

#Common
output/common-File.o: common/File.c common/include/File.h \
	common/include/Type.h
	$(CC) $(CLIBFLAGS) -o $@ $<

output/common-Process.o: common/Process.c common/include/Process.h \
	common/include/Type.h
	$(CC) $(CLIBFLAGS) -o $@ $<

output/common-Mongoose.o: common/Mongoose.c common/include/Mongoose.h
	$(CC) $(CLIBFLAGS) $(PTHREADFLAGS) -o $@ $<

#Net Shell
output/netsh-Main.o: netsh/Main.c
	$(CC) $(CFLAGS) -o $@ $<

output/netsh: output/netsh-Main.o
	$(LD) $(LDFLAGS) $(DLFLAGS) -o $@ $^

#Net Daemon
output/netd-Main.o: netd/Main.c \
	common/include/Mongoose.h
	$(CC) $(CLIBFLAGS) $(PTHREADFLAGS) -o $@ $<

output/netd: output/netd-Main.o \
	output/common-Mongoose.o
	$(LD) $(LDFLAGS) $(DLFLAGS) $(PTHREADFLAGS) -o $@ $^

#Plugins
#DHCP
output/plugins/dhcp-Main.o: dhcp/Main.c \
	common/include/File.h
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/dhcp.so: output/plugins/dhcp-Main.o \
	output/common-File.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

#Firewall
output/plugins/firewall-Main.o: firewall/Main.c \
	common/include/File.h common/include/Process.h
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/firewall.so: output/plugins/firewall-Main.o \
	output/common-File.o output/common-Process.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

#NAT
output/plugins/nat-Main.o: nat/Main.c \
	common/include/File.h common/include/Process.h
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/nat.so: output/plugins/nat-Main.o \
	output/common-File.o output/common-Process.o
	$(LD) $(LDLIBFLAGS) -o $@ $^


