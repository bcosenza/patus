# script to plot Kendall's Tau on the training data set
import math
import glob
import scipy.stats as stats
import os
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages 
from compute_kendall import compute_kendall    


#model='./data/model_0.01_0_12320.model'
#(qids,taus,benchs) = compute_kendall(model)
#qmax=max(qids)
#print "qmax", qmax

alltaus = []
datasetsize = []

def violin_plot(ax, data, pos, bp=False):
	dist = len(pos) # max(pos)-min(pos)
	w = min(0.15*max(dist,1.0),0.5) 
	ax.yaxis.grid(True)
	
	fpos = [] # formatted positions
	for p in pos:
		fpos.append(str(p/1000) + "K")
	
	i=1
	for d,p in zip(data,pos):
		# value 1 draw ugly violin
		dclamp=[]
		for x in d:
			dclamp.append(min(0.9999,x))
		d=dclamp
		
		k = stats.gaussian_kde(d) # calculates the kernel density
		m = k.dataset.min()       # lower bound of violin
		M = k.dataset.max()       # upper bound of violin
		x = np.arange(m,M,(M-m)/100.) # support for violin
		v = k.evaluate(x)         # violin profile (density curve)
		v = v/v.max()*w *0.8          # scaling the violin to the available space
		#ax.fill_betweenx(x,p,v+p,facecolor='y',alpha=0.3)
		#ax.fill_betweenx(x,p,-v+p,facecolor='y',alpha=0.3)
		ax.fill_betweenx(x,i,v+i,facecolor='y',alpha=0.3)
		ax.fill_betweenx(x,i,-v+i,facecolor='y',alpha=0.3)
		i = i+1
	if bp:
		ax.boxplot(data,notch=1,vert=1)#,positions=pos)
	plt.xticks(range(1, len(pos)+1), fpos,rotation=70)
	plt.ylim(ymin=-1.0,ymax=1.0)
	#plt.xscale('log', basex=2) 
	ax.set_xlabel('size')
	#ax.set_xlabel('C')
	ax.set_ylabel(r"Kendall's $\tau$")


# for each tau file, previously saved in the folder data/kendall/
for i, filename in enumerate(glob.glob("./data/kendall/*.tau")):
	print 'kendall2 on ',filename
	taus = []
	file = open(filename)
	for line in file:
		#print line
		taus.append(float(line))
	qmax=len(taus)
	
	fig = plt.figure()#facecolor='white')
	ax = fig.add_subplot(1,1,1)
	ax.bar(left=range(1,qmax+1,1),height=taus,width=0.1)
	ax.grid(b=True)
	#ax.spines['left'].set_position('zero')
	#ax.spines['right'].set_color('none')
	#ax.spines['bottom'].set_position('zero')
	#ax.spines['top'].set_color('none')
	ax.axis([0,qmax,-1,1])
	
	#plt.axhline(y=np.mean(taus), xmin=0.0, xmax=1.0, linewidth=1, color = 'red')
	#plt.text(0.0, np.mean(taus), 'mean', color = 'red')
	
	#plt.title("Kendall's Tau on the training set")
	size=filename.split('/')[-1][:-4]
	plt.xlabel(size.replace('_','='))
	plt.ylabel(r"Kendall's $\tau$")
	plt.grid(True)
	#plt.plot(taus)
	#plt.show()
	filename='plots/kendall_'+filename.split("data/kendall/")[1]
	plt.savefig(filename+'.png') #, bbox_inches='tight')
	with PdfPages(filename+'.pdf') as pdf:
		pdf.savefig(plt.gcf())
	
	alltaus.append(taus)	
	#print 'size:',size.split('_')[1]
	#datasetsize.append(int(size.split('_')[1]))
	datasetsize.append(float(size.split('_')[1]))


# clean up all
plt.cla()
plt.clf()
plt.close()

# plot all togheter in a violin plot

# sort arrasy by size
datasetsize, alltaus = (list(x) for x in zip(*sorted(zip(datasetsize, alltaus), key=lambda pair: pair[0])))

#fig, axes = plt.subplots(nrows=2, ncols=3, figsize=(6, 6))
fig = plt.figure()#facecolor='white')
ax = fig.add_subplot(1,1,1)
ax
violin_plot(ax,alltaus,datasetsize,bp=True)

#ax.violinplot(alltaus, datasetsize, points=60, widths=0.5, showmeans=True, showextrema=True, showmedians=True, bw_method=0.5)
#ax.set_title(r"Kendall's $\tau$ density with different sizes", fontsize=10)
#fig.suptitle("Violin Plotting Examples")
#fig.subplots_adjust(hspace=0.4)
fig.tight_layout()

plt.savefig('plots/kendall_violin2.png') #, bbox_inches='tight')
with PdfPages('plots/kendall_violin2.pdf') as pdf:
	pdf.savefig(plt.gcf())
