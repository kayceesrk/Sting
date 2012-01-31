//$ bin/sessionjc tests/src/places/pi2/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.pi2.Worker 4440 | 4441 | 4442 | ...

package places.pi2;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * @author Andi Bejleri
 *
 */
public class Worker
{	
	private final noalias protocol p_wm { sbegin.?(int).!<int> }
	
	public void run(int port) throws Exception
	{	
		/* Server that listens for connections from Master. */
		final noalias SJServerSocket ss;
		
		try (ss)
		{	
			/* Create and set up the server socket. */
			ss = SJServerSocketImpl.create(p_wm, port);
			
			while (true)
			{
				final noalias SJSocket wm;
				
				try (wm)
				{
					/* Accept the connection from Master. */
					wm = ss.accept();
					
					int hits = 0;
					int i = 0;
					
					int trials = wm.receiveInt();
					
					while(i < trials)
					{					
						if (hit())
						{
							hits++;
						}
						
						i++;
					}
					
					/* Send the result. */
					wm.send(hits); 
				}
				catch (SJIncompatibleSessionException ise)
				{
					System.err.println("[Worker] Non-dual behavior: " + ise);
				}
				catch (SJIOException sioe)
				{
					System.err.println("[Worker] Communication error: " + sioe);				
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
		
		Worker w = new Worker();
		
		w.run(port);
	}
	
	private boolean hit()
	{	
		double x = Math.random() * 2.0 - 1.0; // Not quite right: Math.random gives [0.0, 1.0) 
		double y = Math.random() * 2.0 - 1.0;
		
		return (x * x + y * y <= 1.0);		
	}
}