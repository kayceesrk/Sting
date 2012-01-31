//$ bin/sessionjc tests/src/runtime/selector/server/Server2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.selector.server.Server2 false s a 8888

package runtime.selector.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

public class Server2
{	
	public protocol p_body ?(int).rec X [?(int).#X] 
	public protocol p_server sbegin.@(p_body)
		
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		protocol p_selector { @(p_body) }
		
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(setups, transports); // NB. this must currently come before selector creation. 
		
		final noalias SJSelector sel = SJRuntime.selectorFor(p_selector);
		
		try (sel)
		{		
			noalias SJServerSocket ss;
			
			try (ss)
			{
				ss = SJServerSocket.create(p_server, port, params);
				
				sel.registerAccept(ss);
			}
			finally
			{
				
			}
			
			while (true)
			{
				noalias SJSocket s;
				
				try (s)
				{
					s = sel.select();
					
					typecase (s)
					{
						when (@(p_body))  // Both the first "accept" event and all subsequent iterations.
						{
							int i = s.receiveInt();  // Selected session means there is a message available for reading.
							
							System.out.println("Received (" + s.getPort() + "): " + i);
							
							s.recursion(X)
							{
								sel.registerInput(s);
							}								
						}										
					}
																			
					//System.out.println("Current session type: " + s.currentSessionType());
					//System.out.println("Remaining session type: " + s.remainingSessionType());						
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
		
		new Server2().run(debug, setups, transports, port);
	}
}
