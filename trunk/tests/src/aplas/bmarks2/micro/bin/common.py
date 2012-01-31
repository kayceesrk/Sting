#!/usr/bin/python

import os
import sys
from threading import Thread


# Benchmark configuration parameters (Camelot).

#versions = ['JT', 'JE', 'ST', 'SE']
#versions = ['JT', 'ST']

versions = ['ST', 'SE']

client = 'camelot01' # The Timer/Counter Client machine.


# "Full" configuration parameters (10 Workers).

#workers = ['camelot02', 'camelot03', 'camelot04', 'camelot05', 'camelot06', 'camelot07', 'camelot08', 'camelot09', 'camelot10', 'camelot11'] # The Worker machines. # Load clients need to know how many Worker machines there are.
workers = ['camelot02', 'camelot03', 'camelot04', 'camelot05', 'camelot06', 'camelot07', 'camelot08', 'camelot09', 'camelot14', 'camelot11'] 

#numClients = ['10', '100', '300', '500', '700', '900']
#messageSizes = ['100', '1024']
#sessionLengths = ['1', '10', '100'] # For response time benchmarks.
#sessionLengths = ['-1'] # For throughput benchmarks.

numClients = ['10', '100', '300', '500', '700', '900']
messageSizes = ['100','1024']
#sessionLengths = ['1','10','100']
sessionLengths = ['-1'] # For throughput benchmarks.


# Benchmark debugging parameters (2 Workers).

debugWorkers = ['camelot02', 'camelot03']

debugNumClients = ['2', '4']	# Debug means for debugging these scripts: to play around with parameter settings, use the above (non-debug) parameters.
debugMessageSizes = ['100', '10240']
#debugSessionLengths = ['0', '4'] # For response time benchmarks.
debugSessionLengths = ['-1'] # For throughput benchmarks.


# Localhost testing parameters.

localhostNumClients = ['1', '2']
localhostMessageSizes = ['100', '1024']
#localhostSessionLengths = ['0', '4'] # For response time benchmarks.
localhostSessionLengths = ['-1'] # For throughput benchmarks.

#localhostNumClients = ['0']
#localhostMessageSizes = ['100']
#localhostSessionLengths = ['10'] # For response time benchmarks.


# Function declarations.

def getCamelotClient():
	return client

def getCamelotWorkers():
	return workers

def getCamelotDebugWorkers():
	return debugWorkers

def getParameters():
	return (numClients, messageSizes, sessionLengths)

def getDebugParameters():
	return (debugNumClients, debugMessageSizes, debugSessionLengths)

def getLocalhostClient():
	return 'localhost'

def getLocalhostWorkers():
	return ['localhost']

def getLocalhostParameters():
	return (localhostNumClients, localhostMessageSizes, localhostSessionLengths)


def parseBoolean(v):
	return v.upper() == 'T'

def printAndFlush(msg):
	print msg
	sys.stdout.flush()

def debugPrint(debug, msg):
	if debug:
		printAndFlush(msg)
		

# Class declarations.

class CommandThread(Thread):
	def __init__(self, command):
		Thread.__init__(self)
		self.command = command

	def run(self):
		os.system(self.command)
		#common.printAndFlush('ServerThread finished.')
				
