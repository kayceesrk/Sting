#!/usr/bin/python

import os
import sys
from threading import Thread


# Benchmark configuration parameters (Camelot).

versions = ['ST', 'SE']

client = 'camelot01' # The Timer/Counter Client machine.


# "Full" configuration parameters (10 Workers).

workers = ['camelot02', 'camelot03', 'camelot04', 'camelot12', 'camelot06', 'camelot07', 'camelot08', 'camelot09', 'camelot10', 'camelot11'] # The Worker machines. # Load clients need to know how many Worker machines there are.

numClients = ['10', '100', '300', '500', '700', '900']
#numClients = ['300', '500', '700', '900']
#numClients = ['2000', '5000']
#messageSizes = ['1024', '10000']
messageSizes = ['1024']


# Benchmark debugging parameters (2 Workers).

debugWorkers = ['camelot02', 'camelot03']

debugNumClients = ['2', '4']	# Debug means for debugging these scripts: to play around with parameter settings, use the above (non-debug) parameters.
debugMessageSizes = ['1024']


# Localhost testing parameters.

localhostNumClients = ['1', '2']
localhostMessageSizes = ['100', '1024']


# Function declarations.

def getCamelotClient():
	return client

def getCamelotWorkers():
	return workers

def getCamelotDebugWorkers():
	return debugWorkers

def getParameters():
	return (numClients, messageSizes)

def getDebugParameters():
	return (debugNumClients, debugMessageSizes)

def getLocalhostClient():
	return 'localhost'

def getLocalhostWorkers():
	return ['localhost']

def getLocalhostParameters():
	return (localhostNumClients, localhostMessageSizes)


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
				
