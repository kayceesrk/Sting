//$ bin/sessionjc tests/src/runtime/selector/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.selector.server.Server false s a 8888

package runtime.selector.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

public class Server
{	
	public protocol p_body rec X [?(int).?(String).!<int>.#X] // Receive two numbers: first as an int, next as a String, and then return their product as an int.
	public protocol p_server sbegin.@(p_body)
		
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		//protocol p_selector { @(p_body), ?(int).?(String).!<int>.@(p_body), ?(String).!<int>.@(p_body) }
		protocol p_selector { @(p_body), ?(String).!<int>.@(p_body) }
		
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(setups, transports); // NB. this must currently come before selector creation. 
		
		final noalias SJSelector sel = SJRuntime.selectorFor(p_selector);
		
		HashMap map = new HashMap();
		
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
						when (@(p_body)) // Initially includes the "accept" event, and then just "normal" session recursion after that.
						{
							s.recursion(X)             // Selected session means...
							{
								int i = s.receiveInt();  // ...there is a message available for reading.
								
								System.out.println("Receive-1 (" + s.getPort() + "): " + i);
								
								map.put(new Integer(s.getPort()), new Integer(i));
								
								sel.registerInput(s);
							}								
						}
						when (?(String).!<int>.@(p_body))
						{
							String m = (String) s.receive();
							
							System.out.println("Receive-2 (" + s.getPort() + "): " + m);
							
							s.send(((Integer) map.get(new Integer(s.getPort()))).intValue() * Integer.parseInt(m));
							
							//s.recursion(X)
							{
								sel.registerInput(s); // Any problem registering the recursion as an input event?
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
		
		new Server().run(debug, setups, transports, port);
	}
}
