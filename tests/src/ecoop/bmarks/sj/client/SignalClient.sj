//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.SignalClient localhost 8888 KILL

package ecoop.bmarks.sj.client;

import java.io.*;
import java.net.*;

import ecoop.bmarks.sj.common.*;
import ecoop.bmarks.sj.server.SignalServer;

//@deprecated
public class SignalClient 
{
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
     	if (os != null)
    	{
    		os.flush();
    		os.close();
    	}   
     	
     	if (s != null)
    	{
    		s.close();
    	}
    }    
  }

  public static void main(String args[]) throws Exception
  {
    if (args.length < 3) 
    {
      System.out.println("Usage: java SignalClient <host> <port> <Command1> [Command2] [Command3]");
    }

    String host = args[0];
    int port = Integer.parseInt(args[1]);
    
    int signal = 0;
    
    for (int i = 2; i < args.length; i++) 
    {
    	String command = args[i].toUpperCase();
    	
      if (command.equals("KILL"))
      {
        signal |= MyObject.KILL;
      }
      else if (command.equals("TIME"))
      {
        signal |= MyObject.BEGIN_TIMING;
      }
      else if (command.equals("COUNT"))
      {
        signal |= MyObject.BEGIN_COUNTING;
      }
      else
      {
      	System.out.println("[SignalClient] Unrecognised command: " + args[i]);
      }
    }

    sendSignal(host, port, signal);
  }
}
