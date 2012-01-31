//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.java.event.client.LoadClient false localhost 8888 1234 100  

package ecoop.bmarks.java.event.client;

import java.io.*;
import java.net.*;
import java.util.*;

import ecoop.bmarks.*;
import ecoop.bmarks.java.event.server.Server;

public class LoadClient  
{
	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;

  public LoadClient(boolean debug, String host, int port, int clientNum, int messageSize) 
  {
  	LoadClient.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
  }
  
	public void run() throws Exception
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
			
			MyObject mo;
      
      boolean run = true; 
      int iters = 0;
      
      while (run) 
      {
  			dos.write(Server.intToByteArray(Server.REC));
  			dos.flush();
  			
  			byte[] bs = Server.serializeObject(new ClientMessage(clientNum, Integer.toString(iters++), messageSize));  			
  			  			
  			dos.write(Server.intToByteArray(bs.length));
        dos.write(bs);
        dos.flush();
            
        bs = new byte[4];
        
        dis.readFully(bs);
        
        bs = new byte[Server.byteArrayToInt(bs)];
        
        dis.readFully(bs);
        
        mo = (MyObject) Server.deserializeObject(bs);      
        
        run = !mo.killSignal();
            
	      debugPrintln("[LoadClient " + clientNum + "] Received: " + mo);
	
	      if (debug)
	      {
	      	Thread.sleep(1000);
	      }
      }
      
      dos.write(Server.intToByteArray(Server.QUIT));
			dos.flush();
			
      debugPrintln("[LoadClient " + clientNum + "] Quitting.");
		}
		catch(Exception x)
		{
			throw new RuntimeException(x);
		}
		finally
		{
			if (dos != null)
			{
				dos.flush();
				dos.close();
			}
			
			if (dis != null)
			{
				dis.close();
			}
			
			if (s != null)
			{
				s.close();
			}
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

    new LoadClient(debug, host, port, clientNum, messageSize).run();
  }
}
