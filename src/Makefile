# Net Daemon Makefile

CC=gcc
LD=gcc

NETD_CC=$(QUIET_CC)$(CC)
NETD_LD=$(QUIET_LD)$(LD)

CFLAGS=-g -Wall -Werror -c
CLIBFLAGS=-g -Wall -Werror -c -fpic
LDFLAGS=-g -Wall -Werror
LDLIBFLAGS=-g -Wall -Werror -shared -lc

DLFLAGS=-ldl
PTHREADFLAGS=-pthread

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

NETD_MAIN=netd/netd
NETD_PLUGINS=$(NETD_DHCP_PLUGIN) $(NETD_FIREWALL_PLUGIN) $(NETD_LIMIT_PLUGIN) \
	$(NETD_LOADBALANCER_PLUGIN) $(NETD_NAT_PLUGIN) $(NETD_ROUTE_PLUGIN) \
	$(NETD_PPTP_PLUGIN) $(NETD_OPENVPN_PLUGIN)

# Plugins
NETD_DHCP_PLUGIN=plugins/dhcp/dhcp.so
NETD_FIREWALL_PLUGIN=plugins/firewall/firewall.so
NETD_LIMIT_PLUGIN=plugins/limit/limit.so
NETD_LOADBALANCER_PLUGIN=plugins/loadbalancer/loadbalancer.so
NETD_NAT_PLUGIN=plugins/nat/nat.so
NETD_ROUTE_PLUGIN=plugins/route/route.so
NETD_PPTP_PLUGIN=plugins/pptp/pptp.so
NETD_OPENVPN_PLUGIN=plugins/openvpn/openvpn.so

NETD_MAIN_INCLUDE=-Inetd/include/
NETD_COMMON_INCLUDE=-Icommon/include/

# Plugins
NETD_DHCP_PLUGIN_INCLUDE=-Iplugins/dhcp/include/
NETD_FIREWALL_PLUGIN_INCLUDE=-Iplugins/firewall/include/
NETD_LIMIT_PLUGIN_INCLUDE=-Iplugins/limit/include/
NETD_LOADBALANCER_PLUGIN_INCLUDE=-Iplugins/loadbalancer/include/
NETD_NAT_PLUGIN_INCLUDE=-Iplugins/nat/include/
NETD_ROUTE_PLUGIN_INCLUDE=-Iplugins/route/include/
NETD_PPTP_PLUGIN_INCLUDE=-Iplugins/pptp/include/
NETD_OPENVPN_PLUGIN_INCLUDE=-Iplugins/openvpn/include/

NETD_OBJECTS=$(NETD_MAIN_OBJECTS) $(NETD_COMMON_OBJECTS) \
	$(NETD_DHCP_PLUGIN_OBJECTS) $(NETD_FIREWALL_PLUGIN_OBJECTS) $(NETD_LIMIT_PLUGIN_OBJECTS) \
	$(NETD_LOADBALANCER_PLUGIN_OBJECTS) $(NETD_NAT_PLUGIN_OBJECTS) $(NETD_ROUTE_PLUGIN_OBJECTS) \
	$(NETD_PPTP_PLUGIN_OBJECTS) $(NETD_OPENVPN_PLUGIN_OBJECTS)

all: everything api
.PHONY: all

everything: $(NETD_MAIN) $(NETD_PLUGINS)

main: $(NETD_MAIN)
.PHONY: main

# Plugins
plugins: $(NETD_PLUGINS)
.PHONY: plugins

clean:
	rm -rf $(NETD_OBJECTS)
	rm -rf $(NETD_MAIN)
	rm -rf $(NETD_PLUGINS)
	cd api && python setup.py clean
.PHONY: clean

api:
	cd api && python setup.py build
.PHONY: api

PREFIX=/usr/local
INSTALL_BIN=$(PREFIX)/netd
INSTALL=install

