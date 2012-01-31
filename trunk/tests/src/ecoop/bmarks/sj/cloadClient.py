#!/usr/bin/env python
import sys, socket
import os, time

# tests/src/ecoop/bmarks/sj/cloadClient.py <debug> <host> <server_port> <client_port> <num_repeats>
# tests/src/ecoop/bmarks/sj/cloadClient.py f camelot16 2000 4321 100  


if len(sys.argv) < 6:
  print 'Usage: cloadClient.py <debug> <host> <server_port> <client_port> <num_repeats>'
  sys.exit(1)
  
debug = sys.argv[1]
host = sys.argv[2]
sport = sys.argv[3]
cport  = int(sys.argv[4])
repeats = int(sys.argv[5])


clients = []
msgSizes = []
sessionLengths = []

if debug == 't':	
  clients = ['1', '2']
  msgSizes = ['10', '100']
  sessionLengths = ['0', '1', '10']
else:
  #clients = ['1', '10', '100']
  clients = ['10', '100']
  msgSizes = ['10', '100', '1000', '10000']
  sessionLengths = ['0', '1', '10', '100', '1000']


#create an INET, STREAMing socket
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((socket.gethostname(), cport))

serversocket.listen(5)

#accept connection
(s, address) = serversocket.accept()


for i in clients:
  for k in msgSizes:
    for j in sessionLengths:
      for l in range(0, repeats):
        
        data = s.recv(1024);
        
        command = 'bin/csessionj -cp tests/classes ecoop.bmarks.sj.client.ClientRunner false ' + host + ' ' + sport + ' ' + i + ' ' + k 
        
        if debug == 't':
          print 'Running: ' + command
          
        os.system(command)
          
