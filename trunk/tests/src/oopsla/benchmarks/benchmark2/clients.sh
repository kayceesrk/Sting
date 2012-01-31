#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/benchmarks/benchmark2/clients.sh a localhost 6666 7777 8888 9999 1 


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
      c=true
      d=true
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
    d)
      d=true
      shift
      ;;      
    *)
      break
      ;;
  esac
done

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


if [ "$a" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ )) 
  do
    depth=${depths[x]}

    echo benchmark2a: depth=$depth 

    tests/src/benchmarks/benchmark2/a/noalias.sh $port_a $depth -r $iters
  done
  
  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi


if [ "$b" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ )) 
  do
    depth=${depths[x]}

    echo benchmark2b: depth=$depth 

    tests/src/benchmarks/benchmark2/b/ordinary.sh $port_b $depth -r $iters
  done

  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi


if [ "$c" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ )) 
  do
    depth=${depths[x]}

    echo benchmark2c: depth=$depth 

    if [ $x -eq $(( $numdepths - 1 )) ]
    then
      tests/src/benchmarks/benchmark2/c/client.sh $hostname $port_c $depth -r $iters -k
    else
      tests/src/benchmarks/benchmark2/c/client.sh $hostname $port_c $depth -r $iters
    fi
  done

  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi


if [ "$d" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ )) 
  do
    depth=${depths[x]}

    echo benchmark2d: depth=$depth 

    if [ $x -eq $(( $numdepths - 1 )) ]
    then
      tests/src/benchmarks/benchmark1/a/client.sh $hostname $port_d s $depth -r $iters -k
    else
      tests/src/benchmarks/benchmark1/a/client.sh $hostname $port_d s $depth -r $iters
    fi
  done
fi
