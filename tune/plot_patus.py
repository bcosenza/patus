# This scripts assumes that Patus results are saved in 'patus_temp'

import numpy as np
import matplotlib.pyplot as plt
from matplotlib.mlab import csv2rec 
from matplotlib.cbook import get_sample_data
import matplotlib.cm as cm
from matplotlib.backends.backend_pdf import PdfPages 

import glob
import os
import urllib2 

from plot_search_comparison import getMLperformance


ml_mask = [0,3,6,10] #[960,3840,6720,16000]

ml = getMLperformance()

# function to plot an overview of both ML and Patus' search 
def plot_patus(patus):
	width = 0.070 # the width of the bars
	
	searchAlgoNum = 1
	algoNum  = len(ml_mask) + searchAlgoNum
	yy = []
	
	average = []
	geom    = []
	
	# for each bench
	bechname = []
	sortedbecnhs = sorted(patus.items())
	#for bench, rt in patus.iteritems():
	for item in sortedbecnhs:
		(bench, rt) = item
		bs=bench.split('_');
		name = bs[0]+ "\n" + bs[1] + "x" + bs[2];
		if(len(bs) > 3):
			name = name + "x" + bs[3]
		
		#print 'patus bench:', bench
		y = []
		y.append(rt)
		#print rt
		# look for the same ML algo (ordering can be different)		
		for m, model in enumerate(ml): # for each model
			if not m in ml_mask:
				continue
			for j, b in enumerate(model[1]): # for each bench
				modelname = model[0]
				#print '***', bench, '***'
				if(bench == b):
					rt = model[2][j]
					y.append(rt)
					#print 'ml     bench:', b#model[1][j],':'
		#print y
		# we add that line only if ML is available
		if(len(y) > 1):
			yy.append(y)
			bechname.append(name)

	benchNum = len(yy) # there can be test benchs for which we dont have ML results
	print 'benchnum ',benchNum
	
	
	# speedups
	speedups = np.zeros((algoNum,benchNum))
	for i, bench in enumerate(yy):
		baseline = float(yy[i][0])
		print '--',bench
		for j, rt in enumerate(bench):
			speedups[j][i] = float(rt)/baseline
	
	yy = np.transpose(yy)
	
	# create label with algo names
	labels = []	
	labels.append( "Patus autotuner")
	for m, algo in enumerate(ml):
		if not m in ml_mask:
			continue
		lab = 'ord.regression C=' + algo[0].split('_')[1] + ' size=' + algo[0].split('_')[3]
		labels.append( lab )
	
	print algoNum,len(labels),benchNum
	
	
	# plot 1 - absolutes runtimes 
	fig, axes = plt.subplots(1)
	ax = axes 
	plt.xlim([0,benchNum+0.5])
	plt.grid(True)
	
	ind = np.arange(benchNum)  # the x locations for the groups
	rects = []
	color_search = cm.rainbow(np.linspace(0,1,len(labels)))
	for i, rt in enumerate(labels):
		ix = i
		if(i>=searchAlgoNum): # we add a little space between search and ML results
			ix = i + 0.8
		bar = ax.bar(ind + width * ix, yy[i], width, color=color_search[i]) 
		rects.append(bar)
	ax.set_ylabel('performance GB/s')
	#ax.set_title('test benchmarks')
	ax.set_xticks(ind + width + 0.25)
	ax.set_xticklabels(bechname, rotation=0,ha='center')
	ax.legend(rects, labels, loc=9,ncol=3)
	
	fig.set_size_inches(17*2.54, 4*2.54, forward=True)
	plt.tight_layout()
	plt.savefig('plots/patus_perf.png', bbox_inches='tight')
	with PdfPages('plots/patus_perf.pdf') as pdf:
		pdf.savefig(plt.gcf())
	#plt.show()
	plt.close()
	
	
	# plot 2 - speedups
	fig, axes = plt.subplots(1)
	ax = axes 
	plt.xlim([0,benchNum+0.5])
	plt.ylim([0,2.0])
	plt.grid(True)	
	plt.axhline(y=1.0, xmin=0.0, xmax=1.0, linewidth=1, color = 'black')
	
	ind = np.arange(benchNum)  # the x locations for the groups
	rects = []
	color_search = cm.rainbow(np.linspace(0,1,len(labels)))
	for i, rt in enumerate(labels):
		ix = i
		if(i>=searchAlgoNum): # we add a little space between search and ML results
			ix = i + 0.5
		bar = ax.bar(ind + width * ix, speedups[i], width, color=color_search[i]) 
		rects.append(bar)
	ax.set_ylabel('speed ups')
	#ax.set_title('test benchmarks')
	ax.set_xticks(ind + width + 0.25)
	ax.set_xticklabels(bechname, rotation=0,ha='center')
	ax.legend(rects, labels, loc=9,ncol=3)
	
	fig.set_size_inches(17*2.54, 3.5*2.54, forward=True)
	plt.tight_layout()
	plt.savefig('plots/patus_speedup.png', bbox_inches='tight')
	with PdfPages('plots/patus_speedup.pdf') as pdf:		
		pdf.savefig(plt.gcf())
	#plt.show()
	plt.close()

def get_patus():
	patus_rt = {} # dictionary
	for file in glob.glob('./patus_temp/*.tune'):
		lines=open(file,'r').readlines()
		print file, " ",lines[-6]
		rt=lines[-6].split()[2]
		bench=file[len('./patus_temp/'):-len('.tune')]
		patus_rt[bench]=float(rt)
		#print bench, rt
	return patus_rt

# plot patus vs ML
plot_patus( get_patus() )