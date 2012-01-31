#!/usr/bin/env python

import os
import sys
from threading import Thread


##
# "Environment"-dependent execution parameters
##
JAVA = 'java'
CAMELOT_JAVA = '~/bin/jdk1.6.0_21-64/bin/java'


##
# Benchmark configuration parameters.
##
ALL_VERSIONS = ['NOALIASm', 'ORDINARYm', 'NOALIASf', 'ORDINARYf']
#ALL_MESSAGE_SIZES = ['0', '1024', '10240'] # Original parameters for NoaliasMessage
ALL_MESSAGE_SIZES = ['1', '2', '4', '8']    # Tree depths for NoaliasBinaryTree
ALL_SESSION_LENGTHS = ['1', '10', '100', '1000']
SERVER_WARMUP = 3   # seconds
SERVER_COOLDOWN = 3 


##
# Benchmark debugging parameters.
##
#DEBUG_MESSAGE_SIZES = ['0', '10240'] # Original parameters for NoaliasMessage
DEBUG_MESSAGE_SIZES = ['1', '2']      # Tree depths for NoaliasBinaryTree
DEBUG_SESSION_LENGTHS = ['0', '2'] 


##
# Function declarations.
##
def get_parameters():
	return (ALL_MESSAGE_SIZES, ALL_SESSION_LENGTHS)


def get_debug_parameters():
	return (DEBUG_MESSAGE_SIZES, DEBUG_SESSION_LENGTHS)


def print_and_flush(msg):
	print msg
	sys.stdout.flush()


def debug_print(debug, msg):
	if debug:
		print_and_flush(msg)


def runtime_error(msg):
	print_and_flush(msg)
	sys.exit(1)


def parse_boolean(v):
	return v.upper() == 'TRUE'
	

##	
# Class declarations.
##
class CommandThread(Thread):
	def __init__(self, command):
		Thread.__init__(self)
		self.command = command

	def run(self):
		os.system(self.command)
		#common.print_and_flush('ServerThread finished.')
			
