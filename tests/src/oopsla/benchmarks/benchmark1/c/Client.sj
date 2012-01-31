//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark1/c/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark1.c.Client false localhost RMIBenchmarkObject 0 10 1
//$ tests/src/benchmarks/benchmark1/c/client.sh localhost RMIBenchmarkObject 0 

package benchmarks.benchmark1.c;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;

import benchmarks.BinaryTree;

public class Client 
{
	public void run(boolean debug, String rmiRegHost, String name, int depth, int len, int num) throws Exception
	{
		Registry registry = LocateRegistry.getRegistry(rmiRegHost);
		Server remObj = (Server) registry.lookup(name);
				
		long totalRunTime = 0;	
				
		//for(int i = 1; i <= num; i++)
		{
			BinaryTree bt = remObj.dummy(BinaryTree.createDepth(depth)); // Dummy run.			
			
			bt = BinaryTree.createDepth(depth); 
			
			long runTime = 0;
			
			long timeStarted = 0;		
			long timeFinished = 0;							
			
			//remObj.reset();
			
			timeStarted = System.nanoTime();
			
			for (int j = 0; j < len; j++) 
			{						
				bt = remObj.inc(bt);
				
				if (debug)
				{
					bt.println();
				}
			}
	
			timeFinished = System.nanoTime();
			
			remObj.end();
			
			runTime = (timeFinished - timeStarted) / 1000; // Micros.		
			
			totalRunTime += runTime;
			
			//System.out.println("Total running time = " + totalRunTime);
			System.out.println(totalRunTime);		
		}
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String rmiRegHost = args[1];
		String name = args[2];
		int depth = Integer.parseInt(args[3]); 
		int len = Integer.parseInt(args[4]); // Session length.
		int num = Integer.parseInt(args[5]); // Number of consecutive sessions to run. // Currently ignored for RMI (no way to close existing RMI connection).		
		
		new Client().run(debug, rmiRegHost, name, depth, len, num);
	}
}
