//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark1/a/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark1.a.Server false 8888 s 10

//$ javac -cp lib/sessionj.jar';'lib/sessionj-rt.jar';'tests/classes/ tests/classes/popl/bmarks/bmark1/a/Server.java

/**
 *
 */
package popl.bmarks.bmark1.a;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import popl.bmarks.*;

public class Server 
{
	private final noalias protocol p 
	{
		sbegin.rec X[?{Y:?(Message).!<Message>.#X, N:}]
	}	
		
	public Server(boolean debug, int port, String transports, int bufferSize) throws Exception 
	{							
		ServerThread st = new ServerThread(debug, port, transports, bufferSize);		
		
		st.start();
	}

	private class ServerThread extends Thread 
	{
		//private static final int BUFFER_SIZE = 1000;			
		
		private boolean debug;
		private int port;
		
		private String transports;
		private int bufferSize;
		
		public ServerThread(boolean debug, int port, String transports, int bufferSize)
		{
			this.debug = debug;
			this.port = port;
			this.transports = transports;
			this.bufferSize = bufferSize;
		}
		
		public void run()
		{
			final noalias SJServerSocket ss;
			
			try (ss)
			{
				boolean run = true;
				
				//ss = SJServerSocketImpl.create(p, port);
				
				//SJSessionParameters params = TransportUtils.createSJSessionParameters(this.transports, this.transports, BUFFER_SIZE);
				SJSessionParameters params = SJTransportUtils.createSJSessionParameters(this.transports, this.transports, bufferSize);
				
				ss = SJServerSocket.create(p, port, params);
				
				new KillThread(port + Kill.KILL_PORT_ADJUST, ss.getCloser()).start(); // Will be using default transports (should include SJFifoPair).
				
				Message msg;
				
				for (int counter = 0; run; counter++) 
				{						
					final noalias SJSocket ds;
					
					try (ds)
					{					
						ds = ss.accept(); // Dummy run.							
						
						ds.recursion(X)
						{
							ds.inbranch()
							{
								case Y:
								{
									msg = (Message) ds.receive();
									
									msg.inc();
									
									ds.send(msg);																							
									
									ds.recurse(X);
								}
								case N:
								{
								
								}
							}
						}
		
						final noalias SJSocket s;
					
						try (s) 
						{	
							s = ss.accept(); // Actual run.
							
							s.recursion(X)
							{
								s.inbranch()
								{
									case Y:
									{
										msg = (Message) s.receive();

										msg.inc();
										
										s.send(msg);																					
										
										s.recurse(X);
									}
									case N:
									{
									
									}
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
					
					if (debug)
					{
						System.out.println("Finished run: " + counter);
					}
					
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
	
	public static void main(String[] args) throws Exception 
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);		
		String transports = args[2];		
		int bufferSize = Integer.parseInt(args[3]);
		
		//TransportUtils.configureTransports(transports, transports);
		
		new Server(debug, port, transports, bufferSize);
	}
}
