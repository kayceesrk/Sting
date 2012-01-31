//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark1/b/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark1.b.LocalRun false 8888 b 0 1 
//$ tests/src/popl/bmarks/bmark1/b/noalias.sh 8888 b 0 

package popl.bmarks.bmark1.b;

import java.util.*;

import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

import util.*;

import popl.bmarks.*;

/**
 * @author Raymond
 *
 */
public class LocalRun
{
	private static final int lens[] = { 0, 1, 10, 100, 1000 };
	//private static final int bufferSizes[] = { 7, 7, 34, 304, 3004 };
	private static final int bufferSizes[] = { 7, 7, 7, 7, 7 };
	
	private void run(final boolean debug, final int port, final String transports, final int size, final int repeat) throws Exception
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					new Server(debug, port, transports, 5011);
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
				final int fli = li;
				
				Thread t = new Thread()
				{
					public void run()
					{
						try
						{
							Client c = new Client();
							
							c.run(debug, "localhost", port, transports, size, len, bufferSizes[fli], 1);
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

	public static void main(String[] args) throws Exception
	{				
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		String transports = args[2];
		int size = Integer.parseInt(args[3]);
		int repeat = Integer.parseInt(args[4]);
			
		TransportUtils.configureTransports("f", "f");
		
		new LocalRun().run(debug, port, transports, size, repeat);
	}
}
