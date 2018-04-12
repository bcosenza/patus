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

# Set the target architecture
# if the architecture is not specified, by default x86_64 AVX is used
# if the parameter ARM is provided, 
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

# setup Patus binary
cd ..
source util/patusvars.sh
cd tune

echo Done


