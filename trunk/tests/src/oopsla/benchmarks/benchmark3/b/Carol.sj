//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark3/b/Carol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark3.b.Carol false 9999

package benchmarks.benchmark3.b;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.*;

public class Carol
{	
	private final noalias protocol p_alice 
	{
		?[?(BinaryTree).!<BinaryTree>]*
	}	

	private final noalias protocol p_bob 
	{
		sbegin.?(@(p_alice))
	}		
	
	public Carol(boolean debug, int port) throws Exception 
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
				
				ss = SJServerSocketImpl.create(p_bob, port);
				
				new KillThread(port + Kill.KILL_PORT_ADJUST, ss.getCloser()).start();
				
				BinaryTree bt;
				
				for (int counter = 0; run; counter++) 
				{						
					final noalias SJSocket ds1;
					final noalias SJSocket ds2;
					
					try (ds1, ds2)
					{					
						ds1 = ss.accept(); // Dummy run.							
						
						ds2 = (@(p_alice)) ds1.receive();
						
						ds2.inwhile()
						{
							bt = (BinaryTree) ds2.receive();
							 
							bt.inc();
							 
							ds2.send(bt);
						}				

						final noalias SJSocket bob;
						final noalias SJSocket alice;
				
						try (bob, alice)
						{
							bob = ss.accept();			
							
							alice = (@(p_alice)) bob.receive();
							
							alice.inwhile()
							{								
								bt = (BinaryTree) alice.receive();
								
								bt.inc();
								
								alice.send(bt);
								
							 	if (debug)
							 	{
							 		bt.println();
							 	}								
							}
						}
						finally 
						{ 
		
						}					
					}			
					catch (Exception x)
					{
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
				
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{		
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
	
		new Carol(debug, port);
	}
}
