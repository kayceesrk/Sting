//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.java.thread.client.LoadClient false localhost 8888 1234 100  

package ecoop.bmarks.java.thread.client;

import java.io.*;
import java.net.*;

import ecoop.bmarks.*;
import ecoop.bmarks.java.thread.server.Server;

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
		
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		try
		{
			s = new Socket(host, port);
			
			s.setTcpNoDelay(true);
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			MyObject mo;
      
      boolean run = true; 
      int iters = 0;
      
      while (run) 
      {
  			oos.writeInt(Server.REC);
  			oos.flush();
  			
        oos.writeObject(new ClientMessage(clientNum, Integer.toString(iters++), messageSize));
            
        mo = (MyObject) ois.readObject();      
        
        run = !mo.killSignal();
            
	      debugPrintln("[LoadClient " + clientNum + "] Received: " + mo);
	
	      if (debug)
	      {
	      	Thread.sleep(1000);
	      }
      }
      
      oos.writeInt(Server.QUIT);
			oos.flush();
			
      debugPrintln("[LoadClient " + clientNum + "] Quitting.");
		}
		catch(Exception x)
		{
			throw new RuntimeException(x);
		}
		finally
		{
			if (oos != null)
			{
				oos.flush();
				oos.close();
			}
			
			if (ois != null)
			{
				ois.close();
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
