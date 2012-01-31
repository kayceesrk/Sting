//$ bin/sessionjc -cp tests/classes tests/src/serverclient/MemTest.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ serverclient.MemTest f f false 8888 1 1

package serverclient;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;


import serverclient.server.*;
import serverclient.client.*;

public class MemTest
{	
	public static void main(String[] args) throws Exception
	{								
		/*final int port = Integer.parseInt(args[0]);
		final int len = Integer.parseInt(args[1]);*/
		
		final String setups = args[0];
		final String transports = args[1];
		
		//configureTransports(setups, transports);
		
		final String debug = args[2];
		final String port = args[3];		
		final String size = args[4];
		final String len = args[5];
		
		new Thread()
		{
			public void run()
			{
				try
				{
					//new Server().run(port);
					//new Server().main(new String[] { debug, "d", "d", port });
					new Server().main(new String[] { debug, setups, transports, port });
				}
				catch (Exception x)
				{
					System.out.println("Server: " + x);
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
					//new Client().run("localhost", port, len);
					//new Client().main(new String[] { debug, "d", "d", "localhost", port, size, len });
					new Client().main(new String[] { debug, setups, transports, "localhost", port, size, len });
				}
				catch (Exception x)
				{
					System.out.println("Client: " + x);
				}				
			}
		}.start();		
	}
	
	private static void configureTransports(String setups, String transports)
	{
		SJTransportManager sjtm = SJRuntime.getTransportManager();	
		
		if (!setups.contains("d"))
		{
			List ss = new LinkedList();
			
			parseTransportFlags(ss, setups);		
			
			sjtm.configureSetups(ss);
		}
		
		if (!transports.contains("d"))
		{
			List ts = new LinkedList();
			
			parseTransportFlags(ts, transports);	
			
			sjtm.configureTransports(ts);
		}		
	}	
	
	private static void parseTransportFlags(List ts, String transports)
	{
		if (transports.contains("d"))
		{
			ts.add(new SJFifoPair());
			ts.add(new SJStreamTCP());
			
			return;
		}
		
		char[] cs = transports.toCharArray();
		
		for (int i = 0; i < cs.length; i++)
		{
			switch (cs[i])
			{
				case 'f':
				{
					ts.add(new SJFifoPair());
					
					break;
				}
				case 's':
				{
					ts.add(new SJStreamTCP());
					
					break;
				}					
				case 'm':
				{			
					ts.add(new SJManualTCP());
					
					break;
				}					
				case 'h':
				{			
					ts.add(new SJHTTPServlet());
					
					break;
				}
			}
		}					
	}
}
