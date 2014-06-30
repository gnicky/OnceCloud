CC=gcc
LD=gcc
CFLAGS=-Wall -Werror -c
LDFLAGS=-Wall -Werror

SETUP=output/setup.o \
	output/setup

all: prepare everything

everything: output/setup

setup: output/setup

prepare:
	if [ ! -d output ]; then mkdir output; fi

clean:
	rm -rf $(SETUP)
	rm -rf output

.PHONY: all everything prepare setup

#Setup
output/setup.o: setup/Main.c
	$(CC) $(CFLAGS) -o $@ $<

output/setup: output/setup.o
	$(LD) $(LDFLAGS) -o $@ $^
