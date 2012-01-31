//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark1/FClient.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmark1.FClient localhost 4444 2 100 1

/**
 * 
 * @author Andi
 *
 */
package benchmark1;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmark1.BinaryTree;

import benchmarks.BigObject;

public class FClient 
{
	
	private final noalias protocol p 
	{
		cbegin.![!<BinaryTree>.?(BinaryTree)]*
	}	
	
	public FClient(String server, int port, int depth, int singleSession, int manySessions) throws Exception 
	{				
		long timeStarted = 0;		
		long timeFinished = 0;
		
		long runningTimeManySessions = 0;
		long runningTimeOneSession = 0;
		
		//create the balanced binary tree of depth @depth
		BinaryTree theTree = BinaryTree.createBinaryTree(depth);
		
		final noalias SJService d = SJService.create(p, server, port);
		
		final noalias SJSocket ds;		
			
		try (ds) 
			{
				ds = d.request(); // Dummy run.							
				//System.out.println("Client connection established");
			
				int k = 1;
				ds.outwhile(k <= 100){
					//theTree.print();
					ds.send(theTree);
					theTree = (BinaryTree)ds.receive();
					k++;
				}
							
			}finally{}
		//System.out.println("Client dummy session done");
	
		
		for(int i = 1; i <= manySessions; i++){
			theTree = BinaryTree.createBinaryTree(depth);				
			final noalias SJSocket s;
			try(s){
				s = d.request(); // Actual run.			
							
				timeStarted = System.nanoTime();
				//System.out.println("Here");
				int k = 1;
				s.outwhile(k <= singleSession){
					//theTree.print();
					s.send(theTree);
					theTree = (BinaryTree)s.receive();
					k++;
				}		
				timeFinished = System.nanoTime();										
			}
							
			finally {}				
				
			runningTimeOneSession = (timeFinished - timeStarted) / 1000;
			runningTimeManySessions = runningTimeManySessions + runningTimeOneSession;	
		}
		//ps.println(runningTimeManySessions);
		System.out.println(runningTimeManySessions);
		//System.out.println("Mean session run: "+Math.round(running_mean/iterations));
	}

	public static void main(String args[]) throws Exception 
	{
		new FClient(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
	}
}
