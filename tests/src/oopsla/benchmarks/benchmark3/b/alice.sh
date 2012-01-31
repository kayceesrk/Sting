#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/benchmarks/benchmark3/b/alice.sh localhost 8888 0 -k localhost 9999


debug=false
kill=false
carol=
carol_port=
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
			carol=$1
			shift
			carol_port=$1
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
  echo 'Expected arguments: [bob, bob_port, depth]; not:' $args
  exit 1
fi 


for (( k = 0; k < $repeat; k = k + 1 ))
do
  #for j in 1
  #do
    for i in 0 1 10 100 1000
    do
      bin/sessionj -cp tests/classes/ benchmarks.benchmark3.c.Alice $debug $args $i #$j  
    done                           
  #done
done


if $kill = true;
then
  bin/sessionj -cp tests/classes/ benchmarks.benchmark3.c.KillBobCarol $args $carol $carol_port
fi
