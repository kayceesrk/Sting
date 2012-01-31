#!/bin/bash

#$ tests/src/pldi/benchmarks/benchmark2/clients.sh localhost 6666 7777 8888 9999 1 

if [ $# -ne 6 ]
then
  echo 'Expected arguments: [hostname, port_a, port_b, port_c, port_d, iters]; not:' $@
  exit 1
fi 

hostname=$1
port_a=$2 
port_b=$3
port_c=$4 
port_d=$5
iters=$6


depths=( 0 1 2 4 8 )
numdepths=${#depths[@]}


for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark2a: depth=$depth 

  tests/src/pldi/benchmarks/benchmark2/a/noalias.sh $port_a $depth -r $iters
done


bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000


for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark2b: depth=$depth 

  tests/src/pldi/benchmarks/benchmark2/b/ordinary.sh $port_b $depth -r $iters
done


bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000


for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark2c: depth=$depth 

  if [ $d -eq $(( $numdepths - 1 )) ]
  then
    tests/src/pldi/benchmarks/benchmark2/c/client.sh $hostname $port_c $depth -r $iters -k
  else
    tests/src/pldi/benchmarks/benchmark2/c/client.sh $hostname $port_c $depth -r $iters
  fi
done


bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000


for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark2d: depth=$depth 

  if [ $d -eq $(( $numdepths - 1 )) ]
  then
    tests/src/pldi/benchmarks/benchmark1/a/client.sh $hostname $port_d $depth -r $iters -k
  else
    tests/src/pldi/benchmarks/benchmark1/a/client.sh $hostname $port_d $depth -r $iters
  fi
done
