# This scripts assumes that results are saved in 'optimize_all.txt'

import os
import glob

import numpy as np
import matplotlib.pyplot as plt
import matplotlib.cm as cm

from matplotlib.backends.backend_pdf import PdfPages 


def getMLperformance():
	datadir = os.listdir('./data')
	allmodels = []
	
	# all model files
	modelfiles = glob.glob('./data/*.model')
	modelfiles = [ x[7:-6] for x in modelfiles]
	modelsizes = [int(x.split('_')[3]) for x in modelfiles]	
	
	#
	#modelfiles = []
	
	
	# sort by size, using a fancy zip transform
	modelfiles = zip(*sorted(zip(modelsizes, modelfiles)))[1]
	#print modelfiles
	
	# for each model type
	for modelname in modelfiles:
		testcaselist = [] 
		performancelist = []
		
		# all performance are listed in the summary file
		summary=open('./data/'+modelname+'/summary.txt')
		for line in summary: 
			lsp=line.split()
			testcaselist.append(lsp[0])
			performancelist.append(float(lsp[-1]))
		model = (modelname, testcaselist, performancelist)
		allmodels.append(model)
	return allmodels



def plot_ml2():
	ml=getMLperformance()
	
	#fig, axes = plt.subplots(nrows=len(ml), ncols=1,squeeze=True)
	fig, axes = plt.subplots(nrows=4, ncols=2,squeeze=True)	
	
	for i, ax in enumerate(axes.flat): #.flat, start=0):
		if(i>=len(ml)): break
		model=ml[i]
		rts=model[2]
		ax.set_title(model[0])
		ax.set_xlabel('test benchmarks')
		ax.set_ylabel('performance GFlop/s')
		ax.hist(rts, len(rts), normed=1, histtype='bar', label=model[1])
		#ax.legend(prop={'size': 10})
	fig.tight_layout()
	plt.show()


def plot_ml():
	ml=getMLperformance()
	
	fig, ax = plt.subplots()
	
	# modelnames & runtimes
	models = []
	runtimesA = []
	for entry in ml:
		bs = entry[0].split('_')
		t=''
		if bs == '0':
			t='linear'
		models.append("c "+bs[1]+" "+t+" size "+bs[3])
		## scaled runtimes
		#scaled=[x*0.001 for x in entry[2]]
		runtimesA.append(entry[2])
	
	
	#print 'models', models	
	# test benchmarks names
	benchs = ml[0][1]
	testcases = []
	for b in benchs: # nice formatting
		bs=b.split('_')
		if len(bs) == 3:
			sizestr=bs[1]+"x"+bs[2]
		else:
			sizestr=bs[1]+"x"+bs[2]+"x"+bs[3]
		testcases.append(bs[0]+"\n"+sizestr)
	#print 'testcases', testcases
	
	# runtimes by benchmarks
	runtimesB = []
	for i, b in enumerate(benchs):
		benchruntime = []
		for m in ml:
			print b, m[0], i
			benchruntime.append(m[2][i])
		runtimesB.append(benchruntime)
	#print 'runtimesA', runtimesA
	#print 'runtimesB', runtimesB
	
	ind = np.arange(len(testcases))  # the x locations for the groups
	width = 0.10       # the width of the bars	
	
	# rectangles
	rects = []
	colors = cm.rainbow(np.linspace(0,1,len(models)))
	for i, rt in enumerate(models):
		bar = ax.bar(ind + width * i, runtimesA[i], width, color=colors[i]) 
		rects.append(bar)
	
	
	ax.set_ylabel('performance GFlop/s')
	ax.set_title('test benchmarks')
	ax.set_xticks(ind + width)
	ax.set_xticklabels(testcases,rotation=90)	
	ax.legend(rects, models )
	
	# plt.show()
	fig.set_size_inches(18.5, 10.5, forward=True)
	plt.savefig('plots/models_cmp.png', bbox_inches='tight')
	with PdfPages('plots/models_cmp.pdf') as pdf:
		pdf.savefig(plt.gcf())
	
	plt.clf() 
	plt.cla()


if not os.path.exists('plots'):
	os.mkdir('plots')

plot_ml()