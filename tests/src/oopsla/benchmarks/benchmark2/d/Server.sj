//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark2/d/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark2.d.Server 8888

/**
 * The same as benchmark1.a.Server except does not print any debug info.
 */
package benchmarks.benchmark2.d;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.*;

public class Server 
{
	private final noalias protocol p 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
	}	
	
	public Server(int port) throws Exception 
	{							
		ServerThread st = new ServerThread(port);		
		
		st.start();
	}

	private class ServerThread extends Thread 
	{
		private int port;
		
		public ServerThread(int port)
		{
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
				}
			}
			catch (Exception x)
			{
				throw new RuntimeException(x);
			}
			finally
			{
				
			}			
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		int port = Integer.parseInt(args[0]);
		
		new Server(port);
	}
}
