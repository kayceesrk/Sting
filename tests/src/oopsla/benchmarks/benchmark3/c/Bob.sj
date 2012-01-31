//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark3/c/Bob.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark3.c.Bob false 8888 localhost 9999

package benchmarks.benchmark3.c;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.*;

public class Bob
{	
	private final noalias protocol p_alice 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
	}		
	
	private final noalias protocol p_carol 
	{
		cbegin.![!<BinaryTree>.?(BinaryTree)]*
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
				
				new KillThread(port + Kill.KILL_PORT_ADJUST, ss.getCloser()).start();
				
				final noalias SJService c = SJService.create(p_carol, carol, carol_port);
				
				BinaryTree bt = null;
				
				for (int counter = 0; run; counter++) 
				{																
					final noalias SJSocket ds1;
					final noalias SJSocket ds2;
					
					try (ds1, ds2)
					{					
						ds1 = ss.accept(); // Dummy run.							
						ds2 = c.request();
						
						ds2.outwhile(ds1.inwhile())
						{
							bt = (BinaryTree) ds1.receive();
							 
							ds2.send(bt);
							 
							bt = (BinaryTree) ds2.receive();
							 
							ds1.send(bt);
						}			
		
						final noalias SJSocket alice;
						final noalias SJSocket bob;
						
						try(alice, bob)
						{
							alice = ss.accept();
							bob = c.request();
							
							bob.outwhile(alice.inwhile())
							{					
								bt = (BinaryTree) alice.receive();
								
								bob.send(bt);
																								
								if (debug)
								{
									bt.println();
								}																							
																								
								bt = (BinaryTree) bob.receive();
								
								alice.send(bt);
							}								
							
							if (debug)
							{
								bt.println();
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
		String carol = args[2];
		int carol_port = Integer.parseInt(args[3]);
	
		new Bob(debug, port, carol, carol_port);
	}
}
