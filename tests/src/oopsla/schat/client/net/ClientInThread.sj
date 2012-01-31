//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/net/ClientInThread.sj -d tests/classes/

package oopsla.schat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.*;

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
	
	//public void srun(final noalias cbegin.@(ChatProtocols.p_event_in_stream) c_cs_in)
	public void srun(final noalias cbegin.@(ChatProtocols.p_event_in_stream) c_cs_in)
	{
		final noalias SJSocket s_cs_in;
		
		try (s_cs_in)
		{
			ChatClient.debugPrintln("[ClientInThread] Client in thread started.");
			
			s_cs_in = c_cs_in.request(client.getSessionParameters());
			
			ChatClient.debugPrintln("[ClientInThread] Connected to Server out thread: " + s_cs_in.getPort());
			
			s_cs_in.inwhile()
			{
				/*if (!run) // Not useful? Server should be disconnecting (or connection will be broken) so can rely on implicit IO error?
				{
					throw new Exception("[ClientInThread] Breaking out of main loop.");
				}*/
				
				s_cs_in.inbranch()
				{
					case USER_JOINED:
					{
						client.handleEvent((UserJoinedEvent) s_cs_in.receive());	
					}
					case MESSAGE:
					{
						client.handleEvent((MessageEvent) s_cs_in.receive());
					}
					case PRIVATE_CONVERSATION:
					{
						PrivateConversationEvent pce = (PrivateConversationEvent) s_cs_in.receive();
						
						if (client.handleEvent(pce))
						{
							s_cs_in.outbranch(ACCEPT) 
							{
								final noalias SJService c_ar_private = (cbegin.@(ChatProtocols.p_ar_private_main)) s_cs_in.receive();
								
								c_ar_private.spawn(new AcceptorOutThread(client, pce.getRequestorId(), pce.getTargetId()));
							}
						}
						else
						{
							s_cs_in.outbranch(REJECT) 
							{
								
							}
						}
					}
					case USER_LEFT:
					{
						client.handleEvent((UserLeftEvent) s_cs_in.receive());
					}
					case QUIT:
					{
						client.handleEvent((QuitEvent) s_cs_in.receive());
					}
				}			
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
