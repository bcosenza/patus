#!/bin/bash

echo "SVM-Rank Training and evaluation of test benchmarks."


# INPUT  
#   trainin set: temp/training*.txt

# OUTPUT 
#   for each model configuration, 
#     - a model file in: data/model_*.model
#     - a folder: data/model_* with
#       - for each executed test case (stencil,size) a  *.best and *.pred file
#       - 


# C, trade-off between training error and margin
#CVALUES="64 32 16 8 4 2 1 0.5 0.25 0.125 0.0625 0.03125 0.015625 0.0078125 0.00390625 0.001953125 0.0009765625"
CVALUES="0.01"

# kernel type: linear, polynomial, radial basis function, sigmoid tanh 
#KTYPES="0 1 2 3"
KTYPES="0"
KDESCR[0]="linear"
KDESCR[1]="polynomial" 
KDESCR[2]="radial-basis-function"
KDESCR[3]="sigmoid-tanh"

# creating features from the test set (this should have been already run by Makefile)
java -cp jar/stenciltune.jar stenciltune.ExampleGenerator

# for each training file
#f=temp/training*.txt
for f in temp/training*.txt
do
  temp=$(wc -l $f)
  temp=($temp)             # temp is now an array
  training_size=${temp[0]} # we get the first element of the array (the actual training size)
  echo "--- --- ---  Dataset: $f ($training_size)  --- --- ---  "
  
  
  for MODC in $CVALUES
  do 
    for MODK in $KTYPES
    do 
      modelfile="data/model_${MODC}_${MODK}_${training_size}.model"
      echo "--- --- ---  model training with c = $MODC, kernel type = $MODK ($KDESCR[$MODK]) and size ${training_size}"
      time ./svm_rank/svm_rank_learn -c $MODC -t $MODK $f $modelfile      
  
      echo "--- --- ---  model evaluation with all test benchmarks"
      testfolder="data/model_${MODC}_${MODK}_${training_size}"
      echo "creating the testing folder: $testfolder"
      mkdir -p "$testfolder"      
      rm -rf ${testfolder}/*
      
      
        # a summary of all best-ranked performance for such dataset will be saved in this file
	summaryfile="${testfolder}/summary.txt"  
	truncate -s 0 $summaryfile 

      
      for tb in data/*.test
      do      
        tbfilename=$(basename ${tb} .test)
        predfile="$testfolder/${tbfilename}.pred"
        echo ""
        echo "*** classification for $tbfilename"
        # time
        ./svm_rank/svm_rank_classify  $tb  $modelfile  $predfile
        
        ### result elaboration ###
        
        # sort results and extracts first 10 values
        first10=$(cat $predfile | sort -n | head)        
        # for each predicted value, we print the original configuration
        count=1   
        echo "Ranking, first 10:"
        for CONF in $first10
        do
          # get the line number from the prediction file (grep also returns the pattern, so we need to cut it)
          lineno=$(grep $predfile -e $CONF -n | cut -f1 -d:)
          # extract that line from the *.test file
          line=$(head -$lineno $tb  | tail -1)          
          echo "    ( $count, $CONF ) #$lineno => ${line##*# }"
          if [[ count -eq 1 ]]; then best=${line##*# }; fi
          (( count ++ ))
        done                
        
        echo "$tbfilename c $MODC kerneltype $MODK param $best" > "$testfolder/${tbfilename}.best"

        # sort results and extracts first 10 values
        last10=$(cat $predfile | sort -n -r | head)        
        # for each predicted value, we print the original configuration
        count=1   
        echo "Ranking, last 10:"
        for CONF in $last10
        do
          # get the line number from the prediction file (grep also returns the pattern, so we need to cut it)
          lineno=$(grep $predfile -e $CONF -n | cut -f1 -d:)
          # extract that line from the *.test file
          line=$(head -$lineno $tb  | tail -1)          
          echo "    ( $count, $CONF ) #$lineno => ${line##*# }"
          (( count ++ ))
        done                               

        # execution
        kernelname=${tbfilename%%_*}
        best_nospace=${param// /_}
        #kerneloutfile=${testfolder}/${kernelname}_${best_nospace}.txt
        kerneloutfile=${testfolder}/${tbfilename}.out        
        echo "Execute the best-ranked for $kernelname with param $best"         
#        temp/${kernelname}.stc/bench ${best} 
        temp/${kernelname}.stc/bench ${best} > ${kerneloutfile}       
                                    
        # extract the timing (line number 8)
        runtime=$(cat $kerneloutfile | sed -n 8p)
        performance=$(cat $kerneloutfile  | sed -n 6p | cut -d' ' -f12)
        echo "$runtime $performance $kerneloutfile"
        echo "$tbfilename ${MODC} ${MODK} $runtime $performance" >> $summaryfile        
      done
      echo " "
    done
  done
done

echo "evaluation complete in $SECONDS sec"
echo ""


