NETD_CC=$(QUIET_CC)$(CC)
NETD_LD=$(QUIET_LD)$(LD)
NETD_INSTALL=$(QUIET_INSTALL)

CC=gcc
LD=gcc

ifndef V
QUIET_CC=@printf '%b %b\n' CC $< 1>&2;
QUIET_LD=@printf '%b %b\n' LD $@ 1>&2;
QUIET_INSTALL = @printf '' 1>&2;
endif

CFLAGS=-g -Wall -Werror -c -Icommon/include/
CLIBFLAGS=-g -Wall -Werror -c -fpic -Icommon/include/
LDFLAGS=-g -Wall -Werror
LDLIBFLAGS=-g -Wall -Werror -shared -lc

NETDFLAGS=-Inetd/include/
DHCPFLAGS=-Iplugins/dhcp/include/
NATFLAGS=-Iplugins/nat/include/
FIREWALLFLAGS=-Iplugins/firewall/include/
LIMITFLAGS=-Iplugins/limit/include/
LOADBALANCERFLAGS=-Iplugins/loadbalancer/include/
ROUTEFLAGS=-Iplugins/route/include/

DLFLAGS=-ldl
PTHREADFLAGS=-pthread

all: do_prepare everything do_package do_install

package: do_prepare everything do_package

install: do_prepare everything do_package do_install

everything: output/netd \
	output/plugins/dhcp.so output/plugins/firewall.so output/plugins/route.so \
	output/plugins/nat.so output/plugins/limit.so output/plugins/loadbalancer.so

do_prepare:
	$(NETD_INSTALL)if [ ! -d output ]; then mkdir output; fi
	$(NETD_INSTALL)if [ ! -d output/plugins ]; then mkdir output/plugins; fi

do_package:
	$(NETD_INSTALL)tar --create --file=output/netd.tar.gz --gzip --directory=output netd plugins
	$(NETD_INSTALL)cat installer/installer.sh output/netd.tar.gz > output/netd-installer
	$(NETD_INSTALL)chmod +x output/netd-installer

do_install:
	$(NETD_INSTALL)output/netd-installer

clean:
	$(NETD_INSTALL)rm -rf output

