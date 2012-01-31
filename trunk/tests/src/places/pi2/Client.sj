//$ bin/sessionjc tests/src/places/pi2/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.pi2.Client localhost 4439 1000 

package places.pi2;

import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * @author Andi Bejleri
 *
 */
public class Client
{	
	private final noalias protocol p_cm { cbegin.!<int>.?(Double) }
	
	public void run(int trials, String host_m, int port_m) 
	{	
		/* Master address. */
		final noalias SJService c_m = SJService.create(p_cm, host_m, port_m);
		
		/* Client-Master socket. */
		final noalias SJSocket cm;
		
		long timeStarted = 0;		
		long timeFinished = 0;
		
		try (cm)
		{					
			/* Set up the connection with Master. */
			cm = c_m.request();
			
			timeStarted = System.nanoTime();
			
			cm.send(trials);
			
			/* Receiving the value of pi. */
			Double pi = (Double) cm.receive();
			
			timeFinished = System.nanoTime();
			
			System.out.println("pi = " + pi);
			
			System.out.println("time = " + ((timeFinished - timeStarted) / 1000) + " micros");								
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Client] Non-dual behavior: " + ise);
		}
		catch (SJIOException sioe)
		{
			System.err.println("[Client] Communication error: " + sioe);		
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new RuntimeException(cnfe);
		}
	}
	
	public static void main(String args[]) throws Exception
	{			
		String host_m = args[0];
		int port_m = Integer.parseInt(args[1]);
		int trials = Integer.parseInt(args[2]);
		
		Client c = new Client();
		
		c.run(trials, host_m, port_m);
	}
}
