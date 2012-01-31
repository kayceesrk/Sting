#!/usr/bin/env python

import os
import sys
from threading import Thread


##
# "Environment"-dependent execution parameters
##
JAVA = 'java'
CAMELOT_JAVA = '~/bin/jdk1.6.0_21-64/bin/java'
RMI_CODEBASE = '-Djava.rmi.server.codebase=file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-hg/tests/classes/'
RMI_SECURITY_POLICY = '-Djava.security.policy=C:/cygwin/home/Raymond/code/java/eclipse/sessionj-hg/tests/src/thesis/benchmark/bmark1/rmi/security-localhost.policy'
DOC_RMI_CODEBASE = '-Djava.rmi.server.codebase=file:///homes/rhu/code/java/eclipse/sessionj-hg/tests/classes/'
DOC_RMI_SECURITY_POLICY = '-Djava.security.policy=/homes/rhu/code/java/eclipse/sessionj-hg/tests/src/thesis/benchmark/bmark1/rmi/security.policy'


##
# Benchmark configuration parameters.
##
ALL_VERSIONS = ['RMI', 'SJm', 'SJs', 'SOCKm', 'SOCKs']
ALL_MESSAGE_SIZES = ['0', '1024']
ALL_SESSION_LENGTHS = ['1', '10', '100', '1000']


##
# Benchmark debugging parameters.
##
DEBUG_MESSAGE_SIZES = ['0', '10240']
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
			
