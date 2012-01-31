//$ bin/sjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/b/Server.sj -d tests/classes/
//$ bin/sj -cp tests/classes/ pldi.benchmarks.benchmark1.b.Server false 8888

/**
 *
 */
package pldi.benchmarks.benchmark1.b;

import java.io.*;
import java.net.*;

import sj.runtime.*;
import sj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

public class Server 
{
	private final protocol p 
	{
		begin.?[?(BinaryTree).!<BinaryTree>]*
	}	
	
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
			SJServerSocket ss = null;
			
			try 
			{
				boolean run = true;
				
				ss = SJFServerSocket.create(p, port);
				
				//new KillThread(port + 1, ss).start();
				new KillThread(port + 1, ss.getCloser()).start();
				
				BinaryTree bt;
				
				for (int counter = 0; run; counter++) 
				{						
					SJSocket ds = null;
					
					try (ds)
					{					
						ds = ss.accept(); // Dummy run.							
						
						ds.inwhile()
						{
							bt = (BinaryTree) ds.receive();
							 
							bt.inc();
							 
							ds.send(bt);
						}			
		
						SJSocket s = null;
					
						try (s) 
						{	
							s = ss.accept(); // Actual run.
							
							s.inwhile()
							{
							 	bt = (BinaryTree) s.receive();				
							 	
							 	bt.inc();
							 	
							 	s.send(bt);
							 	
							 	if (debug)
							 	{
							 		bt.println();
							 	}
							}
						}
						finally 
						{ 
		
						}					
					}			
					catch (Exception x)
					{
						run = false;
					}
					finally
					{
						
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
	
	private class KillThread extends Thread
	{				
		private int port;
		private SJServerSocketCloser ssc;
		//private SJServerSocket server = null;
		
		public KillThread(int port, SJServerSocketCloser ssc)
		//public KillThread(int port, SJServerSocket server)
		{
			this.port = port;
			this.ssc = ssc;
			//this.server = server;
		}
		
		public void run()
		{
			protocol q { begin }
			
			SJServerSocket ss = null;
			
			try 
			{
				ss = SJFServerSocket.create(q, port);
				
				SJSocket s = null;
				
				try (s)
				{
					s = ss.accept();					
				}
				finally
				{
					
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
					ssc.close();
					//server.close();
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
