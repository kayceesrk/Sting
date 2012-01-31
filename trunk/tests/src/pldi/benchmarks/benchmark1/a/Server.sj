//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/a/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.a.Server false 8888

/**
 *
 */
package pldi.benchmarks.benchmark1.a;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;

import pldi.benchmarks.BinaryTree;

public class Server 
{
	private final noalias protocol p 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
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
			final noalias SJServerSocket ss;
			
			try (ss)
			{
				boolean run = true;
				
				ss = SJServerSocketImpl.create(p, port);
				
				new KillThread(port + Kill.KILL_PORT_ADJUST, ss.getCloser()).start();
				
				BinaryTree bt;
				
				for (int counter = 0; run; counter++) 
				{						
					final noalias SJSocket ds;
					
					try (ds)
					{					
						ds = ss.accept(); // Dummy run.							
						
						ds.inwhile()
						{
							bt = (BinaryTree) ds.receive();
							 
							bt.inc();
							 
							ds.send(bt);
						}			
		
						final noalias SJSocket s;
					
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
						//x.printStackTrace();
						
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
				// If main server thread fails, need to terminate the kill thread to close the process.
			}			
		}
	}
	
	private class KillThread extends Thread
	{				
		private int port;
		private SJServerSocketCloser ssc;
		
		public KillThread(int port, SJServerSocketCloser ssc)
		{
			this.port = port;
			this.ssc = ssc;
		}
		
		public void run()
		{
			final noalias protocol q { sbegin }
			
			final noalias SJServerSocket ss;
			
			try (ss)
			{
				ss = SJServerSocketImpl.create(q, port);
				
				final noalias SJSocket s;
				
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
				throw new RuntimeException(x);
			}
			finally
			{
				ssc.close();
			}
		}
	}

	private static void configureTransports()
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		ss.add(new SJStreamTCP());
		ss.add(new SJManualTCP());
		
		ts.add(new SJStreamTCP());
		ts.add(new SJManualTCP());
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}
	
	public static void main(String[] args) throws Exception 
	{
		configureTransports();
		
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		
		new Server(debug, port);
	}
}
