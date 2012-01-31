//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/client/net/ClientOutThread.sj -d tests/classes/

package oopsla.chat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;
import oopsla.chat.client.*;

public class ClientOutThread extends SJThread
{		
	//private ChatProtocols dummy; // Somehow not working, even thought it works for ChatClientCommunicator and ClientInThread...
	
	//private static final noalias protocol p_event_out_stream { @(ChatProtocols.p_event_out_stream) }
	private static final noalias protocol p_event_out_stream { ![!<ChatEvent>]* } // Duplicated from ChatClientCommunicator.
	
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
				
				s_cs_out.send(e);
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
