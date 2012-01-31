//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/net/AcceptorInThread.sj -d tests/classes/

package oopsla.schat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.*;

public class AcceptorInThread extends SJThread
{	
	private ChatProtocols dummy;
	
	//private static final noalias protocol p_private_event_in_stream { ^(ChatProtocols.p_private_event_out_stream) }
	
	private final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	private final noalias protocol p_private_event_in_stream { ^(p_private_event_out_stream) }
	
	//private static final noalias protocol p_ar_private_in { cbegin.@(p_private_event_in_stream) } 
	
	private ChatClient client;
	 
	private AcceptorOutThread aot = null; 
	
	private boolean run = true;
	
	public AcceptorInThread(ChatClient client, AcceptorOutThread aot) 
	{ 
		this.client = client;
		this.aot = aot;
	}
		
	//public void srun(final noalias @(ChatProtocols.p_sc_main) s_ar_private_main)
	public void srun(final noalias cbegin.@(p_private_event_in_stream) c_ar_private_in)
	{
		ChatClient.debugPrintln("[AcceptorInThread] Acceptor in thread started.");
		
		final noalias SJSocket s_ar_private_in;
		
		try (s_ar_private_in)
		{			
			s_ar_private_in = c_ar_private_in.request(client.getSessionParameters());
						
			client.openPrivateConversation(aot.getRequestorId(), aot);
			
			s_ar_private_in.inwhile()
			{
				/*if (!run) // Just check here, since if while flag has been received, event message will be pretty much following directly. But this may not be completely correct. 
				{
					throw new SJIOException("[AcceptorInThread] Breaking out of main loop.");
				}*/
				
				ChatEvent e = (ChatEvent) s_ar_private_in.receive();
				
				ChatClient.debugPrintln("[AcceptorInThread] Handling event: " + e);
				
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
					System.err.println("[AcceptorInThread] Shouldn't get in here: " + e);
				}
			}			
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[AcceptorInThread] Shouldn't get in here: " + ise);
		}		
		catch (SJIOException ioe)
		{
			ChatClient.debugPrintln("[AcceptorInThread] I/O error: " + ioe);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[AcceptorInThread] Shouldn't get in here: " + cnfe);
		}		
		finally
		{				
			if (aot != null)
			{
				aot.disconnect();
			}
			
			client.closePrivateConversation(aot.getRequestorId());
			
			ChatClient.debugPrintln("[AcceptorInThread] Acceptor in thread stopped.");
		}		
	}	
	
	public void disconnect()
	{
		if (run)
		{
			ChatClient.debugPrintln("[AcceptorInThread] Told to disconnect...");			
			
			this.run = false;
		}
	}
}
