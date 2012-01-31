#!/usr/bin/env python
import sys, socket
import os

if len(sys.argv) != 5:
  print "usage runClient <server> <threads> <clients> <output file>"
  exit
else:
  command = 'java ServerRunner ' +  sys.argv[1] + ' 2000 ' +sys.argv[2] + ' ' + sys.argv[3] + ' > ' + sys.argv[4] + ' &'

  os.system(command)


  #create an INET, STREAMing socket
  s1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s3 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s4 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s5 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s6 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s7 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s8 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s9 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s10 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s11 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s12 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s13 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s14 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s15 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

  #now connect to the web server on port 80 
  # - the normal http port
  s1.connect(("camelot01", 4321))
  s2.connect(("camelot02", 4321))
  s3.connect(("camelot03", 4321))
  s4.connect(("camelot04", 4321))
  s5.connect(("camelot05", 4321))
  s6.connect(("camelot06", 4321))
  s7.connect(("camelot07", 4321))
  s8.connect(("camelot08", 4321))
  s9.connect(("camelot09", 4321))
  s10.connect(("camelot10", 4321))
  s11.connect(("camelot11", 4321))
  s12.connect(("camelot12", 4321))
  s13.connect(("camelot13", 4321))
  s14.connect(("camelot14", 4321))
  s15.connect(("camelot15", 4321))

