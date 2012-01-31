#!/usr/bin/python
import sys, socket
import os
import time


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

#now connect to the web server
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

#clients = ['20', '7500', '1500', '150', '200', '250', '350']
clients = ['3000', '7500', '15000', '22500', '30000', '37500', '52500']
sessionLength = ['10', '20', '30']
msgSize = ['1024', '2048', '4096']

for i in clients:
  for k in sessionLength:
    for j in msgSize:
      command = 'java ServerRunner 2000 ' + i + ' ' + j + ' > camelot16.' + i + '.' + k + '.' + j + ' &'
      #print command + '\n'
      os.system(command)

      s1.send('1');
      s2.send('1');
      s3.send('1');
      s4.send('1');
      s5.send('1');
      s6.send('1');
      s7.send('1');
      s8.send('1');
      s9.send('1');
      s10.send('1');
      s11.send('1');
      s12.send('1');
      s13.send('1');
      s14.send('1');
      s15.send('1');
      
      time.sleep(5);
      os.system('java Signal localhost 2001 Count');

      time.sleep(1);
      os.system('java Signal localhost 2001 Time');
  
      time.sleep(7);
      os.system('java Signal localhost 2001 Kill');

      time.sleep(5);

#s1.send(0);
#s2.send(0);
#s3.send(0);
#s4.send(0);
#s5.send(0);
#s6.send(0);
#s7.send(0);
#s8.send(0);
#s9.send(0);
#s10.send(0);
#s11.send(0);
#s12.send(0);
#s13.send(0);
#s14.send(0);
#s15.send(0);
