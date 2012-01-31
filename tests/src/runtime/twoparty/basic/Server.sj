//$ bin/sessionjc tests/src/runtime/twoparty/basic/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.twoparty.basic.Server false d d 8888

package runtime.twoparty.basic;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

public class Server
{	
	public final noalias protocol p_server { sbegin.!<String> }
	
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			//ss = SJServerSocket.create(p_server, port, createSJSessionParameters(setups, transports));
			ss = SJServerSocket.create(p_server, port);
			
			while (true)
			{
				final noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
						
					System.out.println("Accepted connection from: " + s.getHostName() + ":" + s.getPort()); 
										
					//System.out.println("Received: " + (String) s.receive(1000));
					//System.out.println("Received: " + (String) s.receive());
								
					System.out.println("Current session type: " + s.currentSessionType());
					System.out.println("Remaining session type: " + s.remainingSessionType());
					
					s.send("Hello from Server!");
					
					System.out.println("Current session type: " + s.currentSessionType());
					System.out.println("Remaining session type: " + s.remainingSessionType());					
				}
				/*catch (Exception x)
				{
					x.printStackTrace();
				}*/
				finally 
				{
					
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
