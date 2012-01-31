#!/usr/bin/env python
#tests/src/ecoop/bmarks2/micro/bin/throughput.py <debug> <env> <serverName> <server_port> <client_port> <version> <inners> <outers>
#tests/src/ecoop/bmarks2/micro/bin/throughput.py f localhost localhost 8888 6666 JT 3 2
#nohup tests/src/ecoop/bmarks2/micro/bin/throughput.py f camelot camelot16 8888 6666 JT 3 2 < /dev/null 1>foo.txt 2>bar.txt &	

import os
import socket
import sys
import time

import common


if len(sys.argv) != 9:
	common.printAndFlush('Usage: timer.py <debug> <env> <serverName> <server_port> <client_port> <version> <inners> <outers>')
	sys.exit(1)
	
debug = common.parseBoolean(sys.argv[1])
env = sys.argv[2] # e.g. 'localhost' or 'camelot'
serverName = sys.argv[3]
sport = sys.argv[4]
cport = int(sys.argv[5])
version = sys.argv[6]
inners = int(sys.argv[7])
outers = int(sys.argv[8]) # This is the parameter called "repeats" in the other scripts. 


# Benchmark configuration parameters.

if version == 'ALL':
	versions = common.versions				
else:
	versions = [version]

if env == 'localhost':
	renv = 'bin/sessionj'

	hostname = 'localhost'	  
	client = common.getLocalhostClient() 
	workers = common.getLocalhostWorkers() 
	
	(numClients, messageSizes, sessionLengths) = common.getLocalhostParameters()
elif env == 'camelot':
	renv = 'bin/csessionj'

	hostname = socket.gethostname()
	client = common.getCamelotClient() 
		
	if debug:
		workers = common.getCamelotDebugWorkers() 
		(numClients, messageSizes, sessionLengths) = common.getDebugParameters()
	else:
		workers = common.getCamelotWorkers() 
		(numClients, messageSizes, sessionLengths) = common.getParameters()	
else:
	common.printAndFlush('Unknown environment: ' + env)
	sys.exit(1)

if debug:
	window = 3
else:
	window = 30


# Main.

common.printAndFlush('Configuration: server=' + serverName + ', client=' + hostname)
common.printAndFlush('Global: window=' + str(window) + ', versions=' + str(versions) + ', numClients=' + str(numClients) + ', messageSizes=' + str(messageSizes) + ', sessionLengths=' + str(sessionLengths))

serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serverSocket.bind((hostname, cport))
serverSocket.listen(5) # 5 seems to be a kind of default.

common.debugPrint(debug, 'Listening on port: ' + str(cport))	

(s, address) = serverSocket.accept()

common.debugPrint(debug, 'Server script connected, starting main loop...')

for v in versions:
	for clients in numClients:
		for size in messageSizes:
			for length in sessionLengths:
				for i in range(0, outers):
					common.printAndFlush('Parameters: version=' + v + ', clients=' + clients + ', size=' + size + ', length=' + length + ', trial=' + str(i))
		
					s.recv(1024)
					
					prefix = renv + ' -cp tests/classes ecoop.bmarks2.micro.SignalClient ' + serverName + ' ' + sport 
					
					count = prefix + ' COUNT'
					stop = prefix + ' STOP'
					kill = prefix + ' KILL'
							
					for i in range(0, inners):
						common.debugPrint(debug, 'Command: ' + count)	
						os.system(count) 		
										
						time.sleep(window)				
										
						common.debugPrint(debug, 'Command: ' + stop)	
						os.system(stop)		
						
						time.sleep(0.05) # Small cool down time.	
									
					common.debugPrint(debug, 'Command: ' + kill)	
					os.system(kill)
					