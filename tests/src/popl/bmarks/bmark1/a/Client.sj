//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark1/a/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark1.a.Client false localhost 8888 s 1024 10 7 1
//$ tests/src/popl/bmarks/bmark1/a/client.sh localhost 8888 d 0 

//$ javac -cp lib/sessionj.jar';'lib/sessionj-rt.jar';'tests/classes/ tests/classes/popl/bmarks/bmark1/a/Client.java

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

public class Client 
{	
	private final noalias protocol p 
	{
		cbegin.rec X[!{Y:!<Message>.?(Message).#X, N:}]
	}	
	
	//private static final int BUFFER_SIZE = 1000;			
	
	public void run(boolean debug, String server, int port, String transports, int size, int len, int bufferSize, int num) throws Exception // public because used by benchmark2.
	{		
		final noalias SJService c = SJService.create(p, server, port);
		
		long totalRunTime = 0;		
	
		//SJSessionParameters params = TransportUtils.createSJSessionParameters(transports, transports, BUFFER_SIZE);
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(transports, transports, bufferSize);
		
		for (int i = 0; i < num; i++) 
		{
			Message msg = new Message(size);
			
			final noalias SJSocket ds;			
			
			try (ds) 
			{
				ds = c.request(params); // Dummy run.							
			
				int k = 0;
				
				ds.recursion(X)
				{
					if (k++ < 1)
					{
						ds.outbranch(Y)
						{
							ds.send(msg);
							
							msg = (Message) ds.receive();
														
							ds.recurse(X);
						}
					}
					else
					{
						ds.outbranch(N)
						{
						
						}
					}
				}
				
				msg = new Message(size);
				
				long runTime = 0;
				
				long timeStarted = 0;		
				long timeFinished = 0;	
				
				final noalias SJSocket s;
				
				try (s)
				{
					s = c.request(params); // Actual run.			
								
					timeStarted = System.nanoTime();

					int j = 0;
					
					s.recursion(X)
					{
						if (j++ < len)
						{
							s.outbranch(Y)
							{
								s.send(msg);
								
								msg = (Message) s.receive();
								
								if (debug)
								{
									msg.println();
								}
								
								s.recurse(X);
							}
						}
						else
						{
							s.outbranch(N)
							{
							
							}
						}
					}		
					
					timeFinished = System.nanoTime();										
				}							
				finally 
				{
					
				}				
					
				runTime = (timeFinished - timeStarted) / 1000; // Micros.
				
				totalRunTime += runTime;	
			}
			finally 
			{
				// Remember: for the Resending Protocol, session close spawns a thread.	So we nest the actual run within the dummy run session-try; and num > 1 is not desirable. 		
			}
		}
		
		//System.out.println("Total running time = " + totalRunTime + " micros");
		System.out.println(totalRunTime);
	}
	
	public static void main(String args[]) throws Exception 
	{			
		boolean debug = Boolean.parseBoolean(args[0]);
		String server = args[1];
		int port = Integer.parseInt(args[2]);
		String transports = args[3];		
				
		//TransportUtils.configureTransports(transports, transports);
		
		int size = Integer.parseInt(args[4]); 
		int len = Integer.parseInt(args[5]); // Session length.
		int bufferSize = Integer.parseInt(args[6]);
		int num = Integer.parseInt(args[7]); // Number of consecutive sessions to run.
			
		new Client().run(debug, server, port, transports, size, len, bufferSize, num);
	}
}
