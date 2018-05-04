# This scripts compile the Patus compiler using the Java compiler


# Uncomment and edit the following in case you gnuplot and java binaries are not in the PATH.
#GNUPLOT="/usr/bin/gnuplot"
#PATH=$PATH:/usr/lib/jvm/java-8-oracle/bin

echo Configuration of PATUS

# The following does not compile Patus anew, but takes the build from the main folder.
# If you have a newer o modified version of Patus, you have to replace /tune/jar/patus.jar with it
#echo Copying the PATUS jar from the main folder to /tune/jar
#cp ../bin/patus_nologs.jar ./jar/patus.jar


# set benchmark folder using the absolute path
export STENCIL_BENCHMARK=$(readlink ../benchmark/ -f)
echo Setting STENCIL_BENCHMARK to $STENCIL_BENCHMARK
### list of 2d kernels
export stencil_2d_set="hinterp-double.stc hinterp-float.stc vinterp-float.stc vinterp-double.stc edge-double.stc edge-float.stc game-of-life-double.stc game-of-life-float.stc blur-float.stc  blur-double.stc"
### list of 3d kernels
export stencil_3d_set="gradient-float.stc gradient-double.stc laplacian-float.stc laplacian-double.stc laplacian6-float.stc laplacian6-double.stc divergence-double.stc divergence-float.stc wave-1-double.stc wave-1-float.stc"
# Warning: there following two codes take (overall) about 5-7 hours for compilation only: tricubic-float.stc tricubic-double.stc
# the reasons seems relate to the "sum" keyword, used for reduction, which does not scale nicely with the number of stencil points (Note by Biagio: this is my guess, but more evidence is necessary).
# Also two more codes use "sum" and have, therefore, been removed: wave-2-double.stc wave-2-float.stc


# Set the target architecture.
# If the architecture is not specified, by default x86_64 AVX is used.
# To enalbe the ARM backend, use the flag "ARM_NEON"
echo Patus backend configuration
# no argument provided
if [ $# -eq 0 ]
  then
    PATUS_ARCH="x86_64 SSE";
  else
    PATUS_ARCH=$1;
fi
echo Setting PATUS_ARCH to $PATUS_ARCH
export PATUS_ARCH


echo "2D kernels: $stencil_2d_set"
echo "3D kernels: $stencil_3d_set"


# setup Patus binary
cd ..
source util/patusvars.sh
cd tune

echo Done


