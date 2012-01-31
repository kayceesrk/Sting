#!/usr/bin/env python
import sys, socket
import os

#create an INET, STREAMing socket
serversocket = socket.socket(
socket.AF_INET, socket.SOCK_STREAM)
#bind the socket to a public host and a well-known port
serversocket.bind((socket.gethostname(), 4321))
#become a server socket
serversocket.listen(5)

#accept connections
(s, address) = serversocket.accept()

clients = ['20', '50', '100', '150', '200', '250', '350']
sessionLength = ['10', '20', '30']
msgSize = ['1024', '2048', '4096']
hostname = socket.gethostname()

for i in clients:
  for j in sessionLength:
    for k in msgSize:
      data = s.recv(1024);
      command = 'java Client camelot16 2000 ' + i + ' ' + j + ' ' + k + ' > ' + hostname + '.' + i + '.' + j + '.' + k
      os.system(command)


