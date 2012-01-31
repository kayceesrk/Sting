#!/usr/bin/env python
import sys, socket
import os, time

# tests/src/ecoop/bmarks/load.py <debug> <host> <server_port> <client_port> <version> 
# tests/src/ecoop/bmarks/load.py f camelot16 2000 4321 JT 

if len(sys.argv) < 6:
  print 'Usage: load.py <debug> <host> <server_port> <client_port> <version>'
  sys.exit(1)
  
debug = sys.argv[1]
host = sys.argv[2]
sport = sys.argv[3]
cport = int(sys.argv[4])
version = sys.argv[5]


versions = []
clients = []
msgSizes = []
sessionLengths = []

if version == 'ALL':
  versions = ['ST', 'SE']
else:
  versions = [version]

if debug == 't':	
  clients = ['1', '2']
  msgSizes = ['10', '100']
  sessionLengths = ['0', '1', '10']
else:
  #clients = ['10']
  clients = ['20']
  msgSizes = ['100', '1000']
#  msgSizes = ['1000']
#  sessionLengths = ['100']
  sessionLengths = ['1', '10', '100']

# Create an INET, STREAMing socket.
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((socket.gethostname(), cport))

serversocket.listen(5)

# Accept connection.
(s, address) = serversocket.accept()

for v in versions:
  for i in clients:
    for j in msgSizes:
      for k in sessionLengths:
        data = s.recv(1024);

        if v == 'SE':
          transport = ' -Dsessionj.transports.session=a '
        else: #elif v == 'ST' || v == 'SE':
          transport = ' '	
        
        command = 'bin/csessionj' + transport + '-cp tests/classes ecoop.bmarks.ClientRunner false ' + host + ' ' + sport + ' ' + i + ' ' + j + ' ' + v 
        
        if debug == 't':
          print 'Running: ' + command
          
        os.system(command)
        
