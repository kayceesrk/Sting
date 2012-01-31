//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark3/b/Bob.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3.b.Bob false 8888 localhost 9999

package pldi.benchmarks.benchmark3.b;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

public class Bob
{	
	private final noalias protocol p
	{
		?[?(BinaryTree).!<BinaryTree>]*
	}

	private final noalias protocol p_alice 
	{
		sbegin.@(p)
	}
	
	private final noalias protocol p_carol 
	{
		cbegin.!<@(p)>
	}
	
	public Bob(boolean debug, int port, String carol, int carol_port) throws Exception 
	{							
		ServerThread st = new ServerThread(debug, port, carol, carol_port);		
		
		st.start();
	}
	
	private class ServerThread extends Thread 
	{
		private boolean debug;
		private int port;
		private String carol;
		private int carol_port;
		
		public ServerThread(boolean debug, int port, String carol, int carol_port)
		{
			this.debug = debug;
			this.port = port;
			this.carol = carol;
			this.carol_port = carol_port;
		}
		
		public void run()
		{
			final noalias SJServerSocket ss;
			
			try (ss)
			{
				boolean run = true;
				
				ss = SJServerSocketImpl.create(p_alice, port);
				
				new KillThread(port + 1, ss.getCloser()).start();
				
				final noalias SJService c = SJService.create(p_carol, carol, carol_port);
				
				BinaryTree bt = null;
				
				for (int counter = 0; run; counter++) 
				{																
					noalias SJSocket ds1;
					final noalias SJSocket ds2;
					
					try (ds1, ds2)
					{					
						ds1 = ss.accept(); // Dummy run.							
						ds2 = c.request();
						
						ds2.pass(ds1);	
		
						noalias SJSocket alice;
						final noalias SJSocket bob;
						
						try(alice, bob)
						{
							alice = ss.accept();
							bob = c.request();
					
							bob.pass(alice);														
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
		String carol = args[2];
		int carol_port = Integer.parseInt(args[3]);
	
		new Bob(debug, port, carol, carol_port);
	}
}
