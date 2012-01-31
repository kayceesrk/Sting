#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/popl/bmarks/bmark2/b/all.sh -i 3 -o 2  


debug=false 
inners=1
outers=1
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
    -i)
      shift
      inners=$1
      shift
      ;;           
    -o)
      shift
      outers=$1
      shift
      ;;     
    *)
      args="$args $1"
      numargs=$(($numargs + 1))
      shift
      ;;
  esac
done

if [ $numargs -ne 0 ]
then
  echo 'Unexpected arguments: ' $args
  exit 1
fi 


for session in 1 2
do  
  #for chan in w o n r s
  for chan in w o n s
  do
    for size in 0 1 10 100 1000 10000
    do
      #echo bmark2b: session=$session chan=$chan size=$size 

      for len in 0 1 10 100 1000 
      do        
        for (( k = 0; k < $outers; k = k + 1 ))
        do  
          echo bmark2b: session=$session chan=$chan size=$size len=$len outer=$k
            
          bin/sessionj -cp tests/classes/ popl.bmarks.bmark2.b.LocalRun $debug $chan $session $size $len $inners  
        done
      done
    done
  done
done
