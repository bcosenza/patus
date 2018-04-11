# This scripts assumes that results are saved in 'optimize_all.txt'

import numpy as np
import matplotlib.pyplot as plt
from matplotlib.mlab import csv2rec 
from matplotlib.cbook import get_sample_data
import matplotlib.cm as cm
from matplotlib.backends.backend_pdf import PdfPages 
from matplotlib.font_manager import FontProperties

import os
import urllib2 

from plot_search_comparison import getMLperformance

# These are the colors that will be used in the plot
#color_sequence = ['#1f77b4', '#aec7e8', '#ff7f0e', '#ffbb78', '#2ca02c', '#98df8a', '#d62728', '#ff9896', '#9467bd', '#c5b0d5', '#8c564b', '#c49c94', '#e377c2', '#f7b6d2', '#7f7f7f', '#c7c7c7', '#bcbd22', '#dbdb8d', '#17becf', '#9edae5']


ml = getMLperformance()

ml_mask = [0,3,6,10] #[960,3840,6720,16000]
bench_mask = [0,1,3,4,5,6,7,8,9,10,12,13,14,15,16,17,18] # out of 20

# function to plot an overview of both ML and search in one single graph
def plot_fullcompare(search_full):
	iterreference = 1023 # starts from 0!
	width = 0.090 # the width of the bars
	
	searchAlgoNum = len(search_full[0][3])
	algoNum  = len(ml_mask) + searchAlgoNum
	benchNum = len(bench_mask) #len(search_full)
	yy = []
	
	average = []
	geom    = []
	
	# for each bench
	bechName = []
	for i, search in enumerate(search_full):
		if not i in bench_mask:
			continue
		bechName.append(search[0]+ "\n" + search[1].replace(" ","x"))
		benchname=(search[0]+' '+search[1]).replace(" ","_")
		print 'search bench', search[0] + " " + search[1]
		y = []
		# for each search algo
		for s, algo in enumerate(search[2]):
			rt = search[3][s][iterreference]
			y.append(rt)
		print y
		# look for the same ML algo (ordering can be different)		
		for m, model in enumerate(ml): # for each model
			if not m in ml_mask:
				continue
			for j, bench in enumerate(model[1]): # for each bench
				modelname = model[0]
				#print '***', bench
				if(benchname == bench):
					rt = model[2][j]
					y.append(rt)
					print 'ml     bench', model[1][j]
		print y
		yy.append(y)
	
	
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
	for a, algo in enumerate(search_full[0][2]):
		labels.append( algo + " " + str(iterreference+1) + " evaluations")
	for m, algo in enumerate(ml):
		if not m in ml_mask:
			continue
		lab = 'ord.regression C=' + algo[0].split('_')[1] + ' size=' + algo[0].split('_')[3]
		labels.append( lab )
	
	print algoNum,len(labels),benchNum
	
	# Color space: cold colors for search, warm ones for ML
	#color_search = cm.rainbow(np.linspace(0,1,len(labels)))
	color_search1 = cm.rainbow(np.linspace(0,0.4,searchAlgoNum))
	color_search2 = cm.rainbow(np.linspace(0.7,1,len(ml_mask))) #len(labels)-searchAlgoNum))
	
	# plot 1 - absolutes runtimes 
	fig, axes = plt.subplots(1)
	ax = axes 
	plt.xlim([0,benchNum+0.5])
	plt.grid(True)
	
	ind = np.arange(benchNum)  # the x locations for the groups
	rects = []
	for i, rt in enumerate(labels):
		ix = i
		if(i>=searchAlgoNum): # we add a little space between search and ML results
			ix = i + 0.8
			bar = ax.bar(ind + width * ix, yy[i], width, color=color_search2[i-searchAlgoNum]) 
		else:
			bar = ax.bar(ind + width * ix, yy[i], width, color=color_search1[i]) 
		rects.append(bar)
	ax.set_ylabel('performance GB/s')
	#ax.set_title('test benchmarks')
	ax.set_xticks(ind + width + 0.15)
	ax.set_xticklabels(bechName, rotation=0,ha='center')
	ax.legend(rects, labels, loc=9,ncol=3)
	
	fig.set_size_inches(17*2.54, 4*2.54, forward=True)
	plt.tight_layout()
	plt.savefig('plots/all_benchs_perf.png', bbox_inches='tight')
	with PdfPages('plots/all_benchs_perf.pdf') as pdf:
		pdf.savefig(plt.gcf())
	#plt.show()
	plt.close()
	
	
	# plot 2 - speedups
	fs=22
	fig, axes = plt.subplots(1)
	ax = axes 
	plt.xlim([-0.1,benchNum+0.02])
	plt.ylim([0,1.5])
	plt.grid(True)
	plt.axhline(y=1.0, xmin=0.0, xmax=1.0, linewidth=1, color = 'black')
	
	ind = np.arange(benchNum)  # the x locations for the groups
	rects = []
	for i, rt in enumerate(labels):
		ix = i
		if(i>=searchAlgoNum): # we add a little space between search and ML results
			ix = i + 0.5
			bar = ax.bar(ind + width * ix, speedups[i], width, color=color_search2[i-searchAlgoNum]) 
		else:
			bar = ax.bar(ind + width * ix, speedups[i], width, color=color_search1[i]) 
		rects.append(bar)
	ax.set_ylabel('speedup', fontsize=fs)
	#ax.set_title('test benchmarks')
	ax.set_xticks(ind + width + 0.25)
	ax.set_xticklabels(bechName, rotation=0,ha='center', fontsize=fs)
	ax.legend(rects, labels, loc=9,ncol=4,fontsize=fs)
	
	fig.set_size_inches(17*2.54, 3.5*2.54, forward=True)
	plt.tight_layout()
	plt.savefig('plots/all_benchs_speedup.png', bbox_inches='tight')
	with PdfPages('plots/all_benchs_speedup.pdf') as pdf:
		pdf.savefig(plt.gcf())
	#plt.show()
	plt.close()



