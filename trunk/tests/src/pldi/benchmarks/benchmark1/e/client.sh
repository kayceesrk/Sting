#!/bin/bash

debug=false
kill=false
repeat=1
args=
numargs=0

while true;
do
  case $1 in
    "")
      break
      ;;
    -d) 
      debug="true"
      shift
      ;;
    -k) 
      kill=true
      shift
      ;;      
    -r)
      shift
      repeat=$1
      shift
      ;;
    *)
      args="$args $1"
      numargs=$(($numargs + 1))
      shift
      ;;
  esac
done

if [ $numargs -ne 3 ]
then
  echo 'Expected arguments: [hostname, port, size]; not:' $args
  exit 1
fi 


for (( k = 0; k < $repeat; k = k + 1 ))
do
  for j in 1
  do
    for i in 0 1 10 100 1000
    do
      bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.e.Client $debug $args $i $j  
    done                           
  done
done


if $kill = true;
then
  bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.a.Kill $args
fi
