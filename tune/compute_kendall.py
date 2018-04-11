# script to compute Kendall's Tau on the training data set 

import scipy.stats as stats
import os
import numpy as np
import sys
from matplotlib.backends.backend_pdf import PdfPages 


def compute_kendall(model):
	print 'model', model
	modelname=model.split("kendalltemp/training_")[1]
	modelname=modelname[:-6]
	print 'modelname',modelname
	#modelname=modelname[:-1]
	qiddir  = './temp/ml_result_qid/'
	sortdir = './temp/ml_result_sorted/'
	
	# model file
	#model="./data/model_0.01_0_12320.model"
	qids = []
	taus = []
	benchs = []
	
	# for each (code k, size s) in the training pattern
	for file in os.listdir(qiddir):
		if file.endswith(".txt"):
			r1 = []
			r2 = []
			#print file
			qid=file.replace(".txt","") 
			qid=qid.split("_q").pop()	
			
			# get the real ranking
			# we create a temporary file with all entries
			traindatafilename='./data/kendall/real_'+modelname+'_'+qid+'.txt'
			rank2file = open(traindatafilename, 'w+')
			
			count = 1
			with open(qiddir+file) as f:
				for line in f:
					midpattern=" qid:"+qid+" "
					#print line.split(midpattern) # performance value
					r1.append(float(line.split(midpattern)[0]))
					#print line.split(midpattern) # feauture 
					newline=str(count)+" qid:"+qid+" "+line.split(midpattern)[1]
					rank2file.write(newline)
					count += 1
			rank2file.close()
			
			# get the predicted ranking
			rank2pred="./data/kendall/"+modelname+"_"+qid+".pred"   
			os.system("./svm_rank/svm_rank_classify "+traindatafilename+" "+model+" "+ rank2pred)
			with open(rank2pred) as f:
				for line in f:
					#print line # ordinal value
					r2.append(float(line))
			#calculate Kandall's Tau
			print 'r1',len(r1),'r2',len(r2)
			tau, p_value = stats.kendalltau(r1, r2)
			#print (qid,tau,p_value)
			# add one entry in the graph
			taus.append(tau)
			qids.append(int(qid))
			benchs.append(qid)
	# write kendal in a file
	kfile = open('./data/kendall/'+modelname+".tau", 'w+')
	for T in taus:
		kfile.write(str(T)+"\n")
	kfile.close()
	return (qids,taus,benchs)

if(len(sys.argv) > 1):
	compute_kendall(sys.argv[1])