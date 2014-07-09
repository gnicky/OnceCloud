CC=gcc
LD=gcc

CFLAGS=-Wall -Werror -c -Icommon/include/
CLIBFLAGS=-Wall -Werror -c -fpic -Icommon/include/
LDFLAGS=-Wall -Werror
LDLIBFLAGS=-Wall -Werror -shared -lc

NETDFLAGS=-Inetd/include/
DHCPFLAGS=-Iplugins/dhcp/include/
NATFLAGS=-Iplugins/nat/include/

DLFLAGS=-ldl
PTHREADFLAGS=-pthread

all: do_prepare everything do_package do_install

package: do_prepare everything do_package

install: do_prepare everything do_package do_install

everything: output/netd \
	output/plugins/dhcp.so output/plugins/firewall.so output/plugins/nat.so

do_prepare:
	if [ ! -d output ]; then mkdir output; fi
	if [ ! -d output/plugins ]; then mkdir output/plugins; fi

do_package:
	cp -r doc output/
	tar --create --file=output/netd.tar.gz --gzip --directory=output netd doc plugins
	cat installer/installer.sh output/netd.tar.gz > output/netd-installer
	chmod +x output/netd-installer

do_install:
	output/netd-installer

clean:
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

#Net Daemon
output/netd-Main.o: netd/Main.c \
	common/include/Mongoose.h
	$(CC) $(CLIBFLAGS) $(NETDFLAGS) $(PTHREADFLAGS) -o $@ $<

output/netd-PluginManager.o: netd/PluginManager.c netd/include/PluginManager.h \
	netd/include/Plugin.h \
	common/include/PluginInterface.h
	$(CC) $(CLIBFLAGS) $(NETDFLAGS) -o $@ $<

output/netd-HttpHelper.o: netd/HttpHelper.c netd/include/HttpHelper.h \
	common/include/HttpRequest.h common/include/HttpResponse.h common/include/HttpHeader.h
	$(CC) $(CLIBFLAGS) $(NETDFLAGS) -o $@ $<

output/netd: output/netd-Main.o output/netd-PluginManager.o output/netd-HttpHelper.o \
	output/common-Mongoose.o
	$(LD) $(LDFLAGS) $(DLFLAGS) $(PTHREADFLAGS) -o $@ $^

#Plugins
#DHCP
output/plugins-dhcp-Api.o: plugins/dhcp/Api.c \
	common/include/PluginInterface.h
	$(CC) $(CLIBFLAGS) $(DHCPFLAGS) -o $@ $<

output/plugins-dhcp-Core.o: plugins/dhcp/Core.c \
	common/include/File.h
	$(CC) $(CLIBFLAGS) $(DHCPFLAGS) -o $@ $<

output/plugins/dhcp.so: output/plugins-dhcp-Api.o output/plugins-dhcp-Core.o \
	output/common-File.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

#NAT
output/plugins-nat-Api.o: plugins/nat/Api.c \
	common/include/PluginInterface.h
	$(CC) $(CLIBFLAGS) $(NATFLAGS) -o $@ $<

output/plugins-nat-Core.o: plugins/nat/Core.c \
	common/include/File.h common/include/Process.h
	$(CC) $(CLIBFLAGS) $(NATFLAGS) -o $@ $<

output/plugins/nat.so: output/plugins-nat-Api.o output/plugins-nat-Core.o \
	output/common-File.o output/common-Process.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

#Firewall
output/plugins-firewall-Main.o: plugins/firewall/Main.c \
	common/include/File.h common/include/Process.h
	$(CC) $(CLIBFLAGS) -o $@ $<

output/plugins/firewall.so: output/plugins-firewall-Main.o \
	output/common-File.o output/common-Process.o
	$(LD) $(LDLIBFLAGS) -o $@ $^

