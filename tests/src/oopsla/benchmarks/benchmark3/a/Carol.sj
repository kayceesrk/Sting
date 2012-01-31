//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark3/a/Carol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark3.a.Carol 9999

package benchmarks.benchmark3.a;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.*;

public class Carol
{	
	private final noalias protocol p_alice 
	{
		?[?(NoAliasBinaryTree).!<NoAliasBinaryTree>]*
	}	

	private final noalias protocol p_bob 
	{
		sbegin.?(@(p_alice))
	}		
	
	public Carol(int port) throws Exception 
	{							
		ServerThread st = new ServerThread(port);		
		
		st.start();
	}
	
	private class ServerThread extends Thread 
	{
		private int port;
		
		public ServerThread(int port)
		{
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
				
				noalias NoAliasBinaryTree bt;
				
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
							bt = (NoAliasBinaryTree) ds2.receive();
							 
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
								bt = (NoAliasBinaryTree) alice.receive();
								
								bt.inc();
								
								alice.send(bt);							
							}
						}
						finally 
						{ 
		
						}					
					}			
					catch (Exception x)
					{
						//x.printStackTrace();
						
						run = false;
					}
					finally
					{
						
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

	/*private static void configureTransports()
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		ss.add(new SJFifoPair());
		ss.add(new SJStreamTCP());
		
		ts.add(new SJFifoPair());
		ts.add(new SJStreamTCP());
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}*/

	public static void main(String[] args) throws Exception
	{		
		//configureTransports(); // Done by AliceCarol.
	
		int port = Integer.parseInt(args[0]);
	
		new Carol(port);
	}
}
