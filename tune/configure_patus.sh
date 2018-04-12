# This scripts compile the Patus compiler using the Java compiler


# Uncomment and edit the following in case you gnuplot and java binaries are not in the PATH.
#GNUPLOT="/usr/bin/gnuplot"
#PATH=$PATH:/usr/lib/jvm/java-8-oracle/bin

echo Configuration of PATUS

# The following does not compile Patus anew, but takes the build from the main folder.
# If you have a newer o modified version of Patus, you have to replace /tune/jar/patus.jar with it
echo Copying the PATUS jar from the main folder to /tune/jar
cp ../bin/patus.jar ./jar/patus.jar


# set benchmark folder
export STENCIL_BENCHMARK=../benchmark

# setup Patus binary
cd ..
source util/patusvars.sh
cd tune

echo Done


