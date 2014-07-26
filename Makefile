# Top level makefile

default: all

.DEFAULT:
	cd src && $(MAKE) $@

install:
	cd src && $(MAKE) $@

.PHONY: install
