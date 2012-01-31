#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/benchmarks/benchmark1/clients.sh a localhost 8888 9999 RMIBenchmarkObject 1 


sj_dir=../sj-prev # Assumes relative position of directory for preceding SJ version.
sessionj_dir=../sessionj 


a=false
b=false
c=false
d=false
e=false

while true;
do
  case $1 in
    all) 
      a=true
      b=true
      c=true
      d=true
      e=true
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
    e)
      e=true
      shift
      ;;      
    *)
      break
      ;;
  esac
done

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


if [ "$a" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ )) 
  do
    depth=${depths[x]}

    echo benchmark1a: depth=$depth 

    if [ $x -eq $(( $numdepths - 1 )) ]
    then
      tests/src/benchmarks/benchmark1/a/client.sh $hostname $port_a s $depth -r $iters -k
    else
      tests/src/benchmarks/benchmark1/a/client.sh $hostname $port_a s $depth -r $iters
    fi
  done
  
  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi


if [ "$b" == "true" ];
then
  cd "$sj_dir"

  for (( x = 0; x < $numdepths; x++ ))
  do
    depth=${depths[x]}

    echo benchmark1b: depth=$depth 

    if [ $x -eq $(( $numdepths - 1 )) ]
    then
      tests/src/benchmarks/benchmark1/b/client.sh $hostname $port_b $depth -r $iters -k
    else
      tests/src/benchmarks/benchmark1/b/client.sh $hostname $port_b $depth -r $iters
    fi
  done

  cd "$sessionj_dir"
  
  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi


if [ "$c" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ ))
  do
    depth=${depths[x]}

    echo benchmark1c: depth=$depth

    if [ $x -eq $(( $numdepths - 1 )) ]
    then 
      tests/src/benchmarks/benchmark1/c/client.sh $hostname $name $depth -r $iters -k
    else
      tests/src/benchmarks/benchmark1/c/client.sh $hostname $name $depth -r $iters
    fi
  done
  
  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi


if [ "$d" == "true" ];
then
  for (( x = 0; x < $numdepths; x++ ))
  do
    depth=${depths[x]}

    echo benchmark1d: depth=$depth

    if [ $x -eq $(( $numdepths - 1 )) ]
    then 
      tests/src/benchmarks/benchmark1/d/client.sh $hostname $port_a $depth -r $iters -k
    else
      tests/src/benchmarks/benchmark1/d/client.sh $hostname $port_a $depth -r $iters
    fi
  done

  bin/sessionj -cp tests/classes/ benchmarks.Pause 5000
fi
 
#if [ "$e" == "true" ];
#then
  
#fi
