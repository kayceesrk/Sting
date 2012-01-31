//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark2/c/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark2.c.Client false localhost 8888 0 10 1
//$ tests/src/pldi/benchmarks/benchmark2/c/client.sh localhost 8888 0 

/**
 * 
 */
package pldi.benchmarks.benchmark2.c;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.NoAliasBinaryTree;

/**
 * The same as benchmark1.a.Client except uses benchmarks.NoAliasBinaryTree and noalias.
 */
public class Client 
{	
	private final noalias protocol p 
	{
		cbegin.![!<NoAliasBinaryTree>.?(NoAliasBinaryTree)]*
	}	
	
	public void run(boolean debug, String server, int port, int depth, int len, int num) throws Exception
	{		
		final noalias SJService c = SJService.create(p, server, port);
		
		long totalRunTime = 0;		
	
		for (int i = 0; i < num; i++) 
		{
			// Create a balanced binary tree of depth @depth.
			noalias NoAliasBinaryTree bt = NoAliasBinaryTree.createDepth(depth);		
			
			final noalias SJSocket ds;			
			
			try (ds) 
			{
				ds = c.request(); // Dummy run.							
			
				int k = 0;
				
				ds.outwhile(k < 1)
				{
					ds.send(bt);
					
					bt = (NoAliasBinaryTree) ds.receive();
					
					k++;
				}		
			
				bt = NoAliasBinaryTree.createDepth(depth);
				
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
						s.send(bt);
						
						bt = (NoAliasBinaryTree) s.receive();
						
						if (debug)
						{
							bt.println();
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
	
	public static void main(String args[]) throws Exception 
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String server = args[1];
		int port = Integer.parseInt(args[2]);
		int depth = Integer.parseInt(args[3]); 
		int len = Integer.parseInt(args[4]); // Session length.
		int num = Integer.parseInt(args[5]); // Number of consecutive sessions to run.
			
		new Client().run(debug, server, port, depth, len, num);
	}
}
