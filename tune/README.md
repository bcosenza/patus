# Patus-AA



## Machine Learning-based Auto-Tuning for the Patus Stencil Compiler
 
This page describe how to instal the stencil tuning framework based on ordinal regeression, included in Patus-AA. 
Before to start, it is strongly recommended to read the seminal paper that describes the methodology behind the code:

_Cosenza, Durillo, Ermon, and Juurlink._ Autotuning Stencil Computations with Structural Ordinal Regression Learning. 
_In IEEE International Parallel and Distributed Processing Symposium (IPDPS), pages 287-296, 2017._
http://biagiocosenza.com/papers/CosenzaIPDPS17.pdf
 
This folder includes a set of bash scripts to reproduce the setup used in the paper. All the commands listed in this page assume that youu are in the _/tune_ folder of the Patus-AA project. 
Patus-AA has different installation scenarios, and it is very important to know that some (see point 2 below) have a very long the preprocessing phase, which may take up to 2 days of processing (due to the long compilation time required by Patus to compile large pattern in the training codes). However, if you have the same processor type of those used in the paper, you can quickly setup the system in few minutes by using our pre-built models.

Therefore, we distinguish two installation scenarios:

1. if you already have a model for your target hardware (e.g., is the same of the one proposed in the paper), then you should:

  * Install Patus    
  * Install SVM Rank   
  * Run the PATUS-AA stencil autotuner with a prebuilt model  

2. if you don't have a prebuilt model for your target hardware , then you shuold:

  * Install Patus    
  * Install SVM Rank     
  * Build a new model that supports your target   
  * Run the PATUS-AA stencil autotuner with your model

The paper also shows results for search based heuristics, mainly used for quality comparison. For those, we have replaced Patus' default autotuner with the JMetal library, which supports a larger set of search heuristics. For more information, check the  'Iterative-Search' section below.



## Install PATUS-AA 

### 1. Installing PATUS

In the following, we assuming to have a Ubuntu Linux OS, but installation is pretty similar with other linux distributions.

First, you need to install gcc, make and Maxima: 
```
> sudo apt install gcc make maxima
```
 
The you need the Java compiler to compile Patus. We highly recommend to use the Oracle VM, as the default OpenJDK RE is very slow while compiling stencil codes with Patus. You can download and install it in this way: 
```
> sudo apt-add-repository ppa:webupd8team/java
> sudo apt update
> sudo apt install oracle-java8-installer
```

Be sure to set your JAVA_HOME environment variable to point to the installed JDK. Additionally, set your PATH environment variable. For example, run the following commands if using Solaris.
```
> export JAVA_HOME=/usr/lib/jvm/java-8-oracle 
> PATH=$JAVA_HOME/bin:$PATH;
> export PATH
```

In case of multiple Java installation, you can check whether you are really using the Oracle JVM with: 
```
> java -version
```

