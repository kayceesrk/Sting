#!/bin/bash

#$ rmiregistry &
#$ tests/src/pldi/benchmarks/benchmark1/servers.sh 8888 9999 RMIBenchmarkObject 

sessionjcvs_dir=~/wrk/eclipse/sessionj-cvs
sj_dir=~/wrk/eclipse/sj

windows_codebase='c:/cygwin/home/Raymond/wrk/eclipse/sessionj-cvs/tests/classes/'

windows_policy='c:/cygwin/home/Raymond/wrk/eclipse/sessionj-cvs/tests/src/pldi/benchmarks/benchmark1/c/security.policy.windows'

linux_codebase='/homes/rh105/wrk/eclipse/sessionj-cvs/tests/classes/'

linux_policy='/homes/rh105/wrk/eclipse/sessionj-cvs/tests/src/pldi/benchmarks/benchmark1/c/security.policy.linux'


if [ $# -ne 3 ]
then
  echo 'Expected arguments: [port_a, port_b, name]; not:' $@
  exit 1
fi 

port_a=$1
port_b=$2
name=$3


echo benchmark1a:

bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.a.Server false $port_a 


echo benchmark1b:

cd "$sj_dir"

bin/sj -cp tests/classes/ pldi.benchmarks.benchmark1.b.Server false $port_b 


cd "$sessionjcvs_dir"


echo benchmark1c:

if [ `uname | grep -c CYGWIN` -ne 0 ]
then 
  bin/sessionj -cp tests/classes/ "-Djava.rmi.server.codebase=file:///$windows_codebase" "-Djava.security.policy=file:///$windows_policy" pldi.benchmarks.benchmark1.c.ServerImpl false $name 
else
  bin/sessionj -cp tests/classes/ "-Djava.rmi.server.codebase=file://$linux_codebase" "-Djava.security.policy=file://$linux_policy" pldi.benchmarks.benchmark1.c.ServerImpl false $name
fi
