//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.SignalClient localhost 8888 KILL

package ecoop.bmarks;

import java.io.*;
import java.net.*;

import ecoop.bmarks.*;

public class SignalClient 
{
	// Not really a good place for these constants. Should make a separate constants class. Could also put e.g. common close routines in there.
	public static final int SIGNAL_SERVER_PORT_OFFSET = 1000;	
	
	public static final String JAVA_THREAD = "JT"; 
	public static final String JAVA_EVENT = "JE";
	public static final String SJ_THREAD = "ST";
	public static final String SJ_EVENT = "SE";	
	
	public static final String SMTP_THREAD = "SMTPT";
	public static final String SMTP_EVENT = "SMTPE"; 
	
	public static final String KILL = "KILL";
	public static final String TIME = "TIME";
	public static final String COUNT = "COUNT";	
	public static final String STOP = "STOP";	
	
  public static void sendSignal(String host, int port, int signal) throws Exception
  {
  	Socket s = null; 
  	
  	ObjectOutputStream os = null;
  	
    try 
    {
      //System.out.println("[SignalClient] Connecting to: " + host + ":" + (port + SignalServer.SIGNAL_SERVER_PORT_OFFSET)); 
    	
    	s = new Socket(host, port + SIGNAL_SERVER_PORT_OFFSET);
      
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
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    
    int signal = 0;
    
    for (int i = 2; i < args.length; i++) 
    {
    	String command = args[i].toUpperCase();
    	
      if (command.equals(KILL))
      {
        signal |= MyObject.KILL;
      }
      else if (command.equals(TIME))
      {
        signal |= MyObject.BEGIN_TIMING;
      }
      else if (command.equals(COUNT))
      {
        signal |= MyObject.BEGIN_COUNTING;
      }
      else if (command.equals(STOP))
      {
        signal |= MyObject.STOP_COUNTING;
      }
      else
      {
      	System.out.println("[SignalClient] Unrecognised command: " + args[i]);
      }
    }

    sendSignal(host, port, signal);
  }
}
