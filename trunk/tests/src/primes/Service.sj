//$ bin/sessionjc tests/src/primes/Service.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ primes.Service 4441 localhost 4442

package primes;

import java.io.*;
import java.math.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * 
 * @author Raymond
 *
 */
public class Service
{	
	private final noalias protocol p_worker_client { ^(Protocols.p_worker) }
	
	private int port;
	private List workerHosts;
	private List workerPorts;
	
	private int numWorkers;
	
	public Service(int port, List workerHosts, List workerPorts)
	{
		this.port = port;
		this.workerHosts = workerHosts;
		this.workerPorts = workerPorts;
		
		this.numWorkers = workerHosts.size();
	}
	
	public void run() throws Exception 
	{			
		final noalias SJServerSocket ss;
		
		try (ss)
		{		
			ss = SJServerSocketImpl.create(Protocols.p_service, port);

			while (true)
			{		
				final noalias SJSocket client;			
 				
				try (client)
				{			
					client = ss.accept();								
					
					final int range = ((Integer) client.receive()).intValue();
					
					final boolean[] foo = new boolean[range];
					
					Arrays.fill(foo, true);
					
					List threads = new LinkedList();												
					
					final int interval = (int) Math.sqrt(range) / numWorkers; // Maybe need to add 1 to the sqrt(range). 
					
					for (int i = 0; i < numWorkers; i++)
					{
						final String host = (String) workerHosts.get(i);
						final int port = ((Integer) workerPorts.get(i)).intValue();
						
						final int fi = i;
						
						Thread t = new Thread()
						{							
							public void run()
							{								
								final noalias SJService c_worker = SJService.create(p_worker_client, host, port);
								
								final noalias SJSocket worker;							
								
								try (worker)
								{										
									worker = c_worker.request();								
									
									worker.send(new Integer(range));
									
									int bar = fi * interval;
									
									worker.send(new Integer(bar <= 2 ? 2 : bar));
									
									if ((range - bar) < interval)
									{
										worker.send(new Integer(range));
									}
									else
									{
										worker.send(new Integer(bar + interval));
									}
									
									Integer[] notprimes = (Integer[]) worker.receive();
									
									for (int j = 0; j < notprimes.length; j++)
									{									
										foo[notprimes[j].intValue()] = false;
									}
								}
								catch(SJIncompatibleSessionException ise)
								{
									System.err.println(serviceId() + " rejected by Worker: " + ise);
								}		
								catch(SJIOException ioe)
								{
									System.err.println(serviceId() + " IO error: " + ioe);			
								}
								catch (ClassNotFoundException cnfe)
								{
									throw new RuntimeException(cnfe);
								}
							}
						};
						
						threads.add(t);
						
						t.start();
					}																								
					
					for (Iterator i = threads.iterator(); i.hasNext(); )
					{
						((Thread) i.next()).join();
					}				
					
					Integer[] primes = new Integer[range];
					
					for (int i = 2, j = 0; i < range; i++)
					{
						if (foo[i])
						{
							primes[j++] = new Integer(i);
						}
					}
					
					int numprimes = range;
					
					for (int i = range - 1; primes[i] == null; i--)
					{
						numprimes--;
					}
					
					Integer[] bar = new Integer[numprimes];
					
					System.arraycopy(primes, 0, bar, 0, numprimes);
					
					client.send(bar);
				}
				catch(SJIncompatibleSessionException ise)
				{
					System.err.println(serviceId() + " incompatible client: " + ise);
				}
				catch(SJIOException ioe)
				{
					System.err.println(serviceId() + " IO error: " + ioe);			
				}			
			}
		}		
		finally		
		{
			
		}		
	}
	
	public static void main(String args[]) throws Exception 
	{
		int port = Integer.parseInt(args[0]);
		
		List workerHosts = new LinkedList();
		List workerPorts = new LinkedList();		
		
		for (int i = 1; i < args.length; )
		{
			workerHosts.add(args[i++]);
			workerPorts.add(new Integer(Integer.parseInt(args[i++])));
		}
		
		new Service(port, workerHosts, workerPorts).run();
	}
	
	private String serviceId()
	{
		return "[Service " + port + "]";
	}		
}
