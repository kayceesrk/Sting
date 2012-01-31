//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.SignalClient localhost 8888 KILL

package ecoop.bmarks2.micro;

import java.io.*;
import java.net.*;

import ecoop.bmarks2.micro.*;

// Also used by macro benchmarks.
public class SignalClient 
{	
	public static final String KILL = "KILL";
	public static final String COUNT = "COUNT";
	public static final String STOP = "STOP";
	
  public static void sendSignal(String host, int port, int signal) throws Exception
  {
  	Socket s = null; 
  	
  	ObjectOutputStream os = null;
  	
    try 
    {
      //System.out.println("[SignalClient] Connecting to: " + host + ":" + (port + SignalServer.SIGNAL_SERVER_PORT_OFFSET)); 
    	
    	s = new Socket(host, port + SignalServer.SIGNAL_SERVER_PORT_OFFSET);
      
      os = new ObjectOutputStream(s.getOutputStream());
      
      os.writeInt(signal);
      os.flush();
    }
    finally
    {
     	Common.closeOutputStream(os); 
     	Common.closeSocket(s);
    }    
  }

  public static void main(String args[]) throws Exception
  {
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    
    String command = args[2].toUpperCase();
    
    int signal;
    
    if (command.equals(KILL))
    {
      signal = SignalServer.KILL;
    }
    else if (command.equals(COUNT))
    {
      signal = SignalServer.START_COUNTING;
    }
    else if (command.equals(STOP))
    {
      signal = SignalServer.STOP_COUNTING;
    }
    else
    {
    	throw new RuntimeException("[SignalClient] Unrecognised command: " + args[2]);
    }

    sendSignal(host, port, signal);
  }
}
