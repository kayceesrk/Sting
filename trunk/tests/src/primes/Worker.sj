//$ bin/sessionjc tests/src/primes/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ primes.Worker 4442

package primes;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * 
 * @author Raymond
 *
 */
public class Worker
{		
	private int port; 
	
	public Worker(int port)
	{
		this.port = port;
	}
	
	public void run() throws Exception
	{	
		final noalias SJServerSocket ss;
				
		try (ss)
		{	
			ss = SJServerSocketImpl.create(Protocols.p_worker, port);					
										
			while (true)
			{								
				final noalias SJSocket service;				
									
				try (service)
				{					
					service = ss.accept();
											
					int range = ((Integer) service.receive()).intValue();
					int lower = ((Integer) service.receive()).intValue();
					int upper = ((Integer) service.receive()).intValue();
					
					boolean[] foo = new boolean[range];
					
					Arrays.fill(foo, true);
					
					int numnotprimes = 0;
					
					for (int i = lower; i <= upper; i++)
					{
						boolean first = true;
						
						for (int j = 2 * i; j < range; j += i)
						{
							if (first)
							{
								if (!foo[i])
								{
									break;
								}
								
								first = false;
							}
							
							if (foo[j])
							{
								foo[j] = false;
								
								numnotprimes++;
							}
						}
					}										
					
					Integer[] notprimes = new Integer[numnotprimes];
					
					for (int i = 2 * lower, j = 0; i < range; i++)
					{
						if (!foo[i])
						{
							notprimes[j++] = new Integer(i);
						}
					}
					
					//System.out.println(workerId() + " : " + Arrays.toString(notprimes));
					
					service.send(notprimes);
				}				
				catch (SJIncompatibleSessionException ise)
				{
					System.err.println(workerId() + " incompatible service: " + ise);
				}				
				catch (SJIOException ioe)
				{
					System.err.println(workerId() + " IO error: " + ioe);				
				}						
			}											
		}
		finally
		{
			
		}	
	}
	
	public static void main(String[] args) throws Exception
	{		
		int port = Integer.parseInt(args[0]);
		
		new Worker(port).run();
	}
	
	private String workerId()
	{
		return "[Worker " + port + "]";
	}	
}
