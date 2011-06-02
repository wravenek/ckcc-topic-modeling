#!/bin/bash

for i in `cat steps.txt`; do 
	java -cp $CLASSPATH:bin:$HOME/wrk/workspace/semanticVectorsGPU/bin -Xmx21000m modelling/IndexMainCpu $i
done
