//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark3/c/Carol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3.c.Carol false 9999

package pldi.benchmarks.benchmark3.c;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

public class Carol
{	
	private final noalias protocol p_bob 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
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
				
				new KillThread(port + 1, ss.getCloser()).start();
				
				BinaryTree bt;
				
				for (int counter = 0; run; counter++) 
				{						
					final noalias SJSocket ds;
					
					try (ds)
					{					
						ds = ss.accept(); // Dummy run.							
						
						ds.inwhile()
						{
							bt = (BinaryTree) ds.receive();
							 
							bt.inc();
							 
							ds.send(bt);
						}				

						final noalias SJSocket bob;
				
						try (bob)
						{
							bob = ss.accept();			
								
							bob.inwhile()
							{								
								bt = (BinaryTree) bob.receive();
								
								bt.inc();
								
								bob.send(bt);
								
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
	
	private class KillThread extends Thread
	{				
		private int port;
		private SJServerSocketCloser ssc;
		
		public KillThread(int port, SJServerSocketCloser ssc)
		{
			this.port = port;
			this.ssc = ssc;
		}
		
		public void run()
		{
			final noalias protocol q { sbegin }
			
			final noalias SJServerSocket ss;
			
			try (ss)
			{
				ss = SJServerSocketImpl.create(q, port);
				
				final noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();					
				}
				finally
				{
					
				}
			}
			catch (Exception x)
			{
				x.printStackTrace();
			}
			finally
			{
				ssc.close();
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
