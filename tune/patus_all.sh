#!/bin/bash

# set default parameters
arch=$PATUS_ARCH
strategy="$PATUS_HOME/strategy/cacheblocked.stg"
#outdir="$PWD/out"
unroll="1,2,4,8"
#create_inline_asm="yes"

# accuracy tolerance
# --validation-tolerance=1e-3


### ------------------------------------------------------------ ###
### Check if the variables ARCH and BENCHMAKRS have been tested

if [ -z ${PATUS_ARCH+x} ]; then 
  echo "Error: PATUS_ARCH is unset. Have you ran configure_patus.sh?"
  exit 1; 
else echo "PATUS_ARCH is set to '$PATUS_ARCH'"; 
fi

if [ -z ${STENCIL_BENCHMARK+x} ]; then 
  echo "Warning STENCIL_BENCHMARK is unset. Have you ran configure_patus.sh?" 
  exit 1;
else echo "STENCIL_BENCHMARK is set to '$STENCIL_BENCHMARK'"; 
fi


### ------------------------------------------------------------ ###
### Compile and executes a some stencil benchmarks 

stencil_name="blur-float.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 8 8 8 2 
cd ../..

stencil_name="wave-1-float.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 64 64 64 16 16 16 8 2:2 
cd ../..

stencil_name="wave-2-double.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path  --architecture=$arch 
cd temp/$stencil_name
make
./bench 64 64 64 16 16 16 8 2 
cd ../..


stencil_name="edge-float.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 16 16 8 2 
cd ../..

stencil_name="game-of-life-float.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch --validation-tolerance=1e-3
cd temp/$stencil_name
make
./bench 62 64 16 16 8 2 
cd ../..

stencil_name="divergence-double.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 16 8 8 8 8 2 
cd ../..

stencil_name="gradient-double.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 16 8 8 8 8 2 
cd ../..

stencil_name="laplacian-double.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 64 16 16 16 8 2 
# NOTE(Biagio) prefething not specified with x86_64 AVX
#  prefething of 8 gave me an seg. fault
cd ../..

stencil_name="laplacian6-float.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path  --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 64 16 16 16 8 2 
cd ../..

stencil_name="hinterp.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 8 8 8 2 
cd ../..

stencil_name="vinterp-float.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 8 8 8 2 
cd ../..

stencil_name="tricubic-double.stc"
stencil_path="$STENCIL_BENCHMARK/$stencil_name"
echo
echo $stencil_name
time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 32 16 16 8 8 2 
cd ../..
