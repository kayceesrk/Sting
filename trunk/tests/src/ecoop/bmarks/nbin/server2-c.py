#!/usr/bin/python
import sys, socket
import os
import time
from threading import Thread


def connect(first, last, port):
	sockets = []
	for i in range(first, last):
		# Create an INET, STREAMing socket.
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		if i < 10:
			host = '0' + str(i)
		else:
			host = str(i)
		# Now connect to the target host.
		s.connect(('camelot' + host, port))
		sockets.append(s)
	return sockets

def send(sockets, value):
	for s in sockets:
		s.send(value)

def spawnThread(command):
	os.system(command)
	print 'Thread finished'


# tests/src/ecoop/bmarks/bin/server.py <debug> <num_machines> <server_port> <client_port> <version> 
# tests/src/ecoop/bmarks/bin/server.py f 10 2000 4321 JT 

if len(sys.argv) < 6:
	print 'Usage: server.py <debug> <num_machines> <server_port> <client_port> <version>'
	sys.exit(1)

debug = sys.argv[1]
machines = int(sys.argv[2])
sport = sys.argv[3]
cport = int(sys.argv[4])
version = sys.argv[5]


versions = []
clients = []
msgSizes = []
sessionLengths = []

if version == 'ALL':
	versions = ['SE', 'JE', 'JT', 'ST']
else:
	versions = [version]

if debug == 't':	
	clients = [str(machines), str(2*machines)]
	msgSizes = ['10', '100']
	sessionLengths = ['0', '1', '10']
else:
	clients = [str(90*machines)]
	msgSizes = ['100', '1000']
	#sessionLengths = ['1', '10', '100', '1000']


sockets = connect(2, 2 + machines, cport)

s1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s1.connect(("camelot01", cport))
	

for v in versions:
	for i in clients:
		for j in msgSizes:
			
			print 'Benchmark: version=' + v + ', clients=' + i + ', msgSize=' + j
			sys.stdout.flush()

			if v == 'SE':
				transport = ' -Dsessionj.transports.session=a '
			else:
				transport = ' '
       
			command = '/opt/util-linux-ng-2.17-rc1/schedutils/taskset 0x00000001 bin/csessionj' + transport + '-cp tests/classes ecoop.bmarks.ServerRunner2 false ' + sport + ' ' + i + ' ' + v
       
			if debug == 't':
				print 'Running: ' + command
       
       #thread.start_new_thread(spawnThread,(command,))
			thread1 = Thread(target=spawnThread, args=(command,))
			thread1.start()
       
			time.sleep(4) # Make sure Server has started.
       
			send(sockets, '1')
       
			if i == '500':
				time.sleep(25) # Make sure LoadClients are warmed up.
			else:
				time.sleep(10)
       
			s1.send('1')
       
			thread1.join()

			time.sleep(4)
