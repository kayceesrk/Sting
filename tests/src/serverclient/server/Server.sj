//$ bin/sessionjc tests/src/serverclient/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ serverclient.server.Server false d d 8888

package serverclient.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

public class Server
{	
	public final noalias protocol p_server { sbegin.?[!<String>]* }
	
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		final noalias SJServerSocket ss;		
		try (ss)
		{
			ss = SJServerSocket.create(p_server, port, SJTransportUtils.createSJSessionParameters(setups, transports));
			//ss = SJServerSocket.create(p_server, port);
			
			//while (true)
			{
				final noalias SJSocket s1, s2;				
				try (s1, s2)
				{
					s1 = ss.accept();
					s2 = ss.accept();
						
					//System.out.println("Accepted connection from: " + s.getHostName() + ":" + s.getPort());
					//System.out.println("Transport: " + s.getConnection().getTransportName());
						
					<s1, s2>.inwhile()
					{						
						//System.out.println("Received: " + (String) s.receive());								
						//Thread.sleep(1000);
								
						<s1, s2>.send("Hello from Server!");		
					}
				}
				finally { }
			}
		}
		finally { }
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);		
		String setups = args[1];
		String transports = args[2];
		int port = Integer.parseInt(args[3]);		
 		
		new Server().run(debug, setups, transports, port);
	}
}
