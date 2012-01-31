//$ bin/sessionjc tests/src/pi/Service.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pi.Service 4441 1024 localhost 4443 

package pi;

import java.io.*;
import java.math.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * 
 * @author Raymond, Andi
 *
 */
public class Service
{	
	private static final BigDecimal PI = new BigDecimal(Math.PI);
	private static final BigDecimal FOUR = new BigDecimal(4.0);	
	
	private final noalias protocol p_worker_client { ^(Protocols.p_worker) }
	
	private int port;
	private int workerSize;
	private List workerHosts;
	private List workerPorts;
	
	public Service(int port, int workerSize, List workerHosts, List workerPorts)
	{
		this.port = port;
		this.workerSize = workerSize;		
		this.workerHosts = workerHosts;
		this.workerPorts = workerPorts;
	}
	
	private BigDecimal pi;
	private int hits;
	private int shots;
	
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
					
					pi = new BigDecimal(0);
					hits = shots = 0;
					
					final double target = ((Double) client.receive()).doubleValue();				
					
					List threads = new LinkedList();
					
					final Iterator hosts = workerHosts.iterator();
					final Iterator ports = workerPorts.iterator();					
					
					for ( ; hosts.hasNext(); )
					{
						final String host = (String) hosts.next();
						final int port = ((Integer) ports.next()).intValue();
						
						Thread t = new Thread()
						{							
							public void run()
							{								
								final noalias SJService c_worker = SJService.create(p_worker_client, host, port);
								
								final noalias SJSocket worker;
								
								try (worker)
								{										
									worker = c_worker.request();								
									
									worker.send(new Integer(workerSize));						
									
									worker.outwhile(!done(target))
									{																	
										updatePi(((Integer) worker.receive()).intValue());									
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
					
					client.send(pi);
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
	
	private synchronized boolean done(double target)
	{
		return ((PI.subtract(this.pi)).abs().doubleValue() < target);
	}
	
	private synchronized void updatePi(int hits)
	{
		this.hits += hits;
		this.shots += workerSize;
		
		this.pi =  FOUR.multiply(new BigDecimal((double) this.hits / (double) this.shots));
				
		System.out.println("pi = " + pi);		
	}	
	
	public static void main(String args[]) throws Exception 
	{
		int port = Integer.parseInt(args[0]);
		int workerSize = Integer.parseInt(args[1]);
		
		List workerHosts = new LinkedList();
		List workerPorts = new LinkedList();		
		
		for (int i = 2; i < args.length; )
		{
			workerHosts.add(args[i++]);
			workerPorts.add(new Integer(Integer.parseInt(args[i++])));
		}
		
		new Service(port, workerSize, workerHosts, workerPorts).run();
	}
	
	private String serviceId()
	{
		return "[Service " + port + "]";
	}		
}
