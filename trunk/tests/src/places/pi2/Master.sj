//$ bin/sessionjc tests/src/places/pi2/Master.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.pi2.Master 4439 camelot03 4440 camelot04 4441 camelot05 4442 camelot06 4443 camelot07 4444 camelot08 4445 camelot09 4446 camelot10 4447 camelot18 4448 camelot12 4449 camelot13 4450 camelot14 4451 camelot15 4452 camelot16 4453

package places.pi2;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * @author Andi Bejleri
 *
 */
public class Master //extends Worker
{	
	private static final int NUM_WORKERS = 2;
	
	private final noalias protocol p_mc { sbegin.?(int).!<Double> }
	private final noalias protocol p_mw { cbegin.!<int>.?(int) }
	
	public void run(int port_m, String[] hosts_w, int[] ports_w) throws Exception
	{		
		if (hosts_w.length != NUM_WORKERS || hosts_w.length != NUM_WORKERS)
		{
			throw new RuntimeException("[Master] Expected " + NUM_WORKERS + " workers.");
		}
		
		/* Master-Worker socket. */
		final noalias SJService c_w2 = SJService.create(p_mw, hosts_w[0], ports_w[0]);						
		final noalias SJService c_w3 = SJService.create(p_mw, hosts_w[1], ports_w[1]);		
		/*final noalias SJService c_w4 = SJService.create(p_mw, hosts_w[2], ports_w[2]);
		final noalias SJService c_w5 = SJService.create(p_mw, hosts_w[3], ports_w[3]);
		final noalias SJService c_w6 = SJService.create(p_mw, hosts_w[4], ports_w[4]);						
		final noalias SJService c_w7 = SJService.create(p_mw, hosts_w[5], ports_w[5]);		
		final noalias SJService c_w8 = SJService.create(p_mw, hosts_w[6], ports_w[6]);
		final noalias SJService c_w9 = SJService.create(p_mw, hosts_w[7], ports_w[7]);
		final noalias SJService c_w10 = SJService.create(p_mw, hosts_w[8], ports_w[8]);						
		final noalias SJService c_w11 = SJService.create(p_mw, hosts_w[9], ports_w[9]);		
		final noalias SJService c_w12 = SJService.create(p_mw, hosts_w[10], ports_w[10]);
		final noalias SJService c_w13 = SJService.create(p_mw, hosts_w[11], ports_w[11]);
		final noalias SJService c_w14 = SJService.create(p_mw, hosts_w[12], ports_w[12]);						
		final noalias SJService c_w15 = SJService.create(p_mw, hosts_w[13], ports_w[13]);*/				
		
		/* Master socket that listens for connections from Client. */
		final noalias SJServerSocket ss;				
		
		try (ss)
		{		
			/* Create and set up the server socket. */
			ss = SJServerSocketImpl.create(p_mc, port_m);

			while (true)
			{		
				final noalias SJSocket mc;			
				final noalias SJSocket mw1, mw2/*, mw3, mw4, mw5, mw6, mw7, mw8, mw9, mw10, mw11, mw12, mw13, mw14*/; 
				
				try (mc, mw1, mw2/*, mw3, mw4, mw5, mw6, mw7, mw8, mw9, mw10, mw11, mw12, mw13, mw14*/)
				{													
					/* Accept the connection from Client. */
					mc = ss.accept();
					
					int trials = mc.receiveInt();
					
					mw1 = c_w2.request();
					mw2 = c_w3.request();
					/*mw3 = c_w4.request();
					mw4 = c_w5.request();
					mw5 = c_w6.request();
					mw6 = c_w7.request();
					mw7 = c_w8.request();
					mw8 = c_w9.request();
					mw9 = c_w10.request();
					mw10 = c_w11.request();
					mw11 = c_w12.request();
					mw12 = c_w13.request();
					mw13 = c_w14.request();
					mw14 = c_w15.request();*/				
													
					int hits = 0;
					
					int runs = trials / (NUM_WORKERS + 1); // Rounding errors (doesn't matter too much).
					
					<mw1, mw2/*, mw3, mw4, mw5, mw6, mw7, mw8, mw9, mw10, mw11, mw12, mw13, mw14*/>.send(runs);
					
					int i = 0;
					
					while (i < runs)
					{				
						if (hit())
						{
							hits++;
						}
						
						i++;					
					}
					
					/* Receive number of points that fall inside the circle from Workers. */
					hits += mw1.receiveInt();
					hits += mw2.receiveInt();
					/*hits += mw3.receiveInt();
					hits += mw4.receiveInt();
					hits += mw5.receiveInt();
					hits += mw6.receiveInt();
					hits += mw7.receiveInt();
					hits += mw8.receiveInt();
					hits += mw9.receiveInt();
					hits += mw10.receiveInt();
					hits += mw11.receiveInt();
					hits += mw12.receiveInt();
					hits += mw13.receiveInt();
					hits += mw14.receiveInt();*/
					
					double pi = 4.0 * ((double) hits) / ((double) trials);
					
					/* Send pi. */
					mc.send(new Double(pi)); // FIXME: should support double.
				}
				catch (SJIncompatibleSessionException ise)
				{
					System.err.println("[Master] Non-dual behavior: " + ise);
				}
				catch (SJIOException sioe)
				{
					System.err.println("[Master] Communication error: " + sioe);			
				}			
			}
		}		
		finally		
		{
			
		}		
	}
	
	public boolean hit()
	{	
		double x = Math.random() * 2.0 - 1.0;
		double y = Math.random() * 2.0 - 1.0;
		
		return (x * x + y * y <= 1.0);
	}
	
	public static void main(String args[]) throws Exception 
	{
		System.out.println(java.util.Arrays.toString(args));
		
		int port_master = Integer.parseInt(args[0]);

		String[] hosts_w = new String[NUM_WORKERS];
		int[] ports_w = new int[NUM_WORKERS];
		
		for (int i = 0; i < NUM_WORKERS; i++)
		{
			hosts_w[i] = args[1 + i * 2];
			ports_w[i] = Integer.parseInt(args[2 + i * 2]);
		}
	
		Master s = new Master();
		
		s.run(port_master, hosts_w, ports_w);
	}
}
