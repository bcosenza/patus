#
# Makefile for Patus stencil benchmark. 
#
# Note: $(PATUS_*) variables will be automatically replaced by the
# required runtime files by Patus.
#
# gcc for natiove copilation, arm-linux-gnueabi-gcc for cross compilation on x86 to targe ARM
#

UNAME_S := $(shell uname -s)
ifeq ($(UNAME_S), Darwin)
	CC = /usr/local/bin/gcc
	GNUPLOT = /Applications/Gnuplot.app/Contents/Resources/bin/gnuplot
else
	CC = arm-linux-gnueabihf-gcc
	GNUPLOT = gnuplot
endif

CFLAGS = -O3 -fopenmp -mfpu=neon -I.


bench: kernel.o driver.o $(PATUS_RUNTIME_OBJECT_FILES)
	$(CC) $(CFLAGS) -o $@ $+ -lm

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

tune:
$(PATUS_TEST_NONAUTOTUNE_EXE_PARAMS)
	$(PATUS_AUTOTUNER) bench $(PATUS_EXE_PARAMS) | tee `date +tune_%F_%H.%M.%S.txt`

plot:
	for f in *.data; do echo "set term png; set view map; splot \"$$f\" matrix with image" | $(GNUPLOT) > $${f%.data}.png; done

clean:
	rm -rf *.o bench