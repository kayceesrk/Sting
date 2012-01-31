//$ bin/sessionjc tests/src/pi/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pi.Worker 4443 localhost 4442 256

package pi;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * 
 * @author Raymond, Andi
 *
 */
public class Worker
{		
	private final noalias protocol p_random_client { ^(Protocols.p_random) }
	
	private int port;
	private String randomHost;
	private int randomPort;
	private int randomsSize; 
	
	public Worker(int port, String randomHost, int randomPort, int randomsSize)
	{
		this.port = port;
		this.randomHost = randomHost;
		this.randomPort = randomPort;
		this.randomsSize = randomsSize;
	}
	
	public void run() throws Exception
	{	
		final noalias SJService c_random = SJService.create(p_random_client, randomHost, randomPort);
		
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
											
					final int workerSize = ((Integer) service.receive()).intValue();
					
					service.inwhile()
					{						
						final noalias SJSocket random;
						
						int iters = workerSize;
						int hits = 0;						
						
						try (random)
						{								
							random = c_random.request();																
							
							random.outwhile(iters > 0)
							{	
								if ((iters * 2) >= randomsSize)
								{
									random.send(new Integer(randomsSize));
								}
								else
								{
									random.send(new Integer(iters * 2));
								}
								
								Double[] rands = (Double[]) random.receive();														
								
								//System.out.println("rands = " + iters + " " + rands.length);
								
								for (int i = rands.length; i > 0; iters--)
								{
									if (hit(rands[--i], rands[--i]))
									{
										hits++;
									}
								}
							}																																				
						}
						catch (SJIncompatibleSessionException ise)
						{
							System.err.println(workerId() + " rejected by Random: " + ise);
						}
						
						service.send(new Integer(hits));
					}									
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
		String randomHost = args[1];
		int randomPort = Integer.parseInt(args[2]);
		int randsSize = Integer.parseInt(args[3]);
		
		new Worker(port, randomHost, randomPort, randsSize).run();
	}
	
	public boolean hit(Double d1, Double d2)
	{	
		double x = d1.doubleValue();
		double y = d2.doubleValue();
		
		return (x * x + y * y) <= 1.0;		
	}
	
	private String workerId()
	{
		return "[Worker " + port + "]";
	}	
}
