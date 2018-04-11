#!/bin/bash

#
# This is a time consuming script which executes all codes with different tuning configurations.
# This version allows to generate randomly generated configuration samples. 
# Encoding information are completely hidden by the Java program listed below. Details about the feature encoding can be found in feature_encoding.txt).
#
#   ml_3_stencil_exec.sh <N3d> <N2d>, where <N3D> is the number of execution for 3d kernels, and <N2D> for two-dimensional ones, for each (stencil,size) combination.
#
#   ml_3_stencil_exec.sh small (default) same as <exec_command> 200 300
#   ml_3_stencil_exec.sh medium
#   ml_3_stencil_exec.sh big 
# 

# INPUT  
#   stencil codes: temp/ml_stencil_code
#   stencil build: temp/ml_stencil_build
# OUTPUT 
#   for each code, a file with the execution params: temp/ml_execution_params
#   for each run, raw results:               temp/ml_result_raw
#   for each qid, runtime results:           temp/ml_result_qid
#   for each qid, sorted runtime results:    temp/ml_result_sorted

arch="x86_64 AVX"
strategy="$PATUS_HOME/strategy/cacheblocked.stg"
create_inline_asm="yes"
script="java -cp jar/stenciltune.jar stenciltune.ExecutionParametersGenerator"


#########################
# Dataset size tuning 
if [[ $1 == "small" ]]
then
  echo "small dataset size"  
  SAMPLE2D=10
  SAMPLE3D=20
elif  [[ $1 == "medium" ]]
then
  echo "medium dataset size"    
  SAMPLE2D=20
  SAMPLE3D=40
elif  [[ $1 == "big" ]]
then
  echo "big dataset size"  
  SAMPLE2D=30
  SAMPLE3D=60
elif [[ $1 =~ ^[-+]?[0-9]+$ ]] # is a number?
then  
  SAMPLE2D=$1  
  SAMPLE3D=$2
else
  echo "Please specify the dataset size!"
  echo "... <small|medium|big> or <2d> <3d>"
  exit 1
fi
# couting the number of executions

#  SIZE3="64 128 256 512" 
#  SIZE2="64 128 256 512 1024 2048 4096"
SIZE3="64 128 256" 
SIZE2="256 512 1024 2048"


echo dataset param $SAMPLE2D $SAMPLE3D

howmany() { echo $#; }
#STENCIL3DSIZE="$((  $(howmany $SIZE3) * $(howmany $CB3) * $(howmany $CHUNK) * $(howmany $UNROLL3) ))"
#STENCIL2DSIZE="$((  $(howmany $SIZE2) * $(howmany $CB2) * $(howmany $CHUNK) * $(howmany $UNROLL2) ))"
#echo "3d stencil execs $STENCIL3DSIZE"
#echo "2d stencil execs $STENCIL2DSIZE" 
#########################


#creating temporary folder for benchmark runs output (and cleaining it, if it was already there)
mkdir -p temp/ml_execution_params
rm temp/ml_execution_params/*
mkdir -p temp/ml_result_raw
rm temp/ml_result_raw/*
mkdir -p temp/ml_result_qid
rm temp/ml_result_qid/*
mkdir -p temp/ml_result_sorted
rm temp/ml_result_sorted/*


# qid is a counter used to indentify the ranking context (i.e., the query in Thorsten's ranking system)
let "qid=1"

# number of execution
declare -i execution
execution=0

# number of errors
declare -i error
error=0

# errors will be written in this file
echo "Error log" > temp/ml_3_error_log.txt


# for each stencil code 
for f in temp/ml_stencil_code/*.stc
do 
    stencilname=$(basename $f .stc)
    stencilbuild=temp/ml_stencil_build/${stencilname}.stc
    QID_PRINT=$(printf "%05d" $qid ; echo)
    echo $qid

    SIZESTRING=""

    # if is a 2d stencil
    if [[ $stencilname == *"2d"* ]]
    then
        SX=$SIZE2
        SY=$SIZE2
        SZ="1" 
	SAMPLE=$SAMPLE2D
        echo "2d executing $stencilname from $f for sizes $SIZE2"
    # if is a 3d stencil
    elif [[ $stencilname == *"3d"* ]]
    then
        SX=$SIZE3
        SY=$SIZE3
        SZ=$SIZE3
	SAMPLE=$SAMPLE3D
        echo "3d executing $stencilname from $f for sizes $SIZE3"
    else
        echo "Error: $stencilname not recognized (is it 3d or 2d?)"
        exit
    fi # 3d/2d stencil    
    

    # for each input size
    for sx in $SX
    do 
#        for sy in $SY
#        do
#            for sz in $SZ
#                do


                # XXX currently, only squared/cubic configurations are used (sx,sx,sx) or (sx,sx)
                sy=$sx
		if [[ $stencilname == *"2d"* ]]
		then
                  sz="1" # a 2d kernel will have z=1, i.e. (x,y,1), for both input size and blocking size
		  dim="2d"
		  SIZESTRING="${sx} ${sx}"
		else
		  sz=$sx
		  dim="3d"
		  SIZESTRING="${sx} ${sx} ${sx}"
                fi

                # we save each kernel run with the same (sizes) in a separete file 
                # as they will have the same qid (ranking context)
                resultfilename="${stencilname}_s${sx}_q${qid}.txt"

                # clean the qid-result file (in case you run it twice)	
                truncate -s 0 "temp/ml_result_qid/${resultfilename}"

                # extracting the vector representation of this stencil shape and buffer num
                stencilshape=$(cat $f | sed -n 3p)

                echo qid:${qid} - ${stencilname} - ${SIZESTRING}

                # all execution parameters are precalculated and saved in a file
		$script $SAMPLE ${SIZESTRING} > temp/ml_execution_params/${stencilname}_q${qid}.txt


                cd $stencilbuild 

		# for each execution (run)		
		while read line; do 
			runfilename="../../ml_result_raw/${stencilname}_q_${qid}_ex_${execution}.out"

			# do something with $line here
			delim=#			
			param=${line%$delim*}
                        # enc has the encoding of runtime parameters: input size [2223-2225] + tuning [2226-2230]
			enc=${line#*$delim}

			echo "running with size $SIZESTRING and param: $param"

			# execution
                        ./bench $param > $runfilename
                                    
                        # extract the timing (line number 8)
                        temp=$(cat $runfilename | sed -n 8p)

                        # extract the validation output (line number 9)
                        val=$(cat $runfilename | sed -n 9p)
			echo "$val --- $temp"
			if [[ $val != "Validation OK." ]]
			then
				cat $stencilname >> ../../ml_3_error_log.txt
				cat $runfilename >> ../../ml_3_error_log.txt
				let error+=1
			else
				let execution+=1
			fi

                        # append timing to the (qid) result file
                        echo $temp qid:$qid $stencilshape $enc >> ../../ml_result_qid/$resultfilename

		done < ../../ml_execution_params/${stencilname}_q${qid}.txt

		cd ../../..

                # sort values with the same qid
                cat temp/ml_result_qid/$resultfilename | sort -g > temp/ml_result_sorted/sorted_${qid}.txt
                truncate -s 0 temp/ml_result_sorted/ranked_${qid}.txt
                cut -d' ' -f2- temp/ml_result_sorted/sorted_${qid}.txt > nl >> temp/ml_result_sorted/ranked_${qid}.txt

                let "qid += 1"
#            done
#        done
    done 

done # for each stencil code 

echo "${qid} input instances - ${execution} total validated executions - ${error} not validated" 
echo "stencil DSL total execution: $SECONDS sec"



