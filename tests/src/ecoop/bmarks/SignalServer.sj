package ecoop.bmarks;

import java.io.*;
import java.net.*;

import ecoop.bmarks.*;
import ecoop.bmarks.java.thread.server.*;
import ecoop.bmarks.java.event.server.*;
import ecoop.bmarks.sj.server.thread.*;
import ecoop.bmarks.sj.server.event.*;

public class SignalServer 
{
  private int port;
  private String server;

  private long start;
  private long finish;
  
  public SignalServer(int port, String server) 
  {
    this.port = port;
    this.server = server;
  }

  public void run() throws Exception
  {
  	ServerSocket ss = null;
  	
  	Socket s  = null; 
  	
    ObjectInputStream is = null;
    
    try 
    {
      ss = new ServerSocket(port + SignalClient.SIGNAL_SERVER_PORT_OFFSET);
      
      //System.out.println("[SignalServer] Listening on: " + (port + SIGNAL_SERVER_PORT_OFFSET));
      
      for (boolean run = true; run; ) 
      {
        s = ss.accept();

        is = new ObjectInputStream(s.getInputStream());
        
        int x = is.readInt();

        //System.out.println("[SignalServer] Read: " + x);
        
        // Not currently used.
        if ((x & MyObject.BEGIN_TIMING) != 0) // Not currently used.
        {
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server.signal |= MyObject.BEGIN_TIMING;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server.signal |= MyObject.BEGIN_TIMING;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server.signal |= MyObject.BEGIN_TIMING;
        	}        	
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server.signal |= MyObject.BEGIN_TIMING;
        	}
        }
        
        if ((x & MyObject.BEGIN_COUNTING) != 0)
        {
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server.counting = true;
        		ecoop.bmarks.java.thread.server.Server.counted = true;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server.counting = true;
        		ecoop.bmarks.java.event.server.Server.counted = true;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server.counting = true;
        		ecoop.bmarks.sj.server.thread.Server.counted = true;
        	}
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server.counting = true;
        		ecoop.bmarks.sj.server.event.Server.counted = true;
        	}
        	
        	start = System.nanoTime();
        }
        
        if ((x & MyObject.STOP_COUNTING) != 0)
        {        	
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server.counting = false;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server.counting = false;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server.counting = false;
        	}
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server.counting = false;
        	}
        	
        	finish = System.nanoTime();
        	
        	System.out.println("[SignalServer] Counting time: " + (finish - start) + " nanos");        	
        }
        
        if ((x & MyObject.KILL) != 0) 
        {
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server.signal |= MyObject.KILL;
        	}
        	
          run = false;
        }        
      }
    }
    finally
    {
     	if (is != null)
    	{
    		is.close();
    	}  
     	
    	if (s != null)
    	{
    		s.close();
    	}
    	
    	if (ss != null)
    	{
    		ss.close();
    	}
    }
    
    //System.out.println("[SignalServer] Finished.");
  }

  public static void main(String args[]) throws Exception
  {
  	int port = Integer.parseInt(args[0]);
  	String server = args[1];
  	
  	if (!(server.equals(SignalClient.JAVA_THREAD) || server.equals(SignalClient.JAVA_EVENT) || server.equals(SignalClient.SJ_THREAD) || server.equals(SignalClient.SJ_EVENT)))
		{
  		System.out.println("[SignalServer] Bad server flag: " + server);
  		
  		return;
		}
  	
    new SignalServer(port, server).run();
  }
}
