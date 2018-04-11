# script to plot Principal Component Analysis on the training data set

import numpy as np
#import scipy.sparse
from sklearn.decomposition import PCA
from matplotlib import pyplot as plt

# open the file and read all lines
trainingfile = 'temp/training_size_3080.txt'
with open(trainingfile, 'r') as file:
    lines = file.readlines()

# parsing features
elemNum = len(lines)
maxDim  = 2230
print (elemNum,maxDim)

# we start with a zeros arrays 
X = np.zeros((elemNum, maxDim))
#print X


count = 0
for line in lines:
    qid   = line.find("qid:") + len("qid")
    nid   = line.find(" ",qid) + 1
    aline = line[nid:]
    print aline
    for element in aline.split():
        index = int(element.split(":")[0])
        value = float(element.split(":")[1])
        #print (index,value)
        X[count,index-1] = value        
    count += 1

#X = np.array([[-1, -1], [-2, -1], [-3, -2], [1, 1], [2, 1], [3, 2]])

print X

# actual PCA
pca = PCA(n_components=2)
pca.fit(X)
transf = pca.fit_transform(X)

#PCA(copy=True, n_components=2, whiten=False)
print(pca.explained_variance_ratio_) 

# plotting
#plt.plot(transf[0:20,0],transf[0:20,1], 'o', markersize=7, color='blue', alpha=0.5)
plt.plot(transf[:,0],transf[:,1], 'o', markersize=3, color='blue', alpha=0.5)
#plt.plot(sklearn_transf[20:40,0], sklearn_transf[20:40,1], '^', markersize=7, color='red', alpha=0.5, label='class2')
#plt.plot(X[:,0],X[:,1], 'x', markersize=3, color='red', alpha=0.5)

plt.legend()
plt.title('PCA transformed samples ')

plt.show()

