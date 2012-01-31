//$ bin/sessionjc -cp tests/classes/ tests/src/runtime/selector/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.selector.client.Client false s a localhost 8888 

package runtime.selector.client;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import runtime.selector.server.*;

public class Client
{		
	private final noalias protocol p_client { ^(runtime.selector.server.Server.p_server) }
	
	public void run(boolean debug, String setups, String transports, String server, int port) throws Exception
	{
		final noalias SJSocket s;	
			
		try (s)
		{
			s = SJService.create(p_client, server, port).request(SJTransportUtils.createSJSessionParameters(setups, transports));
			
			//System.out.println("Current session type: " + s.currentSessionType());					
			//System.out.println("Remaining session type: " + s.remainingSessionType());
			
			int i = 1;
			
			s.recursion(X)
			{				
				s.send(i);
				
				Thread.sleep(1000);
				
				s.send(new Integer(i).toString());
				
				System.out.println("Received: " + s.receiveInt());
				
				Thread.sleep(1000);
				
				i++;
				
				s.recurse(X);
			}									
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
		
		String server = args[3];
		int port = Integer.parseInt(args[4]);
		
		new Client().run(debug, setups, transports, server, port);
	}
}
