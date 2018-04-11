#!/bin/bash

# Note: the code for this is in mch.unibas.cs.hpwc.patus.autotuner.MetaHeuristicOptimizer    

# set default parameters
stencil_spec=""
arch="x86_64 SSE asm"
arch_set="false"
verb="codegen"
strategy="$PATUS_HOME/strategy/cacheblocked.stg"
outdir="$PWD/out"
unroll="1,2,4:1:1"
create_inline_asm="yes"
moreargs=""

#java -jar $PATUS_HOME/bin/patus.jar $verb --stencil2=$stencil_spec --strategy=$strategy --outdir=$outdir --unroll=$unroll --architecture="$PATUS_HOME/arch/architectures.xml,$arch" $moreargs

stencil_path="$PATUS_HOME/examples/wave-1.stc"

# replaces spaces with - on the arch name
stencil_arch=${arch// /-}

stencil_name=$(basename $stencil_path)_$stencil_arch 

echo Automatic tuning of stencil kernel $stencil_name with arch $arch and strategy $strategy

# code generation
mkdir temp/$stencil_name
patus --outdir=temp/$stencil_name --strategy=$PATUS_HOME/strategy/cacheblocked.stg --unroll=$unroll  --stencil=$stencil_path

# kernel params
x_max=64
y_max=64
z_max=64
echo Autotuning max: $x_max $y_max $z_max 
cd temp/$stencil_name
make
make tune x_max=$x_max y_max=$y_max z_max=$z_max 
cd .. 

