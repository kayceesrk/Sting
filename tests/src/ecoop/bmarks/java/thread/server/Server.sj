//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.java.thread.server.Server false 8888 1 

package ecoop.bmarks.java.thread.server;

import java.io.*;
import java.net.*;
import java.util.*;

import ecoop.bmarks.*;

public class Server  
{
	public static final int REC = 1;
	public static final int QUIT = 2;

	public static int signal = MyObject.NO_SIGNAL;
	public static boolean counting = false;
	public static boolean counted = false;
	
	private static boolean debug;
		
  private int port;
  private int numClients; // NB: a TimerClient counts as two clients.
  //private boolean[] join; 
  
  //private long count = 0;  
  private int[] counts;

  public Server(boolean debug, int port, int numClients) 
  {
  	Server.debug = debug;
  	
    this.port = port;
    this.numClients = numClients;
  }

  class ServerThread extends Thread
  {
  	private int tid;
  	
  	private Socket s;
  	
  	public ServerThread(int tid, Socket s)
  	{
  		this.tid = tid;
  		this.s = s;
  	}
  	
  	public void run()
  	{
  		ObjectInputStream ois = null;
  		ObjectOutputStream oos = null;
  		
  		try
  		{
  			s.setTcpNoDelay(true);
  			
  			ois = new ObjectInputStream(s.getInputStream());
  			oos = new ObjectOutputStream(s.getOutputStream());
  			
  			for (boolean run = true; run; )
  			{  			
	  			int i = ois.readInt();
	  			
	  			if (i == REC)
	  			{
	  				ClientMessage cm = (ClientMessage) ois.readObject();
	          
	          debugPrintln("[Server] Received: " + cm);
	          
	          oos.writeObject(new MyObject(signal, cm.getSize()));
	          
	          if (counting) 
	          {
	            counts[tid]++;
	            
	            debugPrintln("[Server] Current count:" + counts[tid]);		            
	          }				
	  			}
	  			else //if (i == QUIT)
	  			{
	  				numClients--; // This is not thread safe, but we'll leave it because numClients isn't used for anything important (we're joining for termination).
	  				
	  				debugPrintln("[Server] Clients remaning: " + numClients);
	  				
	  				run = false;
	  			}
  			}
  		}
  		catch(Exception x)
  		{
  			throw new RuntimeException(x);
  		}
  		finally
  		{
  			try
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
  			catch (Exception x)
  			{
  				// Have to swallow.
  			}
  		}
  	}
  }
  
  public void run() throws Exception
  {		
  	ServerSocket ss = null;
  	
  	Socket s = null;
  	
		try 
		{
			ss = new ServerSocket(port);
			
			debugPrintln("[Server] Listening on: " + port);

			counts = new int[numClients];
			
			List threads = new LinkedList();			
			
			int nc = numClients;
			
			for (int i = 0; i < nc; i++)
			{
				ServerThread st = new ServerThread(i, ss.accept());
				
				st.start();
				
				threads.add(st);
			}
			
			for(Iterator i = threads.iterator(); i.hasNext(); )
			{
				((ServerThread) i.next()).join();				
			}
		}
		finally
		{
			if (counted)
			{
				long total = 0;
				
				for (int i = 0; i < counts.length; i++)
				{
					total += counts[i];
				}
				
				System.out.println("[Server] Total count: " + total);
			}
			
			if (ss != null)
			{
				ss.close();
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
  	int port = Integer.parseInt(args[1]);
  	int numClients = Integer.parseInt(args[2]);
  	
    new Server(debug, port, numClients).run();
  }
}
