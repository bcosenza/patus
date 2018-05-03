# Installation of SVM-Rank from source code

mkdir svm_rank
cd svm_rank

# source version
wget http://download.joachims.org/svm_rank/current/svm_rank.tar.gz
tar xf svm_rank.tar.gz 

# 64-bit binary linux version
# wget http://download.joachims.org/svm_rank/current/svm_rank_linux64.tar.gz
# tar xf svm_rank_linux64.tar.gz 

# compile SVM rank and struct
make all

cd ..
