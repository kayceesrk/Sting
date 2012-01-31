//$ bin/sessionjc -cp tests/classes/ tests/src/simplechat/server/events/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ simplechat.server.events.Server 8888 

package simplechat.server.events;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import simplechat.*;
import simplechat.server.*;
import util.*;

public class Server   
{		
	public static final noalias protocol p_send { rec X[!<String>.#X] }
	// Should work the same way with ![!<String>]*.
	public static final noalias protocol p_receive { rec X[?(String).#X] }
	
	public static final noalias protocol p_serverToClient_body { ?{WRITE: @(p_receive), READ: @(p_send)} }
	public static final noalias protocol p_serverToClient { sbegin.@(p_serverToClient_body) }		
		
	public static final noalias protocol p_selector { @(p_serverToClient_body), @(p_send), @(p_receive) } // Session set type (needs syntax extension).
	
	private static final String transports = "d";

	private SJSessionParameters sparams;
	
	public Server()
	{
		this.sparams = TransportUtils.createSJSessionParameters(transports, transports);
	}
	
	private void run(int port) throws SJIOException, SJIncompatibleSessionException
	{
		final noalias SJSelector selector = SJSelector.create(p_selector); // Has type @(p_selector).

		// Currently, SJServerSockets must be final-noalias, so this must be modified.
		
		SJSelectorKey ssKey = null; // Keys can be used to organise external data specific to each socket or session, but cannot be used for any actual session operations - so not session typed. 
		
		using (noalias SJServerSocket = SJServerSocket.create(p_serverToClient, port, sparams))
		{
			ssKey = selector.registerAccept(ss); // Passing session typed entities to selector registration typed in the same way as regular session argument passing (and spawn, delegation, etc.).
			
			while (true)
			{
				using (noalias SJSocket s = selector.select(SJSelector.ACCEPT | SJSelector.RECEIVE))
				{
					// To avoid introducing even more extra syntax, select is made to return a SJSocket. So the server socket accept must be performed implicitly.
					typecase (s) // The type of s should be p_selector fitered by the specified actions, i.e. ACCEPT and RECEIVE. So the selected type will be a subtype of the original type (that's nice).
					{
						when (p_serverToClient_body):
						{
							setupSession(selector, s); // Passing session typed entities as arguments following existing rules for na-final (selector) and noalias (s) arguments.
						}
						when (p_receive): 
						{
							receiveAndForwardMessage(selector, s);
						}						
					}					
				}
			}
		}
	}
	
	private void setupSession(final noalias @(p_selector) selector, noalias @(p_serverToClient_body) s)
	{
		try (s)
		{
			s.inbranch()
			{
				case WRITE:
				{
					SJSelectorKey skey = selector.registerReceive(s); // Keys not used, but can be used to tag session-specific data and perhaps for session reflection.
				}
				case READ:
				{
					SJSelectorKey skey = selector.registerSend(s);
				}
			}
		}
		catch (SJIOException ioe)
		{
			
		}
	}
	
	private void receiveAndForwardMessage(final noalias @(p_selector) selector, noalias @(p_receive) s)
	{
		try (s)
		{
			String msg = (String) s.receive(); // Need to unroll @(p_receive) to ?(String).@(p_receive). Recursive unrolling not yet supported (we don't have full subtyping yet).
			
			sendToAll(selector, msg);
			
			selector.registerReceive(s);
		}
		catch (SJIOException ioe)
		{
			
		}		
	}
	
	private void sendToAll(final noalias @(p_selector) selector, String msg) throws SJIOException
	{
		noalias SJSocket[] sa; // It seems this extension to support session socket arrays will be the best way to support our needs here. 
		
		try (sa)
		{
			sa = selector.selectAll(SJSelector.SEND); // Selecting all sessions ready for send; original @(p_select) will be filtered accordingly.
			
			olivloop (sa : s) // Overload foreach for our session typing purposes (although Polyglot does not actually support foreach yet).
			{
				try (s)
				{
					typecase (s)
					{
						case @(p_send): 
						{
							s.send(msg); // Need to unroll @(p_send) to ?(String).@(p_send).
							
							selector.register(s, SJSelector.SEND);
						}
					}
				}
				catch (SJIOException ioe)
				{
					
				}
			}
		}
		catch (SJIOException ioe)
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		int port = Integer.parseInt(args[0]);
		
		Server serv = new Server();
		
		serv.run(port);
	}
}
