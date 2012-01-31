//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/net/ClientOutThread.sj -d tests/classes/

package oopsla.schat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.*;

public class ClientOutThread extends SJThread
{		
	//private ChatProtocols dummy; // Somehow not working, even thought it works for ChatClientCommunicator and ClientInThread...
	
	public static final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	public static final noalias protocol p_private_event_in_stream { ^(p_private_event_out_stream) }
	
	public static final noalias protocol p_ar_private_main { ?(cbegin.@(p_private_event_in_stream)).@(p_private_event_out_stream) } // From request "Acceptor's" perspective (the Acceptor is the "Client").  
	
	public static final noalias protocol p_ar_private { cbegin.@(p_ar_private_main) }
	
	public static final noalias protocol p_event_out_stream 
	{ 
		![
		  !{ 
		  	USER_JOINED: 
		  		!<UserJoinedEvent>
		  , MESSAGE:
		  		!<MessageEvent>
		  , PRIVATE_CONVERSATION:
		  		!<PrivateConversationEvent>.
		  		?{
		  			ACCEPT: 
		  				!<cbegin.@(p_ar_private_main)>
		  		,	REJECT:
		  		}
		  ,	USER_LEFT:
		  		!<UserLeftEvent>
		  , QUIT:
		  		!<QuitEvent>
		  }
		 ]*
	}
	
	private ChatClient client;
	private ChatClientCommunicator comm;
	
	private boolean run = true;
	
	private List events = new LinkedList();
	
	public ClientOutThread(ChatClient client, ChatClientCommunicator comm)
	{
		this.client = client;
		this.comm = comm;
	}	
	
	//public void srun(noalias @(ChatProtocols.p_event_out_stream) s_cs_out)	
	public void srun(noalias @(p_event_out_stream) s_cs_out)
	{
		try (s_cs_out)
		{
			ChatClient.debugPrintln("[ClientOutThread] Client out thread started.");		
			
			s_cs_out.outwhile(run) 
			{
				ChatEvent e = nextEvent();
				
				ChatClient.debugPrintln("[ClientOutThread] Sending: " + e);
				
				if (e instanceof UserJoinedEvent) // Never needed?
				{
					s_cs_out.outbranch(USER_JOINED)
					{
						s_cs_out.send((UserJoinedEvent) e);
					}
				}
				else if (e instanceof MessageEvent)
				{
					s_cs_out.outbranch(MESSAGE)
					{
						s_cs_out.send((MessageEvent) e);
					}
				}
				else if (e instanceof PrivateConversationEvent) // FIXME: some of this logic should not be done in here.
				{
					s_cs_out.outbranch(PRIVATE_CONVERSATION)
					{
						PrivateConversationEvent pce = (PrivateConversationEvent) e;
						
						s_cs_out.send(pce);
												
						s_cs_out.inbranch()
						{
							case ACCEPT:
							{
								SJPort port_r_private = SJRuntime.reserveFreeSJPort(client.getSessionParameters());
								
								final noalias SJService c_ar_private = SJService.create(p_ar_private, s_cs_out.getLocalHostName(), port_r_private);
								
								new RequestorInThread(client, pce.getRequestorId(), pce.getTargetId(), port_r_private).start();
								
								s_cs_out.copy(c_ar_private);																	
							}
							case REJECT:
							{
								client.reportError("Private conversation request rejected.", "Request rejected by: " + pce.getTargetId());
							}
						}
					}
				}
				else if (e instanceof UserLeftEvent)
				{
					s_cs_out.outbranch(USER_LEFT)
					{
						s_cs_out.send((UserLeftEvent) e);
					}
				}
				else //if (e instanceof QuitEvent)
				{
					s_cs_out.outbranch(QUIT)
					{
						s_cs_out.send((QuitEvent) e);
					}
				}
			}			
		}
		catch (Exception x)
		{
			//client.reportError("Session terminated.", "Error communicating with the server."); // Rely on ClientInThread. Reliable?
			
			ChatClient.debugPrintln("[ClientOutThread] Exception: " + x);				
		}
		finally
		{
			if (run) // Should prevent this thread from incorrectly disconnecting (via comm) a subsequently spawned thread. 
			{
				//comm.disconnect();
				client.disconnect(); 
			}	
			
			ChatClient.debugPrintln("[ClientOutThread] Client out thread stopped.");
		}
	}
	
	private ChatEvent nextEvent() throws SJIOException
	{
		synchronized (events)
		{
			while (events.isEmpty())
			{
				try
				{
					events.wait();
				}
				catch (InterruptedException ie) // Doesn't matter?
				{
					throw new SJIOException(ie);
				}
				
				if (!run)
				{
					ChatClient.debugPrintln("[ClientOutThread] Creating quit event...");
					
					//throw new SJIOException("[ClientOutThread] Disconnecting.");
					
					if (events.isEmpty())
					{
						return new QuitEvent(client.getUserId()); // A bit of a dummy event. Like a FIN ACK.
					}
					else // Try and get the user left event that should have been put here by the ChatClient. Doesn't matter if we don't send a quit event.
					{
						return (UserLeftEvent) events.get(events.size() - 1);
					}
				}
			}
			
			return (ChatEvent) events.remove(0);
		}
	}
	
	public void queueEvent(ChatEvent e)
	{
		synchronized (events)
		{
			events.add(e);
			events.notify();
		}
	}
	
	protected void disconnect()
	{		
		if (run)
		{
			ChatClient.debugPrintln("[ClientOutThread] Told to disconnect...");
			
			this.run = false;
			
			//this.interrupt(); 
			
			synchronized (events)
			{
				events.notify();
			}
		}
	}
}
