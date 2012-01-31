//$ bin/sessionjc -cp tests/classes/ tests/src/custom/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ custom.client.Client false d d localhost 8888 

package custom.client;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.*;

import custom.*;
import custom.server.*;

public class Client
{		 
	private protocol p_client ^(Server.p_server)
	/*private protocol p_client 
	{
		cbegin.rec X [!{$1: ?(String).#X}]
		//cbegin.rec X [?(String).#X]
	}*/
	
	public void run(boolean debug, String setups, String transports, String server, int port) throws Exception
	{
		final noalias SJSocket s;	
			
		try (s)
		{
			//SJSessionParameters params = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, new MyFormatter());
			SJSessionParameters params = new SJSessionParameters(SJCompatibilityMode.CUSTOM, MyFormatter.class);
			
			s = SJService.create(p_client, server, port).request(params);
			
			int i = 0;
			
			s.recursion(X)
			{
				if (i++ < 3)
				{
					s.outbranch(L1)
					{
						System.out.println("Received: " + (String) s.receive());
						
						Thread.sleep(1000);
						
						s.recurse(X);
					}
				}
				else
				{
					s.outbranch(L2)
					{
						System.out.println("Received: " + (String) s.receive());
					}
				}
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
