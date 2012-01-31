//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark1/a/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark1.a.LocalRun false 8888 b 1024 2 
//$ tests/src/popl/bmarks/bmark1/a/ordinary.sh 8888 b 1024 -r 2 

package popl.bmarks.bmark1.a;

import java.util.*;

import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import popl.bmarks.*;

/**
 * @author Raymond
 *
 */
public class LocalRun
{
	private static final int lens[] = { 0, 1, 10, 100, 1000 };
	//private static final int bufferSizes[] = { 7, 7, 34, 304, 3004 };
	private static final int bufferSizes[] = { 7, 7, 7, 7, 7 }; // Input buffer size for Client: upper message bound is just one, but that is three "messages" at the transport level, and a constant factor of four is added for opening and closing - again three for the session type object at init. and one more for the SJFIN). (These numbers are for non-noalias.)  
	
	private void run(final boolean debug, final int port, final String transports, final int size, final int repeat) throws Exception
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					//new Server(debug, port, transports, 5011);
					new Server(debug, port, transports, 10); // From Server side: upper message bound is two, so buffer size is three bigger than Client. However, application still works for buffer size 9 - probably because we always finish reading the initiation messages quickly enough before overwriting them.   
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
			
		SJTransportUtils.configureTransports("f", "f"); // For KillThreads (communicating with Servers).
		
		new LocalRun().run(debug, port, transports, size, repeat);
	}
}
