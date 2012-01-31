//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark3/c/Alice.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks/benchmark3/c/Alice false localhost 8888 0 1
//$ tests/src/benchmarks/benchmark3/c/alice.sh localhost 8888 0 -k localhost 9999 

package benchmarks.benchmark3.c;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.BinaryTree;

public class Alice
{	
	private final noalias protocol p_bob
	{
		cbegin.![!<BinaryTree>.?(BinaryTree)]*
	}	
	
	public void run(boolean debug, String bob, int bob_port, int depth, int len) throws Exception 
	{	
		final noalias SJService c = SJService.create(p_bob, bob, bob_port);
		
		BinaryTree bt = BinaryTree.createDepth(depth);
		
		final noalias SJSocket ds; 
				
		try (ds) 
		{   
			ds = c.request();

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
			
			final noalias SJSocket s;
			
			try (s) 
			{
				s = c.request();
				
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
							
			System.out.println(runTime);
		}
		finally
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String bob = args[1];
		int bob_port = Integer.parseInt(args[2]);
		int depth = Integer.parseInt(args[3]);
		int len = Integer.parseInt(args[4]);
	
		new Alice().run(debug, bob, bob_port, depth, len);
	}
}