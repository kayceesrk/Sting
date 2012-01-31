//$ bin/sessionjc -cp tests/classes/ tests/src/serverclient/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ serverclient.client.Client false d d localhost 8888 

package serverclient.client;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

import serverclient.server.Server;

public class Client
{		
	//private final noalias protocol p_client { ^(Server.p_server) }
	private final noalias protocol p_client { cbegin.![?(int)]* }     // Testing session initiation compatibility check.
	
	public void run(boolean debug, String setups, String transports, String server, int port) throws Exception
	{
		final noalias SJSocket s1, s2;				
		try (s1, s2)
		{
			s1 = SJService.create(p_client, server, port).request(SJTransportUtils.createSJSessionParameters(setups, transports));
			s2 = SJService.create(p_client, server, port).request(SJTransportUtils.createSJSessionParameters(setups, transports));
			//s = SJService.create(p_client, server, port).request();
			
			long start = System.nanoTime();
			
			int i = 0;
			<s1, s2>.outwhile(i++ < 3)
			{							
				//Thread.sleep(10000);					

				/*System.out.println("Current session type: " + s.currentSessionType());
				System.out.println("Remaining session type: " + s.remainingSessionType());*/
				
				//<s1, s2>.send("Hello from Client!");

				//System.out.println("Received s1: " + (String) s1.receive());
				//System.out.println("Received s2: " + (String) s2.receive());
				
				System.out.println("Received s1: " + s1.receiveInt());
				System.out.println("Received s2: " + s2.receiveInt());
			}			
			
			long finish = System.nanoTime();			
			//System.out.println("time = " + (finish - start) / 1000000 + " millis.");							
		}
		finally
		{
			
		}
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String setups = args[1];
		String transports = args[2];
		
		//configureTransports(setups, transports);
 
		String server = args[3];
		int port = Integer.parseInt(args[4]);
		//int size = Integer.parseInt(args[5]);
		//int len = Integer.parseInt(args[6]);		
		
		new Client().run(debug, setups, transports, server, port);
	}

	/*private static SJSessionParameters createSJSessionParameters(String setups, String transports)
	{
		SJSessionParameters params;
		
		if (setups.contains("d") && transports.contains("d"))
		{
			params = new SJSessionParameters();
		}
		else
		{
			List ss = new LinkedList();
			List ts = new LinkedList();				
			
			parseTransportFlags(ss, setups);
			parseTransportFlags(ts, transports);
								
			params = new SJSessionParameters(ss, ts);
		}

		return params;
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
	}*/	
}
