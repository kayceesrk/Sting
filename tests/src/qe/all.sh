#!/bin/bash

# Run from the sessionj root directory:
#$ tests/src/qe/all.sh -i 3 -o 2 -d  


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


session="es"
capacity="2" # Ideally should be 1 except for gsq (which needs capacity 2).

for chan in gsq bq ssq qecq
do
	#for size in 0 1 10 100 1000 
	for size in 100 1000 
	do
		#for len in 0 1 10 100 1000 
		for len in 10 100 1000 
		do        
			for (( k = 0; k < $outers; k = k + 1 ))
			do  
				echo LocalRun: session=$session chan=$chan size=$size len=$len outer=$k

				java -cp tests/classes/ qe.LocalRun $debug $chan $capacity $session $size $len $inners  
			done
		done
	done
done


session="ss"
capacity="1001" # Ideally should be len + 1 (and at least 2).

for chan in gsq bq ssq qecq
do
	#for size in 0 1 10 100 1000 
	for size in 100 1000 
	do
		#for len in 0 1 10 100 1000 
		for len in 10 100 1000 
		do        		
			for (( k = 0; k < $outers; k = k + 1 ))
			do  
				echo LocalRun: session=$session chan=$chan size=$size len=$len outer=$k

				java -cp tests/classes/ qe.LocalRun $debug $chan $capacity $session $size $len $inners  
			done
		done
	done
done


session="es"
capacity="1" 

#for size in 0 1 10 100 1000 
for size in 100 1000 
do
	#for len in 0 1 10 100 1000 
	for len in 10 100 1000 
	do        
		for (( k = 0; k < $outers; k = k + 1 ))
		do  
			echo QELocalRun: session=$session size=$size len=$len outer=$k

			java -cp tests/classes/ qe.QELocalRun $debug $capacity $session $size $len $inners  
		done
	done
done


session="ss"
capacity="1000" 

#for size in 0 1 10 100 1000 
for size in 100 1000 
do
	#for len in 0 1 10 100 1000 
	for len in 10 100 1000 
	do        
		for (( k = 0; k < $outers; k = k + 1 ))
		do  
			echo QELocalRun: session=$session size=$size len=$len outer=$k

			java -cp tests/classes/ qe.QELocalRun $debug $capacity $session $size $len $inners  
		done
	done
done
