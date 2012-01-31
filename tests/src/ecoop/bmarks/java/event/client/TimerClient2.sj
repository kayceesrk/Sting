//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.java.event.client.TimerClient2 false localhost 8888 -1 100 10 5

package ecoop.bmarks.java.event.client;

import java.io.*;
import java.net.*;

import ecoop.bmarks.*;
import ecoop.bmarks.java.event.server.Server;

// This counts as two clients (from the Server's view), due to the dummy run.
public class TimerClient2 
{
	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;
  private int sessionLength;
  private int repeats;

  public TimerClient2(boolean debug, String host, int port, int clientNum, int messageSize, int sessionLength, int repeats) 
  {
  	TimerClient2.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
    this.sessionLength = sessionLength;    
    this.repeats = repeats;
  }

  public void run() throws Exception
  {
  	try
  	{
  		for (int i = 0; i < repeats; i++)
  		{
  			debugPrintln("[TimerClient2] Run: " + (i + 1) + "/" + repeats);
  			
		  	run(false); // Dummy run for warm up.
		  	
		  	debugPrintln("[TimerClient2] Finished dummy run, now taking measurements.");
		  	
		  	run(true);
  		}
  	}
  	finally
  	{
	  	debugPrintln("Sending KILL.");
	  	
	  	new SignalClient().sendSignal(host, port, MyObject.KILL);  		
  	}
  }

  public void run(boolean time) throws Exception
  {
		Socket s = null;
		
		DataOutputStream dos = null;
		DataInputStream dis = null;
		
		try
		{
			s = new Socket(host, port);
			
			s.setTcpNoDelay(true);
			
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
	
	    long start = System.nanoTime();
	    
	    MyObject mo;
	     
	    for (int iters = 0; iters < sessionLength; iters++) 
      {
	    	dos.write(Server.intToByteArray(Server.REC));
  			dos.flush();
  			
  			byte[] bs = Server.serializeObject(new ClientMessage(clientNum, Integer.toString(iters), messageSize));
            
  			dos.write(Server.intToByteArray(bs.length));
        dos.write(bs);
        dos.flush();
  			
	      bs = new byte[4];
        
        dis.readFully(bs);
        
        bs = new byte[Server.byteArrayToInt(bs)];
        
        dis.readFully(bs);
        
        mo = (MyObject) Server.deserializeObject(bs);      
        
        debugPrintln("[TimerClient2 " + clientNum + "] Received: " + mo);
	      
	      if (debug)
	      {
	      	Thread.sleep(1000);
	      }
      }
      
      dos.writeInt(Server.QUIT);
			dos.flush();
			
      debugPrintln("[TimerClient2 " + clientNum + "] Quitting.");
	    	    
	    long finish = System.nanoTime();
	    
	    if (time)
	    {
	    	System.out.println("[TimerClient2] Session duration: " + (finish - start) + " nanos");
	    }
	  }
	  finally
	  {

	  }
	}
  
  private static final void debugPrintln(String m)
  {
  	if (debug)
  	{
  		System.out.println(m);
  	}
  }
  
  public static void main(String [] args) throws Exception
  {
  	boolean debug = Boolean.parseBoolean(args[0]);
  	String host = args[1];
    int port = Integer.parseInt(args[2]);
    int clientNum = Integer.parseInt(args[3]);
    int messageSize = Integer.parseInt(args[4]);
    int sessionLength = Integer.parseInt(args[5]);
    int repeats = Integer.parseInt(args[6]);

    new TimerClient2(debug, host, port, clientNum, messageSize, sessionLength, repeats).run();
  }
}
