#!/bin/bash

#$ tests/src/benchmarks/benchmark3/bobs.sh all 8888 localhost 9999 localhost 2

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
      if [ $# -ne 5 ]
      then
        echo 'Expected arguments: [port_bob, carol, port_carol, alice, iters]; not:' $@
        exit 1
      fi 

      port_bob=$1
      carol=$2
      port_carol=$3
      alice=$4
      #port_alice=$5
      iters=$5    
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
  bin/sessionj -cp tests/classes/ benchmarks.benchmark3.b.Bob false $port_bob $carol $port_carol
done
fi


if [ "$c" == "true" ]
then 
echo benchmark3c:

for (( i = 0; i < $restart; i++ ))
do
  bin/sessionj -cp tests/classes/ benchmarks.benchmark3.c.Bob false $port_bob $carol $port_carol
done
fi


if [ "$a" == "true" ]
then 
echo benchmark3a:

for (( i = 0; i < $restart; i++ ))
do
  bin/sessionj -cp tests/classes/ benchmarks.benchmark3.a.Bob false $port_bob $alice $port_carol
done
fi
