//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.java.event.client.LoadClient false localhost 8888 -2 100

package ecoop.bmarks2.micro.java.event.client;

import java.io.*;
import java.net.*;
import java.util.*;

import ecoop.bmarks2.micro.*;

public class LoadClient extends ecoop.bmarks2.micro.LoadClient
{
  public LoadClient(boolean debug, String host, int port, int cid, int serverMessageSize, boolean[] ack, boolean[] spin) 
  {
  	super(debug, host, port, cid, serverMessageSize, ack, spin);
  }
  
	public void run() throws Exception
	{
		Socket s = null;
		
		DataOutputStream dos = null;
		DataInputStream dis = null;
		
		try
		{
			s = new Socket(getHost(), getPort());
			
			s.setTcpNoDelay(true);
			
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			
			sendAck();
			
			boolean debug = isDebug();
			int cid = getCid();
			int serverMessageSize = getServerMessageSize();
			
			ServerMessage sm;
      
      boolean run = true; 
      int iters = 0;
      
      while (run) 
      {
  			dos.write(Common.serializeInt(Common.REC));
  			dos.flush();
  			
  			byte[] bs = Common.serializeObject(new ClientMessage(cid, Integer.toString(iters++), serverMessageSize));  			
  			  			
  			dos.write(Common.serializeInt(bs.length));
        dos.write(bs);
        dos.flush();
            
        bs = new byte[4];
        
        dis.readFully(bs);
        
        bs = new byte[Common.deserializeInt(bs)];
        
        dis.readFully(bs);
        
        sm = (ServerMessage) Common.deserializeObject(bs);      
        
        run = !sm.isKill();
            
	      debugPrintln("[LoadClient " + cid + "] Received: " + sm);
	
	      if (debug)
	      {
	      	Thread.sleep(1000);
	      }
        waitSpin();
      }
      
      dos.write(Common.serializeInt(Common.QUIT));
			dos.flush();
			
      debugPrintln("[LoadClient " + cid + "] Quitting.");
      
      Thread.sleep(50);
		}
		catch(Exception x)
		{
			throw new RuntimeException(x);
		}
		finally
		{
			Common.closeOutputStream(dos);
			Common.closeInputStream(dis);
			Common.closeSocket(s);
		}
	}

  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	String host = args[1];
    int port = Integer.parseInt(args[2]);
    int cid = Integer.parseInt(args[3]);
    int serverMessageSize = Integer.parseInt(args[4]);

    new LoadClient(debug, host, port, cid, serverMessageSize, new boolean[1], new boolean[1]).run();  
  }
}
