//$ bin/sjc -cp tests/classes/ tests/src/benchmarks/benchmark1/b/Client.sj -d tests/classes/
//$ bin/sj -cp tests/classes/ benchmarks.benchmark1.b.Client false localhost 8888 0 10 1
//$ tests/src/benchmarks/benchmark1/b/client.sh localhost 8888 0 

/**
 * 
 */
package benchmarks.benchmark1.b;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import sj.runtime.*;
import sj.runtime.net.*;

import benchmarks.BinaryTree;

public class Client 
{	
	private final protocol p 
	{
		begin.![!<BinaryTree>.?(BinaryTree)]*
	}	
	
	private void run(boolean debug, String server, int port, int depth, int len, int num) throws Exception
	{		
		SJServerAddress c = SJServerAddress.create(p, server, port);
		
		long totalRunTime = 0;		
	
		for (int i = 0; i < num; i++) 
		{
			// Create a balanced binary tree of depth @depth.
			BinaryTree bt = BinaryTree.createDepth(depth);		
			
			SJSocket ds = SJFSocket.create(c);			
			
			try (ds) 
			{
				ds.request(); // Dummy run.							
			
				int k = 0;
				
				ds.outwhile(k < 1)
				{
					ds.send(bt);
					
					bt = (BinaryTree) ds.receive();
					
					k++;
				}				
			
				bt = BinaryTree.createDepth(depth);
				
				long runTime = 0;
				
				long timeStarted = 0;		
				long timeFinished = 0;	
				
				SJSocket s = SJFSocket.create(c);
				
				try (s)
				{
					s.request(); // Actual run.			
								
					timeStarted = System.nanoTime();

					int j = 0;
					
					s.outwhile(j < len)
					{
						s.send(bt);
						
						bt = (BinaryTree) s.receive();
						
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
