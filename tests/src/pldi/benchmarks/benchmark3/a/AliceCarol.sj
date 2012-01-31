//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark3/a/AliceCarol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3/a.AliceCarol fs false 9999 localhost 8888 0 1

package pldi.benchmarks.benchmark3.a;

import java.util.*;

import pldi.benchmarks.benchmark1.a.*;
//import pldi.benchmarks.benchmark2.d.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

/**
 * @author Raymond, Andi
 *
 */
public class AliceCarol
{
	private static final int lens[] = { 0, 1, 10, 100, 1000 };
	
	private void run(final boolean debug, final int carol_port, final String bob, final int bob_port, final int depth, final int repeat) throws Exception
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					new pldi.benchmarks.benchmark3.a.Carol(carol_port);
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}					
			}
		}.start();		
		
		Thread.sleep(500);
		
		for (int i = 0; i < repeat; i++)
		{
			for (int li = 0; li < lens.length; li++)
			{
				Thread.sleep(100);
				
				final int len = lens[li];
				
				Thread t = new Thread()
				{
					public void run()
					{
						try
						{
							Alice a = new pldi.benchmarks.benchmark3.a.Alice();
							
							a.run(debug, bob, bob_port, depth, len);
						}
						catch (Exception x)
						{
							throw new RuntimeException(x);
						}				
					}
				};
				
				t.start();
				t.join();
			}		
		}
		
		KillBobCarol.main(new String[] { bob, new Integer(bob_port).toString(), "localhost", new Integer(carol_port).toString() });
		//Kill.main(new String[] { "localhost", new Integer(carol_port).toString() });
	}

	private static void configureTransports(String transports)
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		if (!transports.contains("d"))
		{
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
		}
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}
	
	public static void main(String[] args) throws Exception
	{					
		String transports = args[0];
		
		configureTransports(transports);
		
		boolean debug = Boolean.parseBoolean(args[1]);
		int carol_port = Integer.parseInt(args[2]);
		String bob = args[3];
		int bob_port = Integer.parseInt(args[4]);
		int depth = Integer.parseInt(args[5]);
		int repeat = Integer.parseInt(args[6]);
		
		new AliceCarol().run(debug, carol_port, bob, bob_port, depth, repeat);
	}
}
