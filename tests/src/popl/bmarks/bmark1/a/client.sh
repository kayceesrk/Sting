#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/popl/bmarks/bmark1/a/client.sh localhost 8888 s 1024 -r 2 -k


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
  echo 'Expected arguments: [hostname, port, transport, size]; not:' $args
  exit 1
fi 


lens=( 0 1 10 100 1000 )
#buffers=( 7 7 34 304 3004 )
buffers=( 7 7 7 7 7 )
numlens=${#lens[@]}

for (( k = 0; k < $repeat; k = k + 1 ))
do
  for j in 1
  do
    for(( i = 0; i < $numlens; i = i + 1 ))
    do
      len=${lens[i]}
      buffer=${buffers[i]} 
    
      bin/sessionj -cp tests/classes/ popl.bmarks.bmark1.a.Client $debug $args $len $buffer $j  
    done                           
  done
done


if $kill = true;
then
  bin/sessionj -cp tests/classes/ popl.bmarks.Kill $args
fi
