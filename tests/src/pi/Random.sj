//$ bin/sessionjc tests/src/pi/Random.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pi.Random 4442

package pi;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * @author Raymond
 *
 */
public class Random
{	
	private java.util.Random r = new java.util.Random();
	
	private int port;
	
	public Random(int port)
	{
		this.port = port;
	}	
	
	public void run() throws Exception
	{	
		final noalias SJServerSocket ss;
				
		try(ss)
		{	
			ss = SJServerSocketImpl.create(Protocols.p_random, port);
		
			while (true)
			{
				final noalias SJSocket worker;
					
				try (worker)
				{
					worker = ss.accept();					
					
					worker.inwhile()
					{
						int randsSize = ((Integer) worker.receive()).intValue();
						
						Double[] rands = new Double[randsSize];
						
						for (int i = 0; i < randsSize; i++)
						{
							rands[i] = new Double(r.nextDouble());
						}
						
						worker.send(rands);
					}							
				}
				catch (SJIncompatibleSessionException ise)
				{
					System.err.println(randomId() + " incompatible worker: " + ise);
				}
				catch (SJIOException ioe)
				{
					System.err.println(randomId() + " IO error: " + ioe);				
				}	
			}
		}
		finally
		{
			
		}	
	}
	
	public static void main(String[] args) throws Exception
	{		
		final int port = Integer.parseInt(args[0]);

		new Random(port).run();
	}
	
	private String randomId()
	{
		return "[Random " + port + "]";
	}
}
