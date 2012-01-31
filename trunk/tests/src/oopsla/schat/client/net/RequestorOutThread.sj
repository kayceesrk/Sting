//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/net/RequestorOutThread.sj -d tests/classes/

package oopsla.schat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.*;

public class RequestorOutThread extends Thread
{	
	//private ChatProtocols dummy;
	
	private final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	
	//private final noalias protocol p_ra_private { sbegin.@(ChatProtocols.p_private_event_out_stream) }
	private final noalias protocol p_ra_private { sbegin.@(p_private_event_out_stream) }
	
	private ChatClient client;
	
	private RequestorInThread rit;
	
	private SJPort port_r_private_out;
	
	private List events = new LinkedList();
	
	private boolean run = true;
	
	public RequestorOutThread(ChatClient client, RequestorInThread rit, SJPort port_r_private_out) 
	{ 
		this.client = client;
		this.rit = rit;		
		this.port_r_private_out = port_r_private_out;
	}
	
	public void run()
	{
		ChatClient.debugPrintln("[RequestorOutThread] Requestor out thread started.");
		
		final noalias SJServerSocket ss_r_private_out;
		
		try (ss_r_private_out)
		{
			ss_r_private_out = SJServerSocket.create(p_ra_private, port_r_private_out); 

			ChatClient.debugPrintln("[RequestorOutThread] Waiting for Client to connect...");
			
			noalias SJSocket s_ra_private_out;
			
			try (s_ra_private_out)
			{
				s_ra_private_out = ss_r_private_out.accept(); 
				
				ChatClient.debugPrintln("[RequestorOutThread] Client connected.");
				
				ss_r_private_out.getCloser().close();
				
				client.openPrivateConversation(rit.getTargetId(), this);
				
				s_ra_private_out.outwhile(run) 
				{
					ChatEvent e = nextEvent();
					
					ChatClient.debugPrintln("[RequestorOutThread] Sending event: " + e);
					
					s_ra_private_out.send(e);			
				}				
			}
			finally
			{
				rit.disconnect();
				
				ChatClient.debugPrintln("[RequestorOutThread] Requestor out thread stopped.");
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[RequestorOutThread] Shouldn't get in here: " + ise);
		}
		catch (SJIOException ioe)
		{
			ChatClient.debugPrintln("[RequestorOutThread] I/O error: " + ioe);
		}
		finally
		{

		}
	}
	 
	public void disconnect()
	{
		if (run)
		{
			ChatClient.debugPrintln("[RequestorOutThread] Told to disconnect...");
			
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
					ChatClient.debugPrintln("[RequestorOutThread] Creating quit event...");
					
					//throw new SJIOException("[ClientOutThread] Disconnecting.");
					
					return new QuitEvent(rit.getRequestorId());
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
}
