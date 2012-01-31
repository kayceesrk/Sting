//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/net/AcceptorOutThread.sj -d tests/classes/

package oopsla.schat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.*;

public class AcceptorOutThread extends SJThread
{	
	//private ChatProtocols dummy;
	
	//private static final noalias protocol p_ar_private { cbegin.@(ChatProtocols.p_ar_private_main) } 
	
	private final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	private final noalias protocol p_private_event_in_stream { ^(p_private_event_out_stream) }
	
	private final noalias protocol p_ar_private_main { ?(cbegin.@(p_private_event_in_stream)).@(p_private_event_out_stream) } 
		
	private ChatClient client;
	
	private Integer acceptorId;
	private Integer requestorId;
	
	private AcceptorInThread ait;
	
	private List events = new LinkedList();
	
	private boolean run = true;
	
	public AcceptorOutThread(ChatClient client, Integer requestorId, Integer acceptorId) 
	{ 
		this.client = client;
		this.requestorId = requestorId;
		this.acceptorId = acceptorId;	
	}
	
	//public void srun(final noalias cbegin.@(ChatProtocols.p_ar_private_main) c_ar_private)
	public void srun(final noalias cbegin.@(p_ar_private_main) c_ar_private)
	{
		ChatClient.debugPrintln("[AcceptorOutThread] Acceptor out thread started.");
				
		final noalias SJSocket s_ar_private_main;
		
		try (s_ar_private_main)
		{
			s_ar_private_main = c_ar_private.request(client.getSessionParameters()); 
			
			ChatClient.debugPrintln("[AcceptorOutThread] Acceptor out thread connected .");			
			
			//final noalias SJService c_ar_private_in = (cbegin.@(ChatProtocols.p_private_event_in_stream)) s_ar_private_main.receive();
			final noalias SJService c_ar_private_in = (cbegin.@(p_private_event_in_stream)) s_ar_private_main.receive();
			
			ChatClient.debugPrintln("[AcceptorInThread] Starting acceptor in thread...");
			
			ait = (AcceptorInThread) c_ar_private_in.spawn(new AcceptorInThread(client, this));
			
			s_ar_private_main.outwhile(run)
			{
				ChatEvent e = nextEvent();
				
				ChatClient.debugPrintln("[AcceptorOutThread] Sending event: " + e);
				
				s_ar_private_main.send(e);
			}
		}
		/*catch (SJSetupException se) // No, already converted to SJIOException by the transport manager.
		{
			ChatClient.debugPrintln("[AcceptorOutThread] Transports incompatible: " + se);
		}*/
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[AcceptorOutThread] Shouldn't get in here: " + ise);
		}
		catch (SJIOException ioe)
		{
			client.reportError("Private conversation error.", "Problem communicating with (" + requestorId + "): " + ioe + ". This could be due to a private conversation request that could not be satisfied.");
			
			ChatClient.debugPrintln("[AcceptorOutThread] I/O error: " + ioe);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[AcceptorOutThread] Shouldn't get in here: " + cnfe);
		}
		finally
		{
			if (ait != null)
			{
				ait.disconnect();
			}
			
			ChatClient.debugPrintln("[AcceptorOutThread] Acceptor out thread stopped.");
		}
	}
	 
	public void disconnect()
	{
		if (run)
		{
			ChatClient.debugPrintln("[AcceptorOutThread] Told to disconnect...");			
			
			this.run = false;
			
			synchronized (events)
			{
				events.notify();
			}
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
					ChatClient.debugPrintln("[AcceptorOutThread] Creating quit event...");
					
					//throw new SJIOException("[ClientOutThread] Disconnecting.");
					
					return new QuitEvent(getAcceptorId());
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
	
	protected Integer getRequestorId()
	{
		return requestorId;
	}
	
	protected Integer getAcceptorId()
	{
		return acceptorId;
	}
}
