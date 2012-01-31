//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.java.thread.client.TimerClient false localhost 8888 -1 10 2 BODY 1

package ecoop.bmarks2.micro.java.thread.client;

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
		
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		try
		{
			boolean debug = isDebug();
			int cid = getCid();
			int serverMessageSize = getServerMessageSize();
	    int sessionLength = getSessionLength();

			//if (timer)
			{
				startTimer();
			}
			
			s = new Socket(getHost(), getPort());
			
			s.setTcpNoDelay(true);
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
	
			//if (timer)
			{
				initialised();
			}
	    
	    ServerMessage sm;	     
	    
	    for (int iters = 0; iters < sessionLength; iters++) 
      {
  			oos.writeInt(Common.REC);
  			//oos.flush();
  			
        oos.writeObject(new ClientMessage(cid, Integer.toString(iters), serverMessageSize));
        oos.flush();
        oos.reset(); // Should reset or flush first?   
        
        sm = (ServerMessage) ois.readObject();      
            
	      debugPrintln("[TimerClient " + cid + "] Received: " + sm);
	
	      if (debug)
	      {
	      	Thread.sleep(1000);
	      }
      }     
	    
      oos.writeInt(Common.QUIT);
			oos.flush();
				
      debugPrintln("[TimerClient " + cid + "] Sent QUIT.");
			
      //if (timer)
			{
      	bodyDone();
			}
			
			Thread.sleep(50); // This will affect close timing, but perhaps needed to prevent exceptions at the Server.
	  }
	  finally
	  {
			Common.closeOutputStream(oos);
			Common.closeInputStream(ois);
			Common.closeSocket(s);
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
