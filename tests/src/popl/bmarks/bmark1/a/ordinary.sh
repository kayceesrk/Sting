#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/popl/bmarks/bmark1/a/ordinary.sh 8888 b 1024 -r 2


debug=false 
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
  echo 'Expected arguments: [port, transports, size]; not:' $args
  exit 1
fi 


bin/sessionj -cp tests/classes/ popl.bmarks.bmark1.a.LocalRun $debug $args $repeat  
