#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/benchmarks/benchmark2/servers.sh c d 8888 9999 


c=false
d=false

while true;
do
  case $1 in
    all) 
      c=true
      d=true
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

if [ $# -ne 2 ]
then
  echo 'Expected arguments: [port_a, port_b]; not:' $@
  exit 1
fi 

port_a=$1
port_b=$2


if [ "$c" == "true" ];
then
  echo benchmark2c:

  bin/sessionj -cp tests/classes/ benchmarks.benchmark2.c.Server $port_a 
fi

if [ "$d" == "true" ];
then
  echo benchmark2d:

  bin/sessionj -cp tests/classes/ benchmarks.benchmark2.d.Server $port_b 
fi
