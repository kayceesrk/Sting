#!/bin/csh

@ x = 0
while ($x < $4)
  foreach i (0 1 10 100 1000)
    foreach j (1)
      java -cp tests/classes/ benchmarks.rmi.RMIClient $1 $2 $3 $i $j
    end
  end
@ x = $x + 1
end
