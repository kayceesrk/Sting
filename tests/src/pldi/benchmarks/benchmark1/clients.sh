#!/bin/bash

#$ tests/src/pldi/benchmarks/benchmark1/clients.sh localhost 8888 9999 RMIBenchmarkObject 1 

sessionjcvs_dir=~/wrk/eclipse/sessionj-cvs
sj_dir=~/wrk/eclipse/sj


if [ $# -ne 5 ]
then
  echo 'Expected arguments: [hostname, port_a, port_b, name, iters]; not:' $@
  exit 1
fi 

hostname=$1
port_a=$2 
port_b=$3
name=$4
iters=$5


depths=( 0 1 2 4 8 )
numdepths=${#depths[@]}


for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark1a: depth=$depth 

  if [ $d -eq $(( $numdepths - 1 )) ]
  then
    tests/src/pldi/benchmarks/benchmark1/a/client.sh $hostname $port_a $depth -r $iters -k
  else
    tests/src/pldi/benchmarks/benchmark1/a/client.sh $hostname $port_a $depth -r $iters
  fi
done


bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000


cd "$sj_dir"

for (( d = 0; d < $numdepths; d++ ))
do
  depth=${depths[d]}

 echo benchmark1b: depth=$depth 

  if [ $d -eq $(( $numdepths - 1 )) ]
  then
    tests/src/pldi/benchmarks/benchmark1/b/client.sh $hostname $port_b $depth -r $iters -k
  else
    tests/src/pldi/benchmarks/benchmark1/b/client.sh $hostname $port_b $depth -r $iters
  fi
done


cd "$sessionjcvs_dir"


bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000


for (( d = 0; d < $numdepths; d++ ))
do
  depth=${depths[d]}

  echo benchmark1c: depth=$depth

  if [ $d -eq $(( $numdepths - 1 )) ]
  then 
    tests/src/pldi/benchmarks/benchmark1/c/client.sh $hostname $name $depth -r $iters -k
  else
    tests/src/pldi/benchmarks/benchmark1/c/client.sh $hostname $name $depth -r $iters
  fi
done
