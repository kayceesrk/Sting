#!/bin/bash

# Run from the sessionj root directory:
#$ rmiregistry &
#$ tests/src/benchmarks/benchmark1/servers.sh a 8888 9999 RMIBenchmarkObject 


sj_dir=../sj-prev # Assumes relative position of directory for preceding SJ version.
sessionj_dir=../sessionj 


codebase="$sessionj_dir"/tests/classes/

security_policy="$sessionj_dir"/tests/src/benchmarks/benchmark1/c/security.policy


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

if [ $# -ne 3 ]
then
  echo 'Expected arguments: [port_a, port_b, name]; not:' $@
  exit 1
fi 

port_a=$1
port_b=$2
name=$3


if [ "$a" == "true" ];
then
  echo benchmark1a:

  bin/sessionj -cp tests/classes/ benchmarks.benchmark1.a.Server false $port_a 
fi


if [ "$b" == "true" ];
then
  echo benchmark1b:

  cd "$sj_dir"

  bin/sj -cp tests/classes/ benchmarks.benchmark1.b.Server false $port_b 

  cd "$sessionj_dir"
fi


if [ "$c" == "true" ];
then
  echo benchmark1c:

  #if [ `uname | grep -c CYGWIN` -ne 0 ]
  
  bin/sessionj -cp tests/classes/ "-Djava.rmi.server.codebase=file://$codebase" "-Djava.security.policy=file://$security_policy" benchmarks.benchmark1.c.ServerImpl false $name
fi

if [ "$d" == "true" ];
then
  echo benchmark1d:
  
  bin/sessionj -cp tests/classes/ benchmarks.benchmark1.d.Server false $port_a
fi


#if [ "$e" == "true" ];
#then
#  echo benchmark1e:
#fi
