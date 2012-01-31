#!/usr/bin/env python

##
# tests/src/thesis/benchmark/bmark2/bin/server.py false LOCALHOST 8888 localhost 7777 ORDINARYm 2
# tests/src/thesis/benchmark/bmark2/bin/server.py false CAMELOT 8888 camelot14 7777 ALL 2
##

import socket
import sys
import time

import common 


##
# Command line arguments.
##
if len(sys.argv) != 8:
	common.runtime_error('Usage: server.py <debug> <env> <server_port> <client> <client_port> <version> <repeats>')
debug   = common.parse_boolean(sys.argv[1]) 
env     = sys.argv[2]
sport   = sys.argv[3]
client  = sys.argv[4]      # Use "localhost" if env is localhost
cport   = int(sys.argv[5])
version = sys.argv[6]
repeats = int(sys.argv[7]) # "Outer repeats", i.e. how many times we will recreate the Server per parameter configuration


##
# Main execution command.
## 
if (env == 'LOCALHOST' or env == 'DOC'):
	renv = "bin/sessionj -j '-server' -J " + common.JAVA
elif env == 'CAMELOT':
	renv = "bin/sessionj -j '-server' -J " + common.CAMELOT_JAVA
else:
	common.runtime_error('Bad environment: ' + env)
	

##
# Benchmark configuration parameters.
##
if version == 'ALL':
	versions = common.ALL_VERSIONS				
else:
	versions = [version]
	
if debug:
	(message_sizes, session_lengths) = common.get_debug_parameters()
else:
	(message_sizes, session_lengths) = common.get_parameters()


##
# Run one Server instance.
## 
server_warmup = common.SERVER_WARMUP 
cool_down = common.SERVER_COOLDOWN

def run_server(debug, client_socket, command):								
	common.debug_print(debug, 'Command: ' + command)														
	ct = common.CommandThread(command)
	ct.start()																					
	time.sleep(server_warmup)	# Not really a warmup (dummy run done with client); more like setup (ensure server socket is open etc.)																					
	client_socket.send('1') # Notify client script that the Server is ready													
	ct.join()																						
	time.sleep(cool_down)   # Make sure everything has been shut down and the server port has become free again 
	

##
# Main. (This script needs to be refactored into main plus aux. functions.)
##
common.print_and_flush('Global: renv=' + renv + ', versions=' + str(versions) + ', message_sizes=' + str(message_sizes) + ', session_lengths=' + str(session_lengths) + ', repeats=' + str(repeats))
	
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((client, cport))	
	
for v in versions:
	transport = ''
	if (v.startswith('NOALIAS') or v.startswith('ORDINARY')):
		transport = v[len(v)-1]
		v = v[0:len(v)-1]
	else: 
		common.runtime_error('Bad flag: ' + v)
	
	for size in message_sizes:
		for length in session_lengths:														
			command = renv									
			if debug:
				command += ' -V'													
			if transport == 'm':
				command += ' -Dsessionj.transports.negotiation=' + transport \
			           + ' -Dsessionj.transports.session=' + transport \
		  	         + ' -cp tests/classes thesis.benchmark.bmark2.ServerRunner ' \
		             + str(debug) \
		             + ' ' + sport \
		             + ' ' + v
				for i in range(0, repeats):
					common.print_and_flush('Parameters: version=' + v + transport + ', size=' + size + ', length=' + length + ', repeat=' + str(i))
					run_server(debug, client_socket, command)		             
			elif transport != 'f': 
				common.runtime_error('Missing transport.')																						
