#!/bin/bash

#$ tests/src/pldi/benchmarks/benchmark2/servers.sh 8888 9999  

if [ $# -ne 2 ]
then
  echo 'Expected arguments: [port_a, port_b]; not:' $@
  exit 1
fi 

port_a=$1
port_b=$2


echo benchmark2c:

bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark2.c.Server $port_a 


echo benchmark2d:

bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark2.d.Server $port_b 