dep:
	$(CC) -MM netd/*.c -Icommon/include/ $(NETDFLAGS)

.PHONY: all everything prepare package clean dep

#Common
output/common-File.o: common/File.c common/include/File.h \
	common/include/Type.h
	$(NETD_CC) $(CLIBFLAGS) -o $@ $<

output/common-Frozen.o: common/Frozen.c common/include/Frozen.h
	$(NETD_CC) $(CLIBFLAGS) -o $@ $<

output/common-Logger.o: common/Logger.c common/include/Logger.h
	$(NETD_CC) $(CLIBFLAGS) -o $@ $<

output/common-Mongoose.o: common/Mongoose.c common/include/Mongoose.h
	$(NETD_CC) $(CLIBFLAGS) $(PTHREADFLAGS) -o $@ $<

output/common-Process.o: common/Process.c common/include/Process.h \
	common/include/Type.h
	$(NETD_CC) $(CLIBFLAGS) -o $@ $<

#Net Daemon
output/netd-Main.o: netd/Main.c \
	common/include/Mongoose.h common/include/Logger.h
	$(NETD_CC) $(CLIBFLAGS) $(NETDFLAGS) $(PTHREADFLAGS) -o $@ $<

output/netd-PluginManager.o: netd/PluginManager.c netd/include/PluginManager.h \
	netd/include/Plugin.h \
	common/include/PluginInterface.h common/include/File.h
	$(NETD_CC) $(CLIBFLAGS) $(NETDFLAGS) -o $@ $<

output/netd-HttpHelper.o: netd/HttpHelper.c netd/include/HttpHelper.h \
	common/include/HttpRequest.h common/include/HttpResponse.h common/include/HttpHeader.h
	$(NETD_CC) $(CLIBFLAGS) $(NETDFLAGS) -o $@ $<

output/netd: output/netd-Main.o output/netd-PluginManager.o output/netd-HttpHelper.o \
	output/common-Mongoose.o output/common-File.o output/common-Logger.o
	$(NETD_LD) $(LDFLAGS) $(DLFLAGS) $(PTHREADFLAGS) -o $@ $^

#Plugins
#DHCP
output/plugins-dhcp-Api.o: plugins/dhcp/Api.c \
	common/include/PluginInterface.h common/include/Frozen.h
	$(NETD_CC) $(CLIBFLAGS) $(DHCPFLAGS) -o $@ $<

output/plugins-dhcp-Core.o: plugins/dhcp/Core.c \
	common/include/File.h
	$(NETD_CC) $(CLIBFLAGS) $(DHCPFLAGS) -o $@ $<

output/plugins/dhcp.so: output/plugins-dhcp-Api.o output/plugins-dhcp-Core.o \
	output/common-File.o output/common-Frozen.o output/common-Logger.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

#NAT
output/plugins-nat-Api.o: plugins/nat/Api.c \
	common/include/PluginInterface.h
	$(NETD_CC) $(CLIBFLAGS) $(NATFLAGS) -o $@ $<

output/plugins-nat-Core.o: plugins/nat/Core.c \
	common/include/File.h common/include/Process.h
	$(NETD_CC) $(CLIBFLAGS) $(NATFLAGS) -o $@ $<

output/plugins/nat.so: output/plugins-nat-Api.o output/plugins-nat-Core.o \
	output/common-File.o output/common-Process.o output/common-Logger.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

#Firewall
output/plugins-firewall-Api.o: plugins/firewall/Api.c \
	common/include/PluginInterface.h common/include/Frozen.h
	$(NETD_CC) $(CLIBFLAGS) $(FIREWALLFLAGS) -o $@ $<

output/plugins-firewall-Core.o: plugins/firewall/Core.c \
	common/include/File.h common/include/Process.h
	$(NETD_CC) $(CLIBFLAGS) $(FIREWALLFLAGS) -o $@ $<

output/plugins/firewall.so: output/plugins-firewall-Api.o output/plugins-firewall-Core.o  \
	output/common-File.o output/common-Process.o output/common-Frozen.o output/common-Logger.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

#Limit
output/plugins-limit-Api.o: plugins/limit/Api.c \
	common/include/PluginInterface.h
	$(NETD_CC) $(CLIBFLAGS) $(LIMITFLAGS) -o $@ $<

output/plugins-limit-Core.o: plugins/limit/Core.c \
	common/include/Process.h
	$(NETD_CC) $(CLIBFLAGS) $(LIMITFLAGS) -o $@ $<

output/plugins/limit.so: output/plugins-limit-Api.o output/plugins-limit-Core.o \
	output/common-Process.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

#LoadBalancer
output/plugins-loadbalancer-Api.o: plugins/loadbalancer/Api.c \
	common/include/PluginInterface.h common/include/Frozen.h
	$(NETD_CC) $(CLIBFLAGS) $(LOADBALANCERFLAGS) -o $@ $<

output/plugins-loadbalancer-Core.o: plugins/loadbalancer/Core.c \
	common/include/File.h
	$(NETD_CC) $(CLIBFLAGS) $(LOADBALANCERFLAGS) -o $@ $<

output/plugins/loadbalancer.so: output/plugins-loadbalancer-Api.o output/plugins-loadbalancer-Core.o \
	output/common-File.o output/common-Frozen.o output/common-Logger.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

#Route
output/plugins-route-Api.o: plugins/route/Api.c \
	common/include/PluginInterface.h
	$(NETD_CC) $(CLIBFLAGS) $(ROUTEFLAGS) -o $@ $<

output/plugins-route-Core.o: plugins/route/Core.c \
	common/include/File.h
	$(NETD_CC) $(CLIBFLAGS) $(ROUTEFLAGS) -o $@ $<

output/plugins/route.so: output/plugins-route-Api.o output/plugins-route-Core.o \
	output/common-File.o output/common-Logger.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

