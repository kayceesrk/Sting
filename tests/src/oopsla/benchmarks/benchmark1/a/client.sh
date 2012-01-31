#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/benchmarks/benchmark1/a/client.sh localhost 8888 s 0


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

if [ $numargs -ne 4 ]
then
  echo 'Expected arguments: [hostname, port, transport, depth]; not:' $args
  exit 1
fi 


for (( k = 0; k < $repeat; k = k + 1 ))
do
  for j in 1
  do
    for i in 0 1 10 100 1000
    do
      bin/sessionj -cp tests/classes/ benchmarks.benchmark1.a.Client $debug $args $i $j  
    done                           
  done
done


if $kill = true;
then
  bin/sessionj -cp tests/classes/ benchmarks.Kill $args
fi
