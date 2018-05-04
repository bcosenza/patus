#!/bin/bash

# Compilation of all stencil kernels in temp/ml_stencil_code
# 
# Some 3d kernels requries a higher validation tolerance. In Patus, this wasused mainly for the Wave tex benchmark.
# We couldn't find a way to automatically define this threshold, therefore we used Patus' apporach with Wave.
# 

arch=$PATUS_ARCH
strategy="$PATUS_HOME/strategy/cacheblocked.stg"
#outdir="$PWD/out"
unroll="1,2,4,8"
#create_inline_asm="yes"
moreargs=""


for f in temp/ml_stencil_code/*.stc
do
	echo $f
	echo "compiling $f [Patus]"	 
	stencilname=$(basename $f)
	stencilout=temp/ml_stencil_build/$stencilname

	# create output directory
	mkdir -p $stencilout

	time patus --outdir=$stencilout --strategy=$strategy --unroll=$unroll  --stencil=$f --architecture=$arch --validation-tolerance=1e-2 

	echo "compiling $f [gcc]"
	cd $stencilout
	time make
	cd ../../..
done

echo "Build for each stencil code in temp/ml_stencil_build/<stencilname>"
echo "stencil DSL compilation in $SECONDS sec"
