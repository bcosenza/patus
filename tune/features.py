#!/usr/bin/python
import re
import sys, getopt
# function to quickly extract features from a Patus stencil code
from builtins import print
from typing import Pattern


def extract(filename):
    file = open(filename,"r")
    # feature set
    #buffer
    buffer_count = 0
    const_buffer_count = 0
    #type
    type = "float"
    dimension = 0
    #shape
    points = []
    for line in file:
        # buffer type
        if "double" in line:
            type = "double"
        if "float" in line:
            type = "float"
        # dimension
        if "domainsize" in line:
            dimension = line.count("..")
        # buffer num
        if "grid" in line:
            buffer_count  += 1
            if "const" in line:
                const_buffer_count += 1
        # shape
        line = line.replace(" ", "") # removing any space
        #shapefinder = re.compile('\[[a-zA-Z][+-]?[0-9]?,\s[a-zA-Z][+-][0-9];\st\]')  # [ [a][+-][0-9],[a][+-][0-9]; t ]  ==> 2D points
        if(dimension == 2):
            shapefinder = re.compile('\[x[+-]?[0-9]?,y[+-]?[0-9]?;t\]')
        else:
            shapefinder = re.compile('\[x[+-]?[0-9]?,y[+-]?[0-9]?,z[+-]?[0-9]?;t\]')
        print(shapefinder.findall(line))
        for access in shapefinder.findall(line):
            access = access[1:-1] # remove [ ]
            x = int(0)
            y = int(0)
            z = int(0)
            pat_x = re.compile('x[+-]?[0-9]?').findall(access)
            pat_x = pat_x[0]
            #print(" x:",pat_x)
            if pat_x and len(pat_x)>1:
                x = int(pat_x[1:])
            pat_y = re.compile('y[+-]?[0-9]?').findall(access)
            pat_y = pat_y[0]
            #print(" y:",pat_y)
            if pat_y and len(pat_y)>1:
                y = int(pat_y[1:])
            if dimension == 3: # if 3d
                pat_z = re.compile('z[+-]?[0-9]?').findall(access)
                pat_z = pat_z[0]
                #print(" z:",pat_z)
                if pat_z and len(pat_z)>1:
                    z = int(pat_z[1:])
            #print(x,y,z)
            #numpattern = [int(x) for x in pattern.findall(access)]
            points.append([x,y,z])

        #shapefinder = findall("[*]")
        #for  in line
    # print results
    print("buf num     :",buffer_count)
    print("const buf num:",const_buffer_count)
    print("type        :",type)
    print("dimension   :",dimension)
    print("access      :")
    for p in points:
        print(p)

    print("XY")
    for i in range(-3,4):
        for j in range(-3,4):
            if [i,j,0] in points:
                print('*', end='')
            else:
                print('.', end='')
        print()
    if dimension == 2:
        return
    print("YZ")
    for i in range(-3,4):
        for j in range(-3,4):
            if [0,i,j] in points:
                print('*', end='')
            else:
                print('.', end='')
        print()
    print("XZ")
    for i in range(-3,4):
        for j in range(-3,4):
            if [0,i,j] in points:
                print('*', end='')
            else:
                print('.', end='')
        print()

    # write into a vector
    # XXX TODO
    return

def main():
    if len(sys.argv) != 2:
        print("Utility to extract and print static features of a Patus stencil code")
        print("Note: it is just a scanner, does not perform a real parsing; may be not accurate.")
        print("Usage: features.py <patus_filename>")
        return 0
    print(sys.argv[1])
    extract(sys.argv[1])


if __name__ == "__main__":
    main()

#def normalize_features():
#def print_features(features):





