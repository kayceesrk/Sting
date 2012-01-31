//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/client/net/ClientInThread.sj -d tests/classes/

package oopsla.chat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;
import oopsla.chat.client.*;

public class ClientInThread extends SJThread
{	
	private ChatProtocols dummy; // Dummy reference, acting a bit like a static import...
	
	private ChatClient client;
	private ChatClientCommunicator comm;
	
	private boolean run = true; 
	
	public ClientInThread(ChatClient client, ChatClientCommunicator comm)
	{
		this.client = client;
		this.comm = comm;
	}	
	
	public void srun(final noalias cbegin.@(ChatProtocols.p_event_in_stream) c_cs_in)
	{
		final noalias SJSocket s_cs_in;
		
		try (s_cs_in)
		{
			ChatClient.debugPrintln("[ClientInThread] Client in thread started.");
			
			s_cs_in = c_cs_in.request();
			
			ChatClient.debugPrintln("[ClientInThread] Connected to Server out thread: " + s_cs_in.getPort());
			
			//s_cs_in.outwhile(run)
			s_cs_in.inwhile()
			{
				/*if (!run) // Not useful? Server should be disconnecting (or connection will be broken) so can rely on implicit IO error?
				{
					throw new Exception("[ClientInThread] Breaking out of main loop.");
				}*/
				
				client.handleEvent((ChatEvent) s_cs_in.receive());
			}
		}
		catch (Exception x)
		{
			client.reportError("Session terminated.", "Error communicating with the server.");
			
			ChatClient.debugPrintln("[ClientInThread] Exception: " + x);			
		}
		finally
		{
			if (run) // Should prevent this thread from incorrectly disconnecting (via comm) a subsequently spawned thread.
			{
				//comm.disconnect();
				client.disconnect(); 
			}			
			
			ChatClient.debugPrintln("[ClientInThread] Client in thread stopped.");
		}
	}
	
	protected void disconnect()
	{
		if (run)
		{
			ChatClient.debugPrintln("[ClientInThread] Told to disconnect...");
			
			run = false;
		}
	}	
}
