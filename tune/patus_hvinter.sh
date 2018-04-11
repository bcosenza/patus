#!/bin/bash

# set default parameters
stencil_spec=""
arch="x86_64 AVX"
#arch_set="false"
#verb="codegen"
strategy="$PATUS_HOME/strategy/cacheblocked.stg"
#outdir="$PWD/out"
unroll="1,2,4,8"
create_inline_asm="yes"
moreargs=""

# accuracy tolerance
# --validation-tolerance=1e-3

### ------------------------------------------------------------ ###

stencil_name="hinterp.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 8 8 8 2 
cd ../..


stencil_name="vinterp.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 8 8 8 2 
cd ../..

