//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark1/e/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark1.e.Server false 8888

/**
 *
 */
package benchmarks.benchmark1.e;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;

import benchmarks.*;

public class Server 
{
	private final noalias protocol p 
	{
		//sbegin.?[!<BigObject>]*
		sbegin.?[?(BigObject).!<BigObject>]*
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
				
				BigObject bo;
				
				for (int counter = 0; run; counter++) 
				{						
					final noalias SJSocket ds;
					
					try (ds)
					{					
						ds = ss.accept(); // Dummy run.							
						
						ds.inwhile()
						{
							//BigObject bo = new BigObject(-1, msgSizes[i] - 86);
						
							bo = (BigObject) ds.receive();						
							
							bo.inc();
							
							ds.send(bo);
						}			
		
						final noalias SJSocket s;
					
						try (s) 
						{	
							s = ss.accept(); // Actual run.
							
							//int id = 0;
							
							s.inwhile()
							{
							 	//BigObject bo = new BigObject(-1, msgSizes[i] - 86);
							 	
							 	bo = (BigObject) s.receive();											 	
							 	
							 	bo.inc();
							 	
							 	s.send(bo);
							 	
							 	if (debug)
							 	{
							 		bo.println();
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
				// FIXME: If main server thread fails, need to terminate the kill thread to close the process. Could try and get a closer for the Kill server socket. Or maybe just do a System.exit.
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
