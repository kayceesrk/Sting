#!/usr/bin/python
import sys, socket
import os
import time
from threading import Thread

def connect(first, last, port):
	sockets = []
	for i in range(first, last):
		#create an INET, STREAMing socket
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		if i < 10:
			host = '0' + str(i)
		else:
			host = str(i)
		#now connect to the server
		s.connect(('camelot' + host, port))
		sockets.append(s)
	return sockets

def send(sockets, value):
	for s in sockets:
		s.send(value)

def spawnThread(command):
	os.system(command)
	print 'Thread finished'


# tests/src/ecoop/bmarks/sj/cserver.py <debug> <num_machines> <server_port> <client_port> <num_repeats>
# tests/src/ecoop/bmarks/sj/cserver.py f 10 2000 4321 100

if len(sys.argv) < 6:
	print 'Usage: cserver.py <debug> <num_machines> <server_port> <client_port> <num_repeats>'
	sys.exit(1)

debug = sys.argv[1]
machines = int(sys.argv[2])
sport = sys.argv[3]
cport  = int(sys.argv[4])
repeats = int(sys.argv[5])


clients = []
msgSizes = []
sessionLengths = []

if debug == 't':	
	clients = [str(2 + machines), str(2 + 2*machines)]
	msgSizes = ['10', '100']
	sessionLengths = ['0', '1', '10']
else:
#	clients = [str(2 + machines), str(2 + 10*machines), str(2 + 100*machines)] 
	clients = [str(2 + 10*machines), str(2 + 100*machines)] 
	msgSizes = ['10', '100', '1000', '10000']
	sessionLengths = ['0', '1', '10', '100', '1000']


sockets = connect(2, 2 + machines, cport)

s1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s1.connect(("camelot01", cport))
	

for i in clients:	
	for k in msgSizes:
		for j in sessionLengths:
			for l in range(0, repeats):

				command = 'bin/csessionj -cp tests/classes ecoop.bmarks.sj.server.ServerRunner false ' + sport + ' ' + i

				if debug == 't':
					print 'Running: ' + command

					#thread.start_new_thread(spawnThread,(command,))
				thread1 = Thread(target=spawnThread, args=(command,))
				thread1.start()

				time.sleep(3) # Make sure Server has started.
					
				send(sockets, '1')
					
				time.sleep(10) # Make sure LoadClients are warmed up.

				s1.send('1')
					
				thread1.join()

				time.sleep(3)

