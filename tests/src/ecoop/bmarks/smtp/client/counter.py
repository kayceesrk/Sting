#!/usr/bin/env python
import sys, socket
import os, time

# tests/src/ecoop/bmarks/timer.py <debug> <host> <server_port> <client_port> <version> <num_repeats>
# tests/src/ecoop/bmarks/smtp/client/counter.py vector22 2525 10  

#if len(sys.argv) < 7:
#  print 'Usage: timer.py <debug> <host> <server_port> <client_port> <version> <num_repeats>'
#  sys.exit(1)
  
#debug = sys.argv[1]

host = sys.argv[1]
port = sys.argv[2]
repeats = int(sys.argv[3])

signalClient = 'bin/sessionj -cp tests/classes ecoop.bmarks.SignalClient ' + host + ' ' + port	      
  	        
count = signalClient + ' COUNT'
stop = signalClient + ' STOP'
kill = signalClient + ' KILL'
  
#if debug == 't':
#  print 'Running: ' + command
#  sys.stdout.flush()

for l in range(0, repeats):	
	print l
	sys.stdout.flush()

	os.system(count)      
  
	time.sleep(15)
  
	os.system(stop)
	
	time.sleep(2)	       	       	 
  
os.system(kill)
