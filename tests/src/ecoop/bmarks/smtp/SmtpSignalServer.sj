package ecoop.bmarks.smtp;

import java.io.*;
import java.net.*;

import ecoop.bmarks.*;
import ecoop.bmarks.java.thread.server.*;
import ecoop.bmarks.java.event.server.*;
import ecoop.bmarks.sj.server.thread.*;
import ecoop.bmarks.sj.server.event.*;

public class SmtpSignalServer 
{
  private int port;
  private String server;

  private long start;
  private long finish;
  
  public SmtpSignalServer(int port, String server) 
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
      
      //System.out.println("[SmtpSignalServer] Listening on: " + (port + SIGNAL_SERVER_PORT_OFFSET));
      
      for (boolean run = true; run; ) 
      {
        s = ss.accept();

        is = new ObjectInputStream(s.getInputStream());
        
        int x = is.readInt();

        //System.out.println("[SmtpSignalServer] Read: " + x);
                
        if ((x & MyObject.BEGIN_COUNTING) != 0)
        {
        	if (server.equals(SignalClient.SMTP_THREAD))
        	{
        		ecoop.bmarks.smtp.thread.Server.counting = true;
        		//ecoop.bmarks.java.thread.server.Server2.counted = true;
        	}
        	else if (server.equals(SignalClient.SMTP_EVENT))
        	{
        		ecoop.bmarks.smtp.event.Server.counting = true;
        		//ecoop.bmarks.java.event.server.Server2.counted = true;
        	}        	
        	
        	start = System.nanoTime();
        }
        
        if ((x & MyObject.STOP_COUNTING) != 0)
        {               	
        	finish = System.nanoTime();
        	
        	System.out.println("[SmtpSignalServer] Counting time: " + (finish - start) + " nanos");        	       	
        	
        	if (server.equals(SignalClient.SMTP_THREAD))
        	{
        		ecoop.bmarks.smtp.thread.Server.counting = false;
        		    			
        		int total = 0;
        		
        		for (int i = 0; i < ecoop.bmarks.smtp.thread.Server.counts.length; i++)
        		{
        			total += ecoop.bmarks.smtp.thread.Server.counts[i];
        		}
        		
        		System.out.println("[SmtpSignalServer] Total count: " + total);
    				
    				ecoop.bmarks.smtp.thread.Server.counts = new int[ecoop.bmarks.smtp.thread.Server.counts.length];
        	}
        	else if (server.equals(SignalClient.SMTP_EVENT))
        	{
        		ecoop.bmarks.smtp.event.Server.counting = false;
        		
        		System.out.println("[SmtpSignalServer] Total count: " + ecoop.bmarks.smtp.event.Server.count);
    				
        		ecoop.bmarks.smtp.event.Server.count = 0;        		
        	}
        }
        
        if ((x & MyObject.KILL) != 0) 
        {
        	if (server.equals(SignalClient.SMTP_THREAD))
        	{
        		ecoop.bmarks.smtp.thread.Server.signal |= MyObject.KILL;
        	}
        	else if (server.equals(SignalClient.SMTP_EVENT))
        	{
        		ecoop.bmarks.smtp.event.Server.signal |= MyObject.KILL;
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
    
    //System.out.println("[SmtpSignalServer] Finished.");
  }

  private static final void totalCount(int[] is)
  {
  	long total = 0;
  	
  	for (int i = 0; i < is.length; i++)
		{
			total += is[i];
		}
		
		System.out.println("[SmtpSignalServer] Total count: " + total);  	
  }
  
  public static void main(String args[]) throws Exception
  {
  	int port = Integer.parseInt(args[0]);
  	String server = args[1];
  	
  	if (!(server.equals(SignalClient.SMTP_THREAD) || server.equals(SignalClient.SMTP_EVENT)))
		{
  		System.out.println("[SmtpSignalServer] Bad server flag: " + server);
  		
  		return;
		}
  	
    new SmtpSignalServer(port, server).run();
  }
}