install: all
	mkdir -p $(INSTALL_BIN)
	mkdir -p $(INSTALL_BIN)/sbin
	mkdir -p $(INSTALL_BIN)/plugins
	mkdir -p $(INSTALL_BIN)/log
	$(INSTALL) netd/netd $(INSTALL_BIN)/sbin
	$(INSTALL) $(NETD_PLUGINS) $(INSTALL_BIN)/plugins
	chmod +x $(INSTALL_BIN)/sbin/netd
	ln -sf $(INSTALL_BIN)/sbin/netd /bin/netd
	cd api && python setup.py install

depend:
	@echo "# Dependencies"
	@echo "# Netd Main"
	@$(CC) -MM netd/*.c $(NETD_COMMON_INCLUDE) $(NETD_MAIN_INCLUDE)
	@echo "# Common"
	@$(CC) -MM common/*.c $(NETD_COMMON_INCLUDE)
	# Plugins
	@echo "# DHCP Plugin"
	@$(CC) -MM plugins/dhcp/*.c $(NETD_COMMON_INCLUDE) $(NETD_DHCP_PLUGIN_INCLUDE)
	@echo "# Firewall Plugin"
	@$(CC) -MM plugins/firewall/*.c $(NETD_COMMON_INCLUDE) $(NETD_FIREWALL_PLUGIN_INCLUDE)
	@echo "# Limit Plugin"
	@$(CC) -MM plugins/limit/*.c $(NETD_COMMON_INCLUDE) $(NETD_LIMIT_PLUGIN_INCLUDE)
	@echo "# Loadbalancer Plugin"
	@$(CC) -MM plugins/loadbalancer/*.c $(NETD_COMMON_INCLUDE) $(NETD_LOADBALANCER_PLUGIN_INCLUDE)
	@echo "# Nat Plugin"
	@$(CC) -MM plugins/nat/*.c $(NETD_COMMON_INCLUDE) $(NETD_NAT_PLUGIN_INCLUDE)
	@echo "# Route Plugin"
	@$(CC) -MM plugins/route/*.c $(NETD_COMMON_INCLUDE) $(NETD_ROUTE_PLUGIN_INCLUDE)

# Dependencies
# Netd Main
netd/HttpHelper.o: netd/HttpHelper.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_MAIN_INCLUDE) $(PTHREADFLAGS) -o $@ $<

netd/Main.o: netd/Main.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_MAIN_INCLUDE) $(PTHREADFLAGS) -o $@ $<

netd/PluginManager.o: netd/PluginManager.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_MAIN_INCLUDE) $(PTHREADFLAGS) -o $@ $<

NETD_MAIN_OBJECTS=netd/HttpHelper.o netd/Main.o netd/PluginManager.o

netd/netd: netd/HttpHelper.o netd/Main.o netd/PluginManager.o \
	common/File.o common/Logger.o common/Mongoose.o
	$(NETD_LD) $(LDFLAGS) $(DLFLAGS) $(PTHREADFLAGS) -o $@ $^

# Common
common/File.o: common/File.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) -o $@ $<	

common/Frozen.o: common/Frozen.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) -o $@ $<	

common/Logger.o: common/Logger.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) -o $@ $<	

common/Mongoose.o: common/Mongoose.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) -o $@ $<	

common/Process.o: common/Process.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) -o $@ $<	

common/String.o: common/String.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) -o $@ $<	

NETD_COMMON_OBJECTS=common/File.o common/Frozen.o common/Logger.o \
	common/Mongoose.o common/Process.o common/String.o

# Plugins
# DHCP Plugin
plugins/dhcp/Api.o: plugins/dhcp/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_DHCP_PLUGIN_INCLUDE) -o $@ $<	

plugins/dhcp/Core.o: plugins/dhcp/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_DHCP_PLUGIN_INCLUDE) -o $@ $<	

plugins/dhcp/Parser.o: plugins/dhcp/Parser.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_DHCP_PLUGIN_INCLUDE) -o $@ $<	

NETD_DHCP_PLUGIN_OBJECTS=plugins/dhcp/Api.o plugins/dhcp/Core.o plugins/dhcp/Parser.o

plugins/dhcp/dhcp.so: plugins/dhcp/Api.o plugins/dhcp/Core.o plugins/dhcp/Parser.o \
	common/File.o common/Logger.o common/Process.o common/String.o common/Frozen.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# Firewall Plugin
plugins/firewall/Api.o: plugins/firewall/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_FIREWALL_PLUGIN_INCLUDE) -o $@ $<

plugins/firewall/Core.o: plugins/firewall/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_FIREWALL_PLUGIN_INCLUDE) -o $@ $<

NETD_FIREWALL_PLUGIN_OBJECTS=plugins/firewall/Api.o plugins/firewall/Core.o

plugins/firewall/firewall.so: plugins/firewall/Api.o plugins/firewall/Core.o \
	common/File.o common/Process.o common/Logger.o common/String.o common/Frozen.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# Limit Plugin
plugins/limit/Api.o: plugins/limit/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_LIMIT_PLUGIN_INCLUDE) -o $@ $<

plugins/limit/Core.o: plugins/limit/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_LIMIT_PLUGIN_INCLUDE) -o $@ $<

NETD_LIMIT_PLUGIN_OBJECTS=plugins/limit/Api.o plugins/limit/Core.o

plugins/limit/limit.so: plugins/limit/Api.o plugins/limit/Core.o \
	common/Process.o common/Logger.o common/String.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# Loadbalancer Plugin
plugins/loadbalancer/Api.o: plugins/loadbalancer/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_LOADBALANCER_PLUGIN_INCLUDE) -o $@ $<

plugins/loadbalancer/Core.o: plugins/loadbalancer/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_LOADBALANCER_PLUGIN_INCLUDE) -o $@ $<

NETD_LOADBALANCER_PLUGIN_OBJECTS=plugins/loadbalancer/Api.o plugins/loadbalancer/Core.o

plugins/loadbalancer/loadbalancer.so: plugins/loadbalancer/Api.o plugins/loadbalancer/Core.o \
	common/File.o common/Logger.o common/String.o common/Process.o common/Frozen.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# Nat Plugin
plugins/nat/Api.o: plugins/nat/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_NAT_PLUGIN_INCLUDE) -o $@ $<

plugins/nat/Core.o: plugins/nat/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_NAT_PLUGIN_INCLUDE) -o $@ $<

NETD_NAT_PLUGIN_OBJECTS=plugins/nat/Api.o plugins/nat/Core.o

plugins/nat/nat.so: plugins/nat/Api.o plugins/nat/Core.o \
	common/File.o common/Process.o common/Logger.o common/String.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# Route Plugin
plugins/route/Api.o: plugins/route/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_ROUTE_PLUGIN_INCLUDE) -o $@ $<

plugins/route/Core.o: plugins/route/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_ROUTE_PLUGIN_INCLUDE) -o $@ $<

NETD_ROUTE_PLUGIN_OBJECTS=plugins/route/Api.o plugins/route/Core.o

plugins/route/route.so: plugins/route/Api.o plugins/route/Core.o \
	common/File.o common/Logger.o common/String.o common/Process.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# PPTP Plugin
plugins/pptp/Api.o: plugins/pptp/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_PPTP_PLUGIN_INCLUDE) -o $@ $<

plugins/pptp/Core.o: plugins/pptp/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_PPTP_PLUGIN_INCLUDE) -o $@ $<

NETD_PPTP_PLUGIN_OBJECTS=plugins/pptp/Api.o plugins/pptp/Core.o

plugins/pptp/pptp.so: plugins/pptp/Api.o plugins/pptp/Core.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

# OpenVPN Plugin
plugins/openvpn/Api.o: plugins/openvpn/Api.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_OPENVPN_PLUGIN_INCLUDE) -o $@ $<

plugins/openvpn/Core.o: plugins/openvpn/Core.c
	$(NETD_CC) $(CLIBFLAGS) $(NETD_COMMON_INCLUDE) $(NETD_OPENVPN_PLUGIN_INCLUDE) -o $@ $<

NETD_OPENVPN_PLUGIN_OBJECTS=plugins/openvpn/Api.o plugins/openvpn/Core.o

plugins/openvpn/openvpn.so: plugins/openvpn/Api.o plugins/openvpn/Core.o
	$(NETD_LD) $(LDLIBFLAGS) -o $@ $^

