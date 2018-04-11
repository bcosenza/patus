#!/bin/bash

# Autotune all with Patus
echo Autotune with Patus

mkdir -p patus_temp
cd temp

cd blur.stc
make tune width=1024 height=1024 > ../../patus_temp/blur_1024_1024.tune
make tune width=1024 height=768  > ../../patus_temp/blur_1024_768.tune
cd ..

cd wave-1.stc
make tune x_max=64  y_max=64  z_max=64  > ../../patus_temp/wave-1_64_64_64.tune
make tune x_max=128 y_max=128 z_max=128 > ../../patus_temp/wave-1_128_128_128.tune
make tune x_max=256 y_max=256 z_max=256 > ../../patus_temp/wave-1_256_256_256.tune
cd ..

cd tricubic.stc
make tune x_max=128 y_max=128 z_max=128 > ../../patus_temp/tricubic_128_128_128.tune
make tune x_max=256 y_max=256 z_max=256 > ../../patus_temp/tricubic_256_256_256.tune
cd ..

cd edge.stc
make tune width=512 height=512   > ../../patus_temp/edge_512_512.tune
make tune width=1024 height=1024 > ../../patus_temp/edge_1024_1024.tune
cd ..

cd game-of-life.stc
make tune width=512 height=512   > ../../patus_temp/game-of-life_512_512.tune
make tune width=1024 height=1024 > ../../patus_temp/game-of-life_1024_1024.tune
cd ..

cd divergence.stc
make tune x_max=64  y_max=64  z_max=64  > ../../patus_temp/divergence_64_64_64.tune
make tune x_max=128 y_max=128 z_max=128 > ../../patus_temp/divergence_128_128_128.tune
make tune x_max=256 y_max=256 z_max=256 > ../../patus_temp/divergence_256_256_256.tune
cd ..

cd gradient.stc
make tune x_max=128 y_max=128 z_max=128 > ../../patus_temp/gradient_128_128_128.tune
make tune x_max=256 y_max=256 z_max=256 > ../../patus_temp/gradient_256_256_256.tune
cd ..

cd laplacian.stc
make tune x_max=128 y_max=128 z_max=128 > ../../patus_temp/laplacian_128_128_128.tune
make tune x_max=256 y_max=256 z_max=256 > ../../patus_temp/laplacian_256_256_256.tune
cd ..

cd laplacian6.stc
make tune x_max=128 y_max=128 z_max=128 > ../../patus_temp/laplacian6_128_128_128.tune
make tune x_max=256 y_max=256 z_max=256 > ../../patus_temp/laplacian6_256_256_256.tune
cd ..

cd hinterp.stc
make tune width=1024 height=1024 > ../../patus_temp/hinterp_1024_1024.tune
cd ..

cd vinterp.stc
make tune width=1024 height=1024 > ../../patus_temp/vinterp_1024_1024.tune
cd ..

echo Output:
ls ../patus_temp/

