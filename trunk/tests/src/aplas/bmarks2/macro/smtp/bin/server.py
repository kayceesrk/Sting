#!/usr/bin/python
#
#tests/src/ecoop/bmarks2/macro/smtp/bin/server.py <debug> <env> <server_port> <worker_port> <client_port> <version> <repeats>
#tests/src/ecoop/bmarks2/macro/smtp/bin/server.py f localhost 8888 7777 6666 ST 2

import socket
import sys
import time

import common 


# Function declarations.

def connectToWorkers(debug, workers, wport):
	loadClients = []
	
	for worker in workers:
		common.debugPrint(debug, 'Connecting to Worker at ' + worker + ':' + str(wport))
	
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.connect((worker, wport))
		loadClients.append(s)
		
	return loadClients


# Command line arguments.

if len(sys.argv) != 8:
	common.printAndFlush('Usage: server.py <debug> <env> <server_port> <worker_port> <client_port> <version> <repeats>')
	sys.exit(1)

debug = common.parseBoolean(sys.argv[1])
env = sys.argv[2] # e.g. 'localhost' or 'camelot' 
sport = sys.argv[3]
wport = int(sys.argv[4]) # For load clients.
cport = int(sys.argv[5]) # For the timerClient/counter client.
version = sys.argv[6]
repeats = int(sys.argv[7]) # "Outer repeats", i.e. how many times we will recreate the Server per parameter configuration.


# Benchmark configuration parameters.

if version == 'ALL':
	versions = common.versions				
else:
	versions = [version]

if env == 'localhost':
	renv = 'bin/sessionj' # Runtime environment.

	client = common.getLocalhostClient() 
	workers = common.getLocalhostWorkers() 
	
	(numClients, messageSizes) = common.getLocalhostParameters()
elif env == 'camelot':
	renv = 'bin/csessionj'

	client = common.getCamelotClient() 
		
	if debug:
		workers = common.getCamelotDebugWorkers() 
		(numClients, messageSizes) = common.getDebugParameters()
	else:
		workers = common.getCamelotWorkers() 
		(numClients, messageSizes) = common.getParameters()	
else:
	common.printAndFlush('Unknown environment: ' + env)
	sys.exit(1)

# Seconds.
serverWarmup = 3 
workerWarmup = 3
coolDown = 3
loadSpinWarmup = 10


# Main.

common.printAndFlush('Configuration: server=' + socket.gethostname() + ', workers=' + str(workers) + ', client=' + client)
common.printAndFlush('Global: versions=' + str(versions) + ', numClients=' + str(numClients) + ', messageSizes=' + str(messageSizes))

loadClients = connectToWorkers(debug, workers, wport)

common.debugPrint(debug, 'Connecting to Client at ' + client + ':' + str(cport))

timerClient = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
timerClient.connect((client, cport))
	
common.debugPrint(debug, 'Starting main loop...')
	
for v in versions:
	for clients in numClients:
		for size in messageSizes:
			for i in range(0, repeats):
				common.printAndFlush('Parameters: version=' + v + ', clients=' + clients + ', size=' + size + ', trial=' + str(i))
								
				command = renv
				
				if env == 'camelot':
					#command = '/opt/util-linux-ng-2.17-rc1/schedutils/taskset 0x00000001 ' + command
					command = '/opt/util-linux-ng-2.17-rc1/schedutils/taskset 0x00000001 ' + command + ' -Xmx1024m'
			
				#command = command + ' -Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false'
				#command += ' -agentpath:/Applications/YourKit_Java_Profiler_8.0.19.app/bin/mac/libyjpagent.jnilib'
				
				if v == 'SE':
					transport = '-Dsessionj.transports.session=a '
					setup = 'a'
				else:
					transport = ''							
					setup = 's'
				
				command = command + ' ' + transport + '-cp tests/classes ecoop.bmarks2.macro.smtp.ServerRunner ' + str(debug) + ' ' + sport + ' ' + setup + ' ' + v + ' ' + str(len(workers))
		
				common.debugPrint(debug, 'Command: ' + command)
				
				ct = common.CommandThread(command)
				ct.start()
		
				time.sleep(serverWarmup) # Make sure Server has started.
					
				for s in loadClients:
					s.send('1')
					s.recv(1024); 
					time.sleep(workerWarmup) # Make sure DummyClients are properly connected and warmed up.
                
				time.sleep(loadSpinWarmup)
				timerClient.send('1')
					
				ct.join()
		
				time.sleep(coolDown) # Make sure everything has been shut down and the server port has become free again. 
