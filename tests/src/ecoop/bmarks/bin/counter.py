#!/usr/bin/env python
import sys, socket
import os, time

# tests/src/ecoop/bmarks/timer.py <debug> <host> <server_port> <client_port> <version> <num_repeats>
# tests/src/ecoop/bmarks/timer.py f camelot16 2000 4321 JT 100  

if len(sys.argv) < 7:
  print 'Usage: timer.py <debug> <host> <server_port> <client_port> <version> <num_repeats>'
  sys.exit(1)
  
debug = sys.argv[1]
host = sys.argv[2]
sport = sys.argv[3]
cport = int(sys.argv[4])
version = sys.argv[5]
repeats = int(sys.argv[6])


versions = []
clients = []
msgSizes = []
sessionLengths = []
hostname = socket.gethostname()

if version == 'ALL':
  versions = ['SE', 'JE', 'JT', 'ST']
else:
  versions = [version]

if debug == 't':	
  clients = ['1', '2']
  msgSizes = ['10', '100']
  sessionLengths = ['0', '1', '10']
else:
  clients = ['1', '10', '50']
  msgSizes = ['100', '1000']
#  sessionLengths = ['1', '10', '100', '1000']


# Create an INET, STREAMing socket.
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((hostname, cport))

serversocket.listen(5)
	

# Accept connection.
(s, address) = serversocket.accept()

for v in versions:
  for i in clients:
    for j in msgSizes:
      for l in range(0, repeats):
	        
        data = s.recv(1024)
        if v == 'SE':
          transport = ' -Dsessionj.transports.session=a '
        else:
          transport = ' '
          
        signalClient = 'bin/csessionj' + transport + ' -cp tests/classes ecoop.bmarks.SignalClient ' + host + ' ' + sport	      
	        	        
        count = signalClient + ' COUNT'
        stop = signalClient + ' STOP'
        kill = signalClient + ' KILL'
	        
          #if debug == 't':
          #  print 'Running: ' + command
          #  sys.stdout.flush()
	
        os.system(count)      
        
        time.sleep(30)
        
        os.system(stop)
        os.system(kill)
