//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/d/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.d.Client false localhost 8888 0 10 1
//$ tests/src/pldi/benchmarks/benchmark1/d/client.sh localhost 8888 0 

/**
 * 
 */
package pldi.benchmarks.benchmark1.d;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import pldi.benchmarks.BinaryTree;

public class Client 
{	
	public void run(boolean debug, String server, int port, int depth, int len, int num) throws Exception // public because used by benchmark2.
	{		
		long totalRunTime = 0;		
	
		for (int i = 0; i < num; i++) 
		{
			// Create a balanced binary tree of depth @depth.
			BinaryTree bt = BinaryTree.createDepth(depth);		
			
			Socket ds = null;			
			
			ObjectOutputStream doos = null;
			ObjectInputStream dois = null;			
			
			try 
			{
				ds = new Socket(server, port); // Dummy run.							
			
				ds.setTcpNoDelay(true);
				
				doos = new ObjectOutputStream(ds.getOutputStream());
				dois = new ObjectInputStream(ds.getInputStream());				
										
				int k = 0;
				
				while (k < 1)
				{
					doos.writeBoolean(true);
					doos.flush();
					
					doos.writeObject(bt);
					
					bt = (BinaryTree) dois.readObject();
					
					k++;
				}		
			
				doos.writeBoolean(false);
				doos.flush();
				
				bt = BinaryTree.createDepth(depth);
				
				long runTime = 0;
				
				long timeStarted = 0;		
				long timeFinished = 0;	
				
				Socket s = null;
				
				ObjectOutputStream oos = null;
				ObjectInputStream ois = null;																							
				
				try 
				{
					s = new Socket(server, port); // Actual run.			
								
					s.setTcpNoDelay(true);
					
					oos = new ObjectOutputStream(s.getOutputStream());
					ois = new ObjectInputStream(s.getInputStream());	
					
					timeStarted = System.nanoTime();

					int j = 0;
					
					while (j < len)
					{
						oos.writeBoolean(true);
						oos.flush();
						
						oos.writeObject(bt);
						
						bt = (BinaryTree) ois.readObject();
						
						if (debug)
						{
							bt.println();
						}
						
						j++;
					}		
					
					oos.writeBoolean(false);
					oos.flush();
					
					timeFinished = System.nanoTime();										
				}							
				finally 
				{
					closeUp(ds, dois, doos);
				}				
					
				runTime = (timeFinished - timeStarted) / 1000; // Micros.
				
				totalRunTime += runTime;	
			}
			finally 
			{
				// Remember: for the Resending Protocol, session close spawns a thread.	So we nest the actual run within the dummy run session-try; and num > 1 is not desirable.
				
				closeUp(ds, dois, doos);
			}			
		}
		
		//System.out.println("Total running time = " + totalRunTime);
		System.out.println(totalRunTime);		
	}
	
	private void closeUp(Socket s, ObjectInputStream ois, ObjectOutputStream oos) throws IOException
	{
		if (ois != null) 
		{
			ois.close();							
		}
		
		if (oos != null) 
		{ 
			oos.flush(); 
			oos.close(); 
		}
		
		if (s != null) 
		{
			s.close();		
		}
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
