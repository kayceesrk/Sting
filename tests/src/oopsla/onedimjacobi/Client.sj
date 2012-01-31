//$ bin/sessionjc -cp tests/classes/ tests/src/onedimjacobi/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ onedimjacobi.Client localhost 4444 s 10

package onedimjacobi;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

public class Client 
{	
	private final noalias protocol p_cm { cbegin.!<int>.?(double[][]) }
	
	public void run(String host_m, int port_m, int size) // size is length of square matrix side. 
	{	
		/* Master address. */
		final noalias SJService c_m = SJService.create(p_cm, host_m, port_m);
		
		/* Client-Master socket. */
		final noalias SJSocket cm;
		
		long timeStarted = 0;		
		long timeFinished = 0;
		
		try (cm)
		{		
			cm = c_m.request();
			
			timeStarted = System.nanoTime();
			
			// Size of the problem.			
			cm.send(size);
			
			// Result returned.
			
			double[][] result = (double[][]) cm.receive();
			
			printMatrix(result);
			
			timeFinished = System.nanoTime();
			
			System.out.println("time = " + (timeFinished - timeStarted) / 1000 + " micros");
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
			System.err.println("[Client] Class error: " + cnfe);
		}
		finally
		{
		
		}			
	}
	
	public static void main(String args[])
	{		
		String host_m = args[0];
		int port_m = Integer.parseInt(args[1]);
		String transports = args[2];

		configureTransports(transports);

		int size = Integer.parseInt(args[3]); 
	
		Client c = new Client();
		
		c.run(host_m, port_m, size);
	}

	private static void configureTransports(String transports)
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		if (transports.contains("f"))
		{
			ss.add(new SJFifoPair());
			ts.add(new SJFifoPair());
		}
		
		if (transports.contains("s"))
		{
			ss.add(new SJStreamTCP());
			ts.add(new SJStreamTCP());
		}
				
		if (transports.contains("m"))
		{
			ss.add(new SJManualTCP());
			ts.add(new SJManualTCP());
		}		
				
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}

	private void printMatrix(double[][] u)
	{
		for (int i = 0; i < u.length; i++)
		{
			System.out.println(Arrays.toString(u[i]));	
		}
	}		
}