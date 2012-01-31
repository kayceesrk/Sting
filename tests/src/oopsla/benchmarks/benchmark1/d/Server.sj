//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark1/d/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark1.d.Server false 8888

/**
 *
 */
package benchmarks.benchmark1.d;

import java.io.*;
import java.net.*;

import benchmarks.BinaryTree;

public class Server 
{
	public Server(boolean debug, int port) throws Exception 
	{							
		ServerThread st = new ServerThread(debug, port);		
		
		st.start();
	}

	private class ServerThread extends Thread 
	{
		private boolean debug;
		private int port;
		
		public ServerThread(boolean debug, int port)
		{
			this.debug = debug;
			this.port = port;
		}
		
		public void run()
		{
			ServerSocket ss = null;
			
			try 
			{
				boolean run = true;
				
				ss = new ServerSocket(port);
				
				new KillThread(port + Kill.KILL_PORT_ADJUST, ss).start();
				
				BinaryTree bt;
				
				for (int counter = 0; run; counter++) 
				{						
					Socket ds = null;										
					
					ObjectInputStream dois = null;
					ObjectOutputStream doos = null;
					
					try 
					{					
						ds = ss.accept(); // Dummy run.							
						
						ds.setTcpNoDelay(true);
						
						dois = new ObjectInputStream(ds.getInputStream());
						doos = new ObjectOutputStream(ds.getOutputStream());
						
						for (boolean b = dois.readBoolean(); b; b = dois.readBoolean())
						{
							bt = (BinaryTree) dois.readObject();
							 
							bt.inc();
							 
							doos.writeObject(bt);
						}			
		
						Socket s = null;											
						
						ObjectInputStream ois = null;
						ObjectOutputStream oos = null;						
						
						try 
						{	
							s = ss.accept(); // Actual run.
							
							s.setTcpNoDelay(true);
							
							ois = new ObjectInputStream(s.getInputStream());
							oos = new ObjectOutputStream(s.getOutputStream());							
							
							for (boolean b = ois.readBoolean(); b; b = ois.readBoolean())
							{
							 	bt = (BinaryTree) ois.readObject();				
							 	
							 	bt.inc();
							 	
							 	oos.writeObject(bt);
							 	
							 	if (debug)
							 	{
							 		bt.println();
							 	}
							}
						}
						finally 
						{ 
							closeUp(s, ois, oos);
						}					
					}			
					catch (Exception x)
					{
						run = false;
					}
					finally
					{
						closeUp(ds, dois, doos);
					}
					
					System.out.println("Finished run: " + counter);
					
					if (debug)
					{
						System.out.println();
					}
				}
			}
			catch (Exception x)
			{
				throw new RuntimeException(x);
			}
			finally
			{
				if (ss != null)
				{
					try
					{
						ss.close();
					}
					catch (IOException x)
					{
						
					}					
				}
			}			
		}
	}
	
	private void closeUp(Socket s, ObjectInputStream ois, ObjectOutputStream oos) throws IOException
	{
		if (ois != null) 
		{
			ois.close();							
		}
		
		if (oos != null) 
		{ 
			oos.flush(); 
			oos.close(); 
		}
		
		if (s != null) 
		{
			s.close();		
		}
	}
	
	private class KillThread extends Thread
	{				
		private int port;
		private ServerSocket server;
		
		public KillThread(int port, ServerSocket server)
		{
			this.port = port;
			this.server = server;
		}
		
		public void run()
		{
			ServerSocket ss = null;
			
			try 
			{
				ss = new ServerSocket(port);
				
				Socket s = null;
				
				try 
				{
					s = ss.accept();					
				}
				finally
				{
					if (s != null)
					{
						s.close();						
					}
				}
			}
			catch (Exception x)
			{
				x.printStackTrace();
			}
			finally
			{
				try
				{
					server.close();
				}
				catch (IOException x)
				{

				}	
			
				if (ss != null)
				{
					try
					{
						ss.close();
					}
					catch (IOException x)
					{
						
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		
		new Server(debug, port);
	}
}
