#!/bin/bash

#
# Stencil execution and training execution with different sizes, to calculate the 'learning curve'
#
echo "learning curve evaluation (different size, same parameter model)"
echo "-- executions"


mkdir -p data/kendall
mkdir -p data/kendalltemp

#exlist=(15) # 4800 samples
exlist=(3 6 9 12 15 18 21 24 27 30 50 100)
#exlist=(5 10 25 50 100 150 200)

#for i in $( seq 10 15 100 ); do
#for i in $( seq 2 2 4 ); do
#for i in $( seq 10 90 500 ); do
for i in ${exlist[@]}
do
	# clean temporary files
	rm -rf temp/ml_execution_params temp/ml_result_raw temp/ml_result_qid temp/ml_result_sorted
	
	# new stencil execution
	j=$(($i * 2))
	bash ./ml_3_stencil_exec.sh $i $j
	
	# create the training file
	bash ./ml_4_prepare_training.sh
done

echo "stencil DSL total execution for all sizes: $SECONDS sec"

echo "-- training sets:"
ls temp/training_*.txt



#./ml_evaluate_learning_curve.sh 

