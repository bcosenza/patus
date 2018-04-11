#!/bin/bash

# several jar archives are required to be in the classpath in order
# to trigger the stencil optimizations
# these jar files are:
# (1) jmetal4.5.jar
# (2) patus.jar
# (3) log4j-1.2.16.jar
# (4) TableLayout.jar
export CLASSPATH=$CLASSPATH:$PWD/jar/jmetal4.5.jar:$PWD/jar/patus.jar:$PWD/jar/log4j-1.2.16.jar:$PWD/jar/TableLayout.jar


# besides the classpath other environment variables require to be set
export BLOCKING_MIN=1
export BLOCKING_MAX=4

export MIN_UNROLL_FACTOR=0
export MAX_UNROLL_FACTOR=8

export DSL_SRC=./dsl
export DSL_BUILD=./temp

export MAX_EVALUATIONS=1024

#---------------------------------------------------------------- #
# the variable STENCIL_HOME should contain the path to the folder where
# the stencil code has been compiled (there should be a bench executable
# there)
echo "optimizing blur"
stencilname="blur.stc"
export STENCIL_HOME=$DSL_BUILD/blur.stc
SIZE="1024 768"
for sx in $SIZE 
do 
  input="1024 $sx" # this 1024 is not an error! check Table 4 in the paper! :D
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done



echo "optimizing wave-1"
stencilname="wave-1.stc"
export STENCIL_HOME=$DSL_BUILD/wave-1.stc
SIZE="64 128 256"
for sx in $SIZE 
do 
  input="$sx $sx $sx"
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


#echo "optimizing wave-2"
#export STENCIL_HOME=$DSL_BUILD/wave-2.stc
#java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/wave-2.stc 512 512 512
#
#echo "running a genetic algorithm"
#java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/wave-2.stc 128 128 128
#echo "running a PSO"
#java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/wave-2.stc 128 128 128
#echo "running a differential evolution"
#java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/wave-2.stc 128 128 128
#echo "running a evolutive strategy"
#java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/wave-2.stc 128 128 128
#echo "running cMAES"
#java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/wave-2.stc 128 128 128
#echo "running ssGA"
#java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/wave-2.stc 128 128 128



echo "optimizing tricubic"
stencilname="tricubic.stc"
export STENCIL_HOME=$DSL_BUILD/tricubic.stc
SIZE="128 256"
for sx in $SIZE 
do 
  input="$sx $sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing edge"
stencilname="edge.stc"
export STENCIL_HOME=$DSL_BUILD/edge.stc
SIZE="512 1024"
for sx in $SIZE 
do 
  input="$sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing game-of-life"
stencilname="game-of-life.stc"
export STENCIL_HOME=$DSL_BUILD/game-of-life.stc
SIZE="512 1024"
for sx in $SIZE 
do 
  input="$sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing divergence"
stencilname="divergence.stc"
export STENCIL_HOME=$DSL_BUILD/divergence.stc
SIZE="64 128"
for sx in $SIZE 
do 
  input="$sx $sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing gradient"
stencilname="gradient.stc"
export STENCIL_HOME=$DSL_BUILD/gradient.stc
SIZE="128 256"
for sx in $SIZE 
do 
  input="$sx $sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing laplacian"
stencilname="laplacian.stc"
export STENCIL_HOME=$DSL_BUILD/laplacian.stc
SIZE="128 256"
for sx in $SIZE 
do 
  input="$sx $sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing laplacian6"
stencilname="laplacian6.stc"
export STENCIL_HOME=$DSL_BUILD/laplacian6.stc
SIZE="128 256"
for sx in $SIZE 
do 
  input="$sx $sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing hinterp"
stencilname="hinterp.stc"
export STENCIL_HOME=$DSL_BUILD/hinterp.stc
SIZE="1024"
for sx in $SIZE 
do 
  input="$sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done


echo "optimizing vinterp"
stencilname="vinterp.stc"
export STENCIL_HOME=$DSL_BUILD/vinterp.stc
SIZE="1024"
for sx in $SIZE 
do 
  input="$sx $sx"  
  echo $input
  echo "running a genetic algorithm"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.GA_main $DSL_SRC/$stencilname $input
#  echo "running a PSO"
#  java jmetal.metaheuristics.singleObjective.particleSwarmOptimization.PSO_main $DSL_SRC/$stencilname $input
  echo "running a differential evolution"
  java jmetal.metaheuristics.singleObjective.differentialEvolution.DE_main $DSL_SRC/$stencilname $input
  echo "running a evolutive strategy"
  java jmetal.metaheuristics.singleObjective.evolutionStrategy.ES_main $DSL_SRC/$stencilname $input
#  echo "running cMAES"
#  java jmetal.metaheuristics.singleObjective.cmaes.CMAES_main $DSL_SRC/$stencilname $input
  echo "running ssGA"
  java jmetal.metaheuristics.singleObjective.geneticAlgorithm.sGA_main $DSL_SRC/$stencilname $input
done





