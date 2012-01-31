import sys, socket
import os
 
if len(sys.argv) != 5:
  print "usage runClient <server> <threads> <repetitions> <output file>"
  exit
else:
  command = 'java ClientRunner ' +  sys.argv[1] + ' 2000 camelot16 ' + sys.argv[2] + ' ' + sys.argv[3] + ' > ' + sys.argv[4]

  #create an INET, STREAMing socket
  serversocket = socket.socket(
  socket.AF_INET, socket.SOCK_STREAM)
  #bind the socket to a public host, 
  # and a well-known port
  serversocket.bind((socket.gethostname(), 4321))
  #become a server socket
  serversocket.listen(5)

  #accept connections
  (clientsocket, address) = serversocket.accept()

  os.system(command)

