#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/popl/bmarks/bmark1/clients.sh a localhost 6666 7777 1 


a=false
b=false
c=false
d=false

while true;
do
  case $1 in
    all) 
      a=true
      b=true
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
    *)
      break
      ;;
  esac
done

if [ $# -ne 4 ]
then
  echo 'Expected arguments: [hostname, port_a, port_b, iters]; not:' $@
  exit 1
fi 

hostname=$1
port_a=$2 
port_b=$3
iters=$4


sizes=( 0 1 10 100 1000 10000 )
numsizes=${#sizes[@]}


if [ "$a" == "true" ];
then
  for transport in b f
  do
    for (( x = 0; x < $numsizes; x++ )) 
    do
      size=${sizes[x]}

      echo bmark1a: transport=$transport size=$size

      tests/src/popl/bmarks/bmark1/a/ordinary.sh $port_a $transport $size -r $iters
    done
  done
  
  bin/sessionj -cp tests/classes/ popl.bmarks.Pause 5000    
fi


if [ "$b" == "true" ];
then
  for transport in b f
  do
    for (( x = 0; x < $numsizes; x++ )) 
    do
      size=${sizes[x]}

      echo bmark1b: transport=$transport size=$size 

      tests/src/popl/bmarks/bmark1/b/noalias.sh $port_b $transport $sizes -r $iters
    done
  done

  bin/sessionj -cp tests/classes/ popl.bmarks.Pause 5000
fi
