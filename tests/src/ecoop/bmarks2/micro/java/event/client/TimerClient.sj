//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.java.event.client.TimerClient false localhost 8888 -1 10 2 BODY 1

package ecoop.bmarks2.micro.java.event.client;

import java.io.*;
import java.net.*;

import ecoop.bmarks2.micro.*;

public class TimerClient extends ecoop.bmarks2.micro.TimerClient 
{
  public TimerClient(boolean debug, String host, int port, int cid, int serverMessageSize, int sessionLength, String flag, int repeats) 
  {
  	super(debug, host, port, cid, serverMessageSize, sessionLength, flag, repeats);
  }

  public void run(boolean timer) throws Exception
  {
		Socket s = null;
		
		DataOutputStream dos = null;
		DataInputStream dis = null;
		
		try
		{
	  	boolean debug = isDebug();
			int cid = getCid();
			int serverMessageSize = getServerMessageSize();
	    int sessionLength = getSessionLength();						
			
    	startTimer();
			
			s = new Socket(getHost(), getPort());
			
			s.setTcpNoDelay(true);
			
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
	
			initialised();
	  	
	    ServerMessage sm;
	     
	    for (int iters = 0; iters < sessionLength; iters++) 
      {
	    	dos.write(Common.serializeInt(Common.REC));
  			dos.flush();
  			
  			byte[] bs = Common.serializeObject(new ClientMessage(cid, Integer.toString(iters), serverMessageSize));
            
  			dos.write(Common.serializeInt(bs.length));
        dos.write(bs);
        dos.flush();
  			
	      bs = new byte[4];
        
        dis.readFully(bs);
        
        bs = new byte[Common.deserializeInt(bs)];
        
        dis.readFully(bs);
        
        sm = (ServerMessage) Common.deserializeObject(bs);      
        
        debugPrintln("[TimerClient " + cid + "] Received: " + sm);
	      
	      if (debug)
	      {
	      	Thread.sleep(1000);
	      }
      }
      
      dos.write(Common.serializeInt(Common.QUIT));
			dos.flush();
			
			debugPrintln("[TimerClient " + cid + "] Sent QUIT.");
	    	    
			bodyDone();
			
			Thread.sleep(50);
	  }
	  finally
	  {

	  }

	  stopTimer();
	  
	  if (timer)
  	{
  		printTimer();
  	}
	  
	  resetTimer();
	}
  
  public static void main(String [] args) throws Exception
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	String host = args[1];
    int port = Integer.parseInt(args[2]);
    int cid = Integer.parseInt(args[3]);
    int serverMessageSize = Integer.parseInt(args[4]);
    int sessionLength = Integer.parseInt(args[5]);
    String flag = args[6];
    int repeats = Integer.parseInt(args[7]);

    new TimerClient(debug, host, port, cid, serverMessageSize, sessionLength, flag, repeats).run();
  }
}
