#!/bin/bash

#$ tests/src/pldi/benchmarks/benchmark3/alices.sh all localhost 8888 localhost 9999 4 2

a=false
b=false
c=false

while true;
do
  case $1 in
    all) 
      a=true
      b=true
      c=true
      shift
      ;;
    a) 
      a=true
      shift
      ;;      
    b)
      b=true
      shift
      ;;
    c)
      c=true
      shift
      ;;      
    *)
      if [ $# -ne 6 ]
      then
        echo 'Expected arguments: [bob, port_bob, carol, port_carol, iters, restart]; not:' $@
        exit 1
      fi 

      bob=$1
      port_bob=$2 
      carol=$3
      port_carol=$4
      iters=$5

      let "restart = $6 - 1"    
      break
      ;;
  esac
done


#depths=( 0 1 2 4 8 )
depths=( 0 1 2 4 )
numdepths=${#depths[@]}


let "i = 0";

if [ "$b" == "true" ]
then 
for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark3b: depth=$depth 

	for (( j = 0; j < $iters; j++ ))
	do
    tests/src/pldi/benchmarks/benchmark3/b/alice.sh $bob $port_bob $depth

    if [ $i -eq $restart ]
    then  
  	  bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3.b.Kill $bob $port_bob 0 $carol $port_carol
	  
	    bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 2000
  
      let "i = 0"
    else
      let "i += 1"  
    fi      
  done
done

bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000

fi


if [ "$c" == "true" ]
then 
for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}

  echo benchmark3c: depth=$depth 

	for (( j = 0; j < $iters; j++ ))
	do
    tests/src/pldi/benchmarks/benchmark3/c/alice.sh $bob $port_bob $depth

    if [ $i -eq $restart ]
    then  
  	  bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3.c.Kill $bob $port_bob 0 $carol $port_carol
	  
	    bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 2000
  
      let "i = 0"
    else
      let "i += 1"  
    fi      
  done
done

bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 5000

fi


if [ "$a" == "true" ]
then 
let "x = restart + 1"

for (( d = 0; d < $numdepths; d++ )) 
do
  depth=${depths[d]}
  #depth=1

  echo benchmark3a: depth=$depth 

  for (( j = 0; j < $iters; j += x ))
	do
    bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3/a.AliceCarol false $port_carol $bob $port_bob $depth $x
	  
    bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 2000     
  done
done
fi
