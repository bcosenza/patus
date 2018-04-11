import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages 

#size = [ '0.96K', '1.92K', '2.88', '3.84K', '48K', '5.76K', '6.72K', '7.68K', '8.64K', '9.6K', '16K', '32K']
size = [ 960, 1920, 2880, 3840, 4800, 5760, 6720, 7680, 8640, 9600, 16000, 32000]
tra  = [   1,    1,    1,    1,    1,    1,    1,    2,    2,    2,     7,    36]
reg  = [   1,    1,    1,    1,    1,    1,    1,    1,    1,    1,     1,     1]
#np.arange(0., 5., 0.2)

fig, ax = plt.subplots()

# red dashes, blue squares and green triangles
plt.plot(size, tra, linestyle=':', label='training time')
plt.plot(size, reg, linestyle='--', label='regression time')
#plt.axis([0,32000,0,40])

legend = plt.legend(loc='upper center',fontsize=20) #, shadow=True, fontsize='x-large')

# Put a nicer background color on the legend.
#legend.get_frame().set_facecolor('#00FFCC')

ax.set_ylabel('time ms',fontsize=20)
ax.set_xlabel('training set size',fontsize=20)
#ax.set_xticks(ind + width)
#ax.set_xticklabels(testcases,rotation=90)	
#ax.legend(rects, models )

#plt.show() 
fig.set_size_inches(18.5, 10.5, forward=True)
plt.savefig('plots/training_amortization.png', bbox_inches='tight')
with PdfPages('plots/training_amortization.pdf') as pdf:
	pdf.savefig(plt.gcf())

plt.close()