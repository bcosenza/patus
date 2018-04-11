#!/bin/bash

# Create a training data set file that can be processed by SVM rank

# calculating the total number of lines (runs)
let "line=0"
for f in temp/ml_result_sorted/ranked_*.txt
do	 
	t=$(wc -l < $f )
	let line=line+t
done

echo lines $line

# the final output will be stored in the training file
trainingfile=temp/training_size_${line}.txt
truncate -s 0 $trainingfile

#for f in temp/ml_result_sorted/ranked_*.txt
for f in $(ls -v temp/ml_result_sorted/ranked_*.txt)
do 
	cat $f | nl --number-format=ln >> $trainingfile
done


# create a folder for Kendal's values
mkdir -p "data/kendalltemp"

echo

CVALUES="64 32 16 8 4 2 1 0.5 0.25 0.125 0.0625 0.03125 0.015625 0.0078125 0.00390625 0.001953125 0.0009765625"

for cval in $CVALUES
do 
	echo "testing C=${cval}"
	modelname="data/kendalltemp/training_C_${cval}_size_${line}.model"
	echo "quick model creation for Kendall's tau evaluation for $modelname for training set $trainingfile"
	./svm_rank/svm_rank_learn -c ${cval} -t 0 ${trainingfile} ${modelname}
	
	# compute kendall's tau
	python ./compute_kendall.py $modelname
done

echo
echo pre-processed results have been saved in $trainingfile
