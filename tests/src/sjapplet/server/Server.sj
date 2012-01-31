//$ bin/sessionjc -cp tests/classes/ tests/src/sjapplet/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ sjapplet.server.Server false d d 8888

package sjapplet.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import util.*;

public class Server
{	
	public final noalias protocol p_server { sbegin.?(String).!<String> }
	
	public Server()
	{
		
	}
	
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocket.create(p_server, port, TransportUtils.createSJSessionParameters(setups, transports));						
			
			while (true)
			{
				final noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
						
					System.out.println("Accepted connection from: " + s.getHostName() + ":" + s.getPort()); 
					
					System.out.println("Received: " + (String) s.receive());				
					
					s.send("Hello from Server!");		
				}
				catch (Exception x)
				{
					x.printStackTrace();
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
 
		int port = Integer.parseInt(args[3]);
		
		new Server().run(debug, setups, transports, port);
	}	
}
