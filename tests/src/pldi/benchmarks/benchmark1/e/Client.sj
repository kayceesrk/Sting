//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/e/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.e.Client false localhost 8888 1000 10 1
//$ tests/src/pldi/benchmarks/benchmark1/e/client.sh localhost 8888 1000 

/**
 * 
 */
package pldi.benchmarks.benchmark1.e;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;

import pldi.benchmarks.benchmark1.e.BigObject;

public class Client 
{	
	private final noalias protocol p 
	{
		cbegin.![!<BigObject>.?(BigObject)]*
	}	
	
	public void run(boolean debug, String server, int port, int size, int len, int num) throws Exception 
	{		
		final noalias SJService c = SJService.create(p, server, port);
				
		long totalRunTime = 0;		
	
		for (int i = 0; i < num; i++) 
		{
			// Create a balanced binary tree of size @size.
			BigObject bo = new BigObject(i, size);		
			
			final noalias SJSocket ds;			
			
			try (ds) 
			{
				ds = c.request(); // Dummy run.							
			
				int k = 0;
				
				ds.outwhile(k < 1)
				{
					ds.send(bo);
					
					bo = (BigObject) ds.receive();
					
					k++;
				}		
				
				bo = new BigObject(i, size);
				
				long runTime = 0;
				
				long timeStarted = 0;		
				long timeFinished = 0;	
				
				final noalias SJSocket s;
				
				try (s)
				{
					s = c.request(); // Actual run.			
								
					timeStarted = System.nanoTime();

					int j = 0;
					
					s.outwhile(j < len)
					{
						s.send(bo);
						
						bo = (BigObject) s.receive();
						
						if (debug)
						{
							bo.println();
						}
						
						j++;
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
		
		//System.out.println("Total running time = " + totalRunTime);
		System.out.println(totalRunTime);
	}
	
	private static void configureTransports()
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		ss.add(new SJStreamTCP());
		//ss.add(new SJManualTCP());		

		ts.add(new SJStreamTCP());
		//ts.add(new SJManualTCP());
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}
	
	public static void main(String args[]) throws Exception 
	{		
		configureTransports();
		
		boolean debug = Boolean.parseBoolean(args[0]);
		String server = args[1];
		int port = Integer.parseInt(args[2]);
		int size = Integer.parseInt(args[3]); 
		int len = Integer.parseInt(args[4]); // Session length.
		int num = Integer.parseInt(args[5]); // Number of consecutive sessions to run.
			
		new Client().run(debug, server, port, size, len, num);
	}
}
