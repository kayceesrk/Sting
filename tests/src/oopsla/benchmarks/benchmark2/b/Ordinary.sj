//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark2/b/Ordinary.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark2.b.Ordinary false 8888 0 1
//$ tests/src/benchmarks/benchmark2/b/ordinary.sh 8888 0

package benchmarks.benchmark2.b;

import java.util.*;

import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

import benchmarks.*;
import benchmarks.benchmark1.a.*;
import benchmarks.benchmark2.d.*;

/**
 * @author Raymond, Andi
 *
 */
public class Ordinary
{
	private static final int lens[] = { 0, 1, 10, 100, 1000 };
	
	private void run(final boolean debug, final int port, final int depth, final int repeat) throws Exception
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					new benchmarks.benchmark2.d.Server(port);
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}					
			}
		}.start();		
		
		Thread.sleep(1000);
		
		for (int i = 0; i < repeat; i++)
		{
			for (int li = 0; li < lens.length; li++)
			{
				final int len = lens[li];
				
				Thread t = new Thread()
				{
					public void run()
					{
						try
						{
							new benchmarks.benchmark1.a.Client().run(debug, "localhost", port, depth, len, 1);
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
		
		Kill.main(new String[] { "localhost", new Integer(port).toString() });
	}

	private static void configureTransports()
	{
		List ss = new LinkedList();
		List ts = new LinkedList();

		ss.add(new SJFifoPair());		
		ss.add(new SJStreamTCP());
		//ss.add(new SJManualTCP());		

		ts.add(new SJFifoPair());
		ts.add(new SJStreamTCP());
		//ts.add(new SJManualTCP());
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}	
	
	public static void main(String[] args) throws Exception
	{		
		configureTransports();
		
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		int depth = Integer.parseInt(args[2]);
		int repeat = Integer.parseInt(args[3]);
		
		new Ordinary().run(debug, port, depth, repeat);
	}
}
