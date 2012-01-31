//$ bin/sessionjc -cp tests/classes/ tests/src/runtime/twoparty/basic/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.twoparty.basic.Client false d d localhost 8888 

package runtime.twoparty.basic;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

//import runtime.twoparty.basic.Server;

public class Client
{		
	private final noalias protocol p_client { ^(runtime.twoparty.basic.Server.p_server) }
	
	public void run(boolean debug, String setups, String transports, String server, int port) throws Exception
	{
		final noalias SJSocket s;	
			
		try (s)
		{
			//long start = System.nanoTime();
							
			//s = SJService.create(p_client, server, port).request(createSJSessionParameters(setups, transports));
			s = SJService.create(p_client, server, port).request();
			
			System.out.println("Current session type: " + s.currentSessionType());					
			System.out.println("Remaining session type: " + s.remainingSessionType());
			
			long start = System.nanoTime();
			
			//s.send("Hello from Client!");
			
			//System.out.println("Received: " + (String) s.receive(5000));
			System.out.println("Received: " + (String) s.receive());			
			
			long finish = System.nanoTime();
			
			System.out.println("Current session type: " + s.currentSessionType());
			System.out.println("Remaining session type: " + s.remainingSessionType());
			
			System.out.println("time = " + (finish - start) / 1000000 + " millis.");
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