# function to plot, for each a bench, search with all iters vs ML
def plot_search(title, size, algos, rttable):
	plt.rcParams['xtick.labelsize'] = 18
	plt.rcParams['ytick.labelsize'] = 18
	fs=23
	maxiter=1024
	benchname=title+'_'+size.replace(' ','_')
	fig, ax = plt.subplots(1)
	# print(title, size, algos, len(rttable))
	plt.title(title+' '+size.replace(' ','x'), fontsize=24, ha='center')
	plt.grid(True)
	plt.xlabel('evaluations', fontsize=fs)
	plt.ylabel('performance GFlop/s', fontsize=fs)
	plt.xlim(1, maxiter)
	plt.xscale('log', basex=2) 
	
	color_search   = cm.rainbow(np.linspace(0,0.4,len(algos)))
	markers = ['+', 's', 'x', 'o']
	
	for rank, column in enumerate(rttable):
		mycolumn=column[:maxiter]
		#print (rank, algos[rank], len(column), len(mycolumn))
		serie = plt.plot(mycolumn)		
		plt.setp(serie, color=color_search[rank], label=algos[rank], marker=markers[rank], markersize=9, markevery=[1024,1024] ) # fake marker, used only to change the legend
		# plot the marker
		pow2x=(2,4,8,16,32,64,128,256,512)
		pow2y=[column[i] for i in pow2x]
		plt.plot(pow2x, pow2y,linestyle='None', marker=markers[rank], markersize=9, color=color_search[rank]) # real marker
		#plt.text(len(mycolumn), mycolumn[-1], algos[rank], color=color_search[rank], fontsize=fs)		
	legend1 = plt.legend(loc=4, ncol=1,fontsize=fs,bbox_to_anchor=(1.0, .29))
	
	
	# plot a line for each ML model
	color_model    = cm.rainbow(np.linspace(0.7,1,len(ml_mask)))
		
	k=0
	lines = []
	modelnames = []
	for i, model in enumerate(ml): # for each model
		if not i in ml_mask:
			continue
		for j, bench in enumerate(model[1]): # for each bench
			modelname = model[0]
			modelname = 'ord.regression C=' + modelname.split('_')[1] + ' size=' + modelname.split('_')[3]
			#print '***', bench
			if(benchname == bench):
				rt = model[2][j]
				lines.append( plt.axhline(y=rt, xmin=0.0, xmax=1.0, linewidth=1, color = color_model[k], linestyle='--') )
				modelnames.append(modelname)
				#plt.text(1.0, rt, modelname, color = color_model[k], fontsize=fs)
		k = k+1
	
	#legend1 = pyplot.legend(plot_lines[0], ["algo1", "algo2", "algo3"], loc=1)
	#pyplot.legend([l[0] for l in plot_lines], parameters, loc=4)
	
	legend2 = plt.legend(lines, modelnames, loc=4, ncol=1,fontsize=fs,bbox_to_anchor=(1.0, .01))
	plt.gca().add_artist(legend1)
	
	#plt.show()
	#fig = plt.gcf()
	fig.set_size_inches(8.3*2.54, 4*2.54, forward=True)
	filename='plots/search_'+title+'-'+size.replace(' ','_')
	plt.savefig(filename+'.png', transparent=True, bbox_inches='tight')
	with PdfPages(filename+'.pdf') as pdf:
		pdf.savefig(fig, transparent=True)
	# clear 
	plt.cla()
	plt.clf()
	plt.close()
	return


# open raw result file
file = open('optimize_all.txt', 'r')
#file = urllib2.urlopen("http://www.user.tu-berlin.de/cosenza/stencil_data/optimize_all.txt")

search_full = []

runtimes = []
rttable = []
algos = []

# create a folder for plots
#plot_path=os'./plots'
#if not os.path.exists(plot_path):
#	os.makedirs(plot_path)
#print 'created!'

#os.makedirs('plots')
#os.mkdir('plots')

line = file.readline()
while(line.startswith('optimizing')):
	title=' '.join(line.split()[1:])
	line = file.readline() # parse the size
	while(len(line)>1 and line[0].isdigit()): # for each series
		size=line.rstrip('\n')
		algos = []
		rttable = []
		runtimes = []
		line = file.readline() # parse the algorithm
		while(line.startswith('running')):
			algoname = line[9:].rstrip('\n').strip()
			runtimes = []
			line = file.readline() # parse runtimes
			# consume dirty lines
			while(line.startswith('(1 + 10)ES')):
				line = file.readline()
			# for each  evaluation
			while(line.startswith('Evaluated')):
				sline=line.split()
				index=sline[3]
				rt=float(sline[4])
				runtimes.append(rt)
				line = file.readline() # consume next line
			if runtimes: # if is not empty
				algos.append(algoname)
				rttable.append(runtimes)
			# consume dirty lines
			while(line.startswith('Total') or line.startswith('Objectives') or line.startswith('Variables')):
				line = file.readline()
		# plot a per benchmark comparison
		plot_search(title, size, algos, rttable)
		search_full.append( (title,size,algos,rttable) )

file.close()

# plot a general overview	
plot_fullcompare(search_full)
