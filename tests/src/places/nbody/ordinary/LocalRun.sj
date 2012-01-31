//$ bin/sessionjc -cp tests/classes/ tests/src/places/nbody/ordinary/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.nbody.ordinary.LocalRun m false 1 1 1

package places.nbody.ordinary;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

public class LocalRun
{
	private static void configureTransports(String transports)
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		if (transports.contains("m"))
		{
			ss.add(new SJFifoPair());
			ts.add(new SJFifoPair());
		}
		
		if (transports.contains("t"))
		{
			ss.add(new SJStreamTCP());
			ts.add(new SJStreamTCP());
		}
				
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}

	public static void main(final String[] args) throws Exception
	{
		final String transports = args[0];
	
		configureTransports(transports);
	
		final String debug = args[1];
		final String numParticlesLast = args[2];
		final String numParticlesFirst = args[3];
		final String reps = args[4];
	
		new Thread()
		{
			public void run()
			{
				try
				{
					LastWorker.main(new String[] { debug, "4444", "localhost", "4442", numParticlesLast });
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}					
			}
		}.start();
		
		Thread.sleep(500);
		
		new Thread()
		{
			public void run()
			{
				try
				{
					FirstWorker.main(new String[] { debug, "4442", "localhost", "4444", numParticlesFirst, reps });
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}				
			}
		}.start();
	}
}
