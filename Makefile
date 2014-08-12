# Top level makefile

all: install doc

install:
	cd src && $(MAKE) $@

doc:
	cd doc && $(MAKE) $@

clean:
	cd src && $(MAKE) $@
	cd doc && $(MAKE) $@

.PHONY: install doc clean
