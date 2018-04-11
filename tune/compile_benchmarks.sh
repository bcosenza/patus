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

stencil_name="blur.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 8 8 8 2 
cd ../..

stencil_name="wave-1.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 64 64 64 16 16 16 8 2:2 
cd ../..

stencil_name="wave-2.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path  --architecture=$arch 
cd temp/$stencil_name
make
./bench 64 64 64 16 16 16 8 2 
cd ../..


stencil_name="edge.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 16 16 8 2 
cd ../..

stencil_name="game-of-life.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch --validation-tolerance=1e-3
cd temp/$stencil_name
make
./bench 62 64 16 16 8 2 
cd ../..

stencil_name="divergence.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 16 8 8 8 8 2 
cd ../..

stencil_name="gradient.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 16 8 8 8 8 2 
cd ../..

stencil_name="laplacian.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 64 16 16 16 8 2 
# NOTE(Biagio) prefething not specified with x86_64 AVX
#  prefething of 8 gave me an seg. fault
cd ../..

stencil_name="laplacian6.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path  --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 64 16 16 16 8 2 
cd ../..

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

stencil_name="tricubic.stc"
stencil_path="dsl/$stencil_name"
echo
echo $stencil_name
patus --outdir=temp/$stencil_name --strategy=$strategy --unroll=$unroll  --stencil=$stencil_path --architecture=$arch 
cd temp/$stencil_name
make
./bench 62 64 32 16 16 8 8 2 
cd ../..
