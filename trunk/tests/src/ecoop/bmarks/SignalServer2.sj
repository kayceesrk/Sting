package ecoop.bmarks;

import java.io.*;
import java.net.*;

import ecoop.bmarks.*;
import ecoop.bmarks.java.thread.server.*;
import ecoop.bmarks.java.event.server.*;
import ecoop.bmarks.sj.server.thread.*;
import ecoop.bmarks.sj.server.event.*;

public class SignalServer2 
{
  private int port;
  private String server;

  private long start;
  private long finish;
  
  public SignalServer2(int port, String server) 
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
      
      //System.out.println("[SignalServer2] Listening on: " + (port + SIGNAL_SERVER_PORT_OFFSET));
      
      for (boolean run = true; run; ) 
      {
        s = ss.accept();

        is = new ObjectInputStream(s.getInputStream());
        
        int x = is.readInt();

        //System.out.println("[SignalServer2] Read: " + x);
        
        // Not currently used.
        if ((x & MyObject.BEGIN_TIMING) != 0) // Not currently used.
        {
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server2.signal |= MyObject.BEGIN_TIMING;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server2.signal |= MyObject.BEGIN_TIMING;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server2.signal |= MyObject.BEGIN_TIMING;
        	}        	
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server2.signal |= MyObject.BEGIN_TIMING;
        	}
        }
        
        if ((x & MyObject.BEGIN_COUNTING) != 0)
        {
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server2.counting = true;
        		ecoop.bmarks.java.thread.server.Server2.counted = true;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server2.counting = true;
        		ecoop.bmarks.java.event.server.Server2.counted = true;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server2.counting = true;
        		ecoop.bmarks.sj.server.thread.Server2.counted = true;
        	}
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server2.counting = true;
        		ecoop.bmarks.sj.server.event.Server2.counted = true;
        	}
        	
        	start = System.nanoTime();
        }
        
        if ((x & MyObject.STOP_COUNTING) != 0)
        {               	
        	finish = System.nanoTime();
        	
        	System.out.println("[SignalServer2] Counting time: " + (finish - start) + " nanos");        	       	
        	
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server2.counting = false;
        		    			
    				totalCount(ecoop.bmarks.java.thread.server.Server2.counts);
    				
    				ecoop.bmarks.java.thread.server.Server2.counts = new int[ecoop.bmarks.java.thread.server.Server2.counts.length];
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server2.counting = false;
        		
        		System.out.println("[SignalServer2] Total count: " + ecoop.bmarks.java.event.server.Server2.count);
    				
        		ecoop.bmarks.java.event.server.Server2.count = 0;        		
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server2.counting = false;
        		
    				totalCount(ecoop.bmarks.sj.server.thread.Server2.counts);
    				
    				ecoop.bmarks.sj.server.thread.Server2.counts = new int[ecoop.bmarks.sj.server.thread.Server2.counts.length];        		
        	}
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server2.counting = false;
        		
        		System.out.println("[SignalServer2] Total count: " + ecoop.bmarks.sj.server.event.Server2.count);
    				
        		ecoop.bmarks.sj.server.event.Server2.count = 0;        		
        	}
        }
        
        if ((x & MyObject.KILL) != 0) 
        {
        	if (server.equals(SignalClient.JAVA_THREAD))
        	{
        		ecoop.bmarks.java.thread.server.Server2.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.JAVA_EVENT))
        	{
        		ecoop.bmarks.java.event.server.Server2.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.SJ_THREAD))
        	{
        		ecoop.bmarks.sj.server.thread.Server2.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.SJ_EVENT))
        	{
        		ecoop.bmarks.sj.server.event.Server2.signal |= MyObject.KILL;
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
    
    //System.out.println("[SignalServer2] Finished.");
  }

  private static final void totalCount(int[] is)
  {
  	long total = 0;
  	
  	for (int i = 0; i < is.length; i++)
		{
			total += is[i];
		}
		
		System.out.println("[SignalServer2] Total count: " + total);  	
  }
  
  public static void main(String args[]) throws Exception
  {
  	int port = Integer.parseInt(args[0]);
  	String server = args[1];
  	
  	if (!(server.equals(SignalClient.JAVA_THREAD) || server.equals(SignalClient.JAVA_EVENT) || server.equals(SignalClient.SJ_THREAD) || server.equals(SignalClient.SJ_EVENT)))
		{
  		System.out.println("[SignalServer2] Bad server flag: " + server);
  		
  		return;
		}
  	
    new SignalServer2(port, server).run();
  }
}