Optionally, you may install:
- Eclipse (for development) 
```
> sudo apt install eclipse eclipse-cdt g++
```
- GNUplot (used by some Patus's automatic tuning script) 
```
> sudo apt install gnuplot
```
- Python and Pythons's required libraries (used by Patus-AA for scripting and plotting)
```
> sudo apt install python-numpy python-scipy python-matplotlib 
```

*Note for Raspberry PI*: many packages are already installed. For Java, if no installed, it should be possible to install it with:
```
> sudo apt install oracle-java8-jdk
```



### 2. Configure PATUS

Once you cloned the _patus-aa_ repository, you will find the necessary scripts into the _tune_ folder.
To configure the environment for Patus, the script _configure_patus.sh_ is provided. If no argument is provided, it will assume as that you want to use the the _x86_64 SSE_ backend:
```
> source configure_patus.sh
```
Alternatively, you can also specify the target backend by providing an extra parameter. For instance, you can use the _ARM_NEON_ backend in the following:
```
> source configure_patus.sh ARM_NEON
```
A list of the supported backends is provided by the command
```
> patus --help
```
 
 
### 3. Getting started with PATUS

All scripts starting with the prefix _patus_ allow you to test the Patus stencil compiler with default parameters (not autotuned). 
For example, you can build and run all the available test benchmarks with the following command (note: takes about 1h):
```
> bash ./patus_all.sh
```


## Install SVM Rank
 
1. Download and install SVM Rank from Cornell's repository
```
> bash ./get_svmrank.sh
```

 
## Install Stencil TUNE with a new model 
 
There are a collection of scripts to build the model and run the TUNE Optimizer

1. [~2 sec] Compile the code generator and auto-generate stencil training code patterns 
```
 > bash ml_1_autogen_stencil.sh
``` 

This step will generate  a number of stencil code in _temp/ml_stencil_code_.


2. [24-32 hours] Double compilation of the training stencil codes with Patus and back-end compiler (e.g, gcc). Some automatically generated codes using many samples (e.g., hypercubes) are very slow to compile with Patus. Unfortunately, we couldn't fix this issue, thus compilation will take approximately 32 hours on a Xeon E5 (see Table II, "e5" in the paper, for a detailed description). 

You can generate and build the code and scripts for a new model, from scratch, (warning: it will take approx. 32 hours!) with:
```
 > bash ml_2_autocompile.sh
``` 

After this step, 'temp/ml_stencil_build' will contain the compiled, multi-versioned binary sources.


3. [from minutes to hours] Run all stencil codes  with a set of tuning parameters, and save results in a ranked/qid format. This step is highly parametrized: you can quickly create small dataset with few dozens of run per kernel, or, instead, use a much bigger dataset. E.g., a quick and small dataset can be generated in this way: 
```
> bash ml_3_stencil_exec.sh small
```

Running time of the stencil executions and corresponding encoded values are stored in a number of folder in temp (ml_execution_params, ml_result_raw, ml_result_qid, ml_result_sorted). 
 
Supported dataset-size flag are: "small", "medium", "big", and numerical, such as "10". We suggest to first test the system with the smallest dataset possible, as the bigger the size, the longer the time to generate it (generation goes from minutes to hours or even days!). The 'small' option produces a dataset with 3200 samples.
For an estimation of the stencil execution time for different datasets, have a look at the paper, Table II.


4. [less than 1 min] Prepare all the collected data for the training phase, using the SVM-Rank data format.
```
> bash ml_4_prepare_training.sh
```
 
 This step will create an output file in temp named _training_size_SX.txt_, where _SX_ is the size of the training set.
 This script will also try to run a python script calculate the Tau distribution of the training dataset. However, this will not work if called directly in this way; to compute this additional information have a look at the scripts _ml_learning_curve.sh_, which sets the right configuration for calculating the Kendal Tau values.

 
5. [few seconds] Model training using SVM-Rank
```
> ./svm_rank/svm_rank_learn -c 20.0 ./temp/training_size_*.txt data/model.dat
```
 
6. [<1 ms] Model inference using SVM-Rank
 
  Regression with a test code from the Patus examples (in this case, blur 1024x1024)
```
> ./svm_rank/svm_rank_classify  data/blur_1024_1024.test data/model.dat output.txt
```

  Successively, with
```
> cat output.txt
```

   you can see the score (a metric for the ordinal distance) for each configutaion in the _data/blur_1024_1024.test_ file.
   The best performing configuration is the one with the smallest score (e.g., line 30), corresponding to the 30 configuraion in the _.test_ file.
 

## Install Stencil TUNE with a prebuilt model 

As building a model is a copmute-intensive long task, we provide a nummber of pre-built models for a set of target machines.
Those models are saved in the _/tune/model_ folder. If your target is one of them, you can download it and reuse it for autotuning your stencil codes.

TODO  explain how it works


### Replicate the results in the paper  
 
A number of scripts have been created to reproduce the same test benchmarks you find in the paper.
Data for Figure 4, 5, 6 and 7 can be generated by using these two scripts:

[how long?]
```
> bash learning_curve.sh
```
Create different datasets with different sizes and same parameters. 

[<20 seconds for each dataset]
```
> bash ml_5_evaluate_all.sh
```
For each dataset, builds a model with the same parameters  (_C=0.01_ and _linear kernel_), does regression and executes the top-ranked configuration.
If more datasets are available, this step takes longer teme, as it is repeated for each file in: _temp/training*.txt_.

The comparison with iterative-search heuristics is discussed in the next section.
 
A number of python scripts have been used to plot results, compute statistical distribution od Tau values, and compare different methods. They are all available in the _/tune_ folder (we suggest to copy the txt files on which such scripts work on a different folder). Examples are: 
```
> plot_optimize_all.sh
> plot_search_comparison.sh
 ```
 
## Autotuning with Iterative-Search (useful for comparison, or to optimize a single code)
  
Once you have correctly configured Patus  (see the configuration scripts listed above), you can use the following scripts to test the different autotuning methods based on iterative-compilation with search heuristics.
 
1. compile all Patus DSLs
```
> bash compile_benchmarks.sh
```

2. run the iterative optimizer based on JMetal
```
> bash search_all.sh
``` 
 
3. (optional, not recommended) you can also test Patus default iterative optimizer with the following script:
```
> bash patus_autotune.sh
```

 However, we suggest to use the optimizer based on JMetal, as it supports a largeer number of search heuristics.
  



