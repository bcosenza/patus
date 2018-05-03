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
### Compile and executes all stencil benchmarks

### list of 2d kernels
stencil_2d_set="hinterp-double.stc hinterp-float.stc vinterp-float.stc vinterp-double.stc edge-double.stc edge-float.stc game-of-life-double.stc game-of-life-float.stc blur-float.stc  blur-double.stc"

### list of 3d kernels
stencil_3d_set="gradient-float.stc gradient-double.stc laplacian-float.stc laplacian-double.stc laplacian6-float.stc laplacian6-double.stc divergence-double.stc divergence-float.stc wave-1-double.stc wave-1-float.stc wave-2-double.stc wave-2-float.stc"
# Warning: there following two codes take (overall) about 5-7 hours for compilation only.
# tricubic-float.stc tricubic-double.stc

echo
echo double compilation of 2d stencil benchmarks
echo

for stencil_name in $stencil_2d_set
do 
  stencil_path="$STENCIL_BENCHMARK/$stencil_name"
  echo
  echo $stencil_name
  time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
  cd temp/$stencil_name
  time make
  ./bench 64 64 8 8 8 2 
  cd ../..
done

echo
echo double compilation of 3d stencil benchmarks
echo

for stencil_name in $stencil_3d_set
do 
  stencil_path="$STENCIL_BENCHMARK/$stencil_name"
  echo
  echo $stencil_name
  time patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
  cd temp/$stencil_name
  time make
  ./bench 64 64 64 16 16 16 8 2 
  cd ../..
done

echo
echo "compilation complete"

