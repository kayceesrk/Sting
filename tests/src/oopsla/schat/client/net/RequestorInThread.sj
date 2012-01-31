//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/net/RequestorInThread.sj -d tests/classes/

package oopsla.schat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.*;

public class RequestorInThread extends Thread
{	
	//private ChatProtocols dummy;
	
	//private final noalias protocol p_ra_private { sbegin.^(ChatProtocols.p_ar_private_main) }
	
	private final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	private final noalias protocol p_private_event_in_stream { ^(p_private_event_out_stream) }
	
	private final noalias protocol p_ar_private_main { ?(cbegin.@(p_private_event_in_stream)).@(p_private_event_out_stream) } 
	
	private final noalias protocol p_ra_private { sbegin.^(p_ar_private_main) }
	
	private ChatClient client;

	private Integer requestorId;
	private Integer targetId;
	
	private SJPort port_r_private;
	
	private RequestorOutThread rot = null; 
	
	private boolean run = true;
	
	public RequestorInThread(ChatClient client, Integer requestorId, Integer targetId, SJPort port_r_private) 
	{ 
		this.client = client;
		this.requestorId = requestorId;
		this.targetId = targetId;
		this.port_r_private = port_r_private;
	}
	
	public void run()
	{
		ChatClient.debugPrintln("[RequestorInThread] Requestor in thread started.");
		
		final noalias SJServerSocket ss_r_private;
		
		try (ss_r_private)
		{
			ss_r_private = SJServerSocket.create(p_ra_private, port_r_private); 

			ChatClient.debugPrintln("[RequestorInThread] Waiting for Client to connect...");
			
			noalias SJSocket s_ra_private;
			
			try (s_ra_private)
			{
				s_ra_private = ss_r_private.accept(); // FIXME: need timeouts, in case e.g. transports are not compatible or user wants to close.
				
				ChatClient.debugPrintln("[RequestorInThread] Client connected.");
				
				ss_r_private.getCloser().close();
				
				SJPort port_r_private_out = SJRuntime.reserveFreeSJPort(client.getSessionParameters());
				
				//final noalias protocol p_ar_private_in { cbegin.@(ChatProtocols.p_private_event_in_stream) }
				final noalias protocol p_ar_private_in { cbegin.@(p_private_event_in_stream) }
				
				final noalias SJService c_ar_private_in = SJService.create(p_ar_private_in, s_ra_private.getLocalHostName(), port_r_private_out);
				
				rot = new RequestorOutThread(client, this, port_r_private_out);
				
				rot.start();
				
				s_ra_private.copy(c_ar_private_in);
				
				s_ra_private.inwhile() 
				{
					/*if (!run) 
					{
						throw new Exception("[RequestorInThread] Breaking out of main loop.");
					}*/					
					
					ChatEvent e = (ChatEvent) s_ra_private.receive();
					
					ChatClient.debugPrintln("[RequestorInThread] Handling event: " + e);
					
					if (e instanceof MessageEvent)
					{
						client.handleEvent(e);
					}					
					else if (e instanceof UserLeftEvent)
					{
						// FIXME: event should be sent if the private conversation is separately closed using a close button or something.
					}
					else if (e instanceof QuitEvent)
					{
						// A bit of a dummy event. Like a FIN ACK.
					}
					else
					{
						System.err.println("[RequestorInThread] Shouldn't get in here: " + e);
					}
				}				
			}
			finally
			{
				if (rot != null)
				{
					rot.disconnect();
				}
				
				client.closePrivateConversation(getTargetId());
				
				ChatClient.debugPrintln("[RequestorInThread] Requestor in thread stopped.");				
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[RequestorInThread] Shouldn't get in here: " + ise);
		}		
		catch (SJIOException ioe)
		{
			ChatClient.debugPrintln("[RequestorInThread] I/O error: " + ioe);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[RequestorInThread] Shouldn't get in here: " + cnfe);
		}		
		finally
		{

		}		
	}	
		
	public void disconnect()
	{
		if (run)
		{
			ChatClient.debugPrintln("[RequestorInThread] Told to disconnect...");			
			
			this.run = false;
		}
	}
	
	protected Integer getRequestorId()
	{
		return requestorId;
	}
	
	protected Integer getTargetId()
	{
		return targetId;
	}
}
