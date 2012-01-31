//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/appserver/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.appserver.server.Server false mh mh 9911 webmail 

package oopsla.appserver.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import util.*;

public class Server
{		
	public static final noalias protocol p_sc { ?(String).!<String> }
	
	public final noalias protocol p_s { sbegin.?(@(p_sc)) }
	
	public Server()
	{
		
	}
	
	public void run(boolean debug, String setups, String transports, int port_s, String name) throws Exception
	{
		SJSessionParameters params = TransportUtils.createSJSessionParameters(setups, transports);
		
		final noalias SJServerSocket ss_s;
		
		try (ss_s)
		{
			ss_s = SJServerSocket.create(p_s, port_s, params);						
			
			while (true)
			{
				final noalias SJSocket s_sp;
				final noalias SJSocket s_sc;
				
				try (s_sp, s_sc)
				{
					s_sp = ss_s.accept();
						
					System.out.println("Accepted connection from Portal: " + s_sp.getHostName() + ":" + s_sp.getPort());
					
					s_sc = (@(p_sc)) s_sp.receive(params); 
					
					System.out.println("Accepted connection from Client: " + s_sc.getHostName() + ":" + s_sc.getPort()); 
					
					System.out.println("Received: " + (String) s_sc.receive());				
					
					s_sc.send("Hello from Server: " + name + "!");		
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
 
		int port_s = Integer.parseInt(args[3]);
		
		String name = args[4];
		
		new Server().run(debug, setups, transports, port_s, name);
	}	
}
