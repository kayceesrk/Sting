#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/benchmarks/benchmark3/carols.sh all 9999 2

b=false
c=false

while true;
do
  case $1 in
    all) 
      b=true
      c=true
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
      if [ $# -ne 2 ]
      then
        echo 'Expected arguments: [port_carol, iters]; not:' $@
        exit 1
      fi 

      port_carol=$1
      iters=$2    
      break
      ;;
  esac
done


depths=( 0 1 2 4 8 )
#depths=( 0 1 2 4 )
numdepths=${#depths[@]}


#let "restart = iters / 100"
let "restart = iters * $numdepths"

#if [ $restart -lt 1 ]
#then
#  let "restart = 1"
#fi


if [ "$b" == "true" ]
then 
echo benchmark3b:

for (( i = 0; i < $restart; i++ ))
do
	bin/sessionj -cp tests/classes/ benchmarks.benchmark3.b.Carol false $port_carol
done
fi


if [ "$c" == "true" ]
then 
echo benchmark3c:

for (( i = 0; i < $restart; i++ ))
do
	bin/sessionj -cp tests/classes/ benchmarks.benchmark3.c.Carol false $port_carol
done
fi
