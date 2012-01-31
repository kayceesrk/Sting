//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/applet/net/ChatAppletCommunicator.sj -d tests/classes/

package oopsla.schat.applet.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.applet.*;

public class ChatAppletCommunicator extends ChatClientCommunicator
{	
	private ChatProtocols dummy; // Dummy reference, acting a bit like a static import...
	
	public static final noalias protocol p_private_event_out_stream { ![!<ChatEvent>]* }
	public static final noalias protocol p_private_event_in_stream { ^(p_private_event_out_stream) }
	
	public static final noalias protocol p_ar_private_main { ?(cbegin.@(p_private_event_in_stream)).@(p_private_event_out_stream) } // From request "Acceptor's" perspective (the Acceptor is the "Client").  
	
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
	
	private static final noalias protocol p_event_in_stream { ^(p_event_out_stream) } 
	
	private ChatApplet applet;
	
	private boolean connected;
	
	private ClientOutThread cot;
	private ClientInThread cit;
	
	public ChatAppletCommunicator(ChatApplet applet)
	{
		this.applet = applet;
		
		this.connected = false;
	}	
	
	public synchronized Integer connect(String host_s, int port_s, String userName) throws SJIncompatibleSessionException, SJIOException
	{
		if (connected)
		{
			System.err.println("[ChatAppletCommunicator] Illegal connect request, already connected: " + host_s + ":" + port_s + ", " + userName);
		}
		
		final noalias SJService c_cs_main = SJService.create(ChatProtocols.p_cs_chat, host_s, port_s);
		
		noalias SJSocket s_cs_main;
		
		try (s_cs_main)
		{
			s_cs_main = c_cs_main.request(); // FIXME: allow transports to be configured.
		
			ChatClient.debugPrintln("[ChatAppletCommunicator] Connected to server at " + host_s + ":" + port_s);
			
			s_cs_main.send(userName);
			
			Integer userId = new Integer(s_cs_main.receiveInt());
			
			ChatClient.debugPrintln("[ChatAppletCommunicator] Assigned user ID: " + userId);
			
			//final noalias SJService c_cs_in = (cbegin.@(ChatProtocols.p_event_in_stream)) s_cs_main.receive(); // Somehow works for chat, but not for schat, and even though it works for c_cs_main above...
			final noalias SJService c_cs_in = (cbegin.@(p_event_in_stream)) s_cs_main.receive();
						
			//finishSession(s_cs_main); // Compilation hack for debugging.
			
			ChatClient.debugPrintln("[ChatAppletCommunicator] Starting Client in/out threads...");
			
			this.connected = true; // After this point, exceptions will not automatically cause the session to be closed. So need an external close mechanism.
			
			cot = (ClientOutThread) s_cs_main.spawn(new ClientOutThread(applet, this));
			cit = (ClientInThread) c_cs_in.spawn(new ClientInThread(applet, this));
			
			return userId;
		}
		catch (ClassNotFoundException cnfe)
		{
			this.connected = false;
			
			throw new SJIOException(cnfe);
		}
		finally
		{
			
		}
	}
	
	public void sendEvent(ChatEvent e)
	{
		cot.queueEvent(e);
	}
	
	public synchronized void disconnect()
	{
		if (isConnected())
		{
			ChatClient.debugPrintln("[ChatAppletCommunicator] Disconnecting...");
			
			this.connected = false;
			
			cot.disconnect();
			cit.disconnect();
		}
	}
	
	public boolean isConnected()
	{
		return connected;
	}

	/*private void finishSession(noalias @(p_sevent_out_stream) s_cs_main) throws SJIOException, ClassNotFoundException
	{
		try (s_cs_main)
		{
			s_cs_main.outwhile(true) 
			{
				if (true)
				{
					s_cs_main.outbranch(USER_JOINED)
					{
						s_cs_main.send(null);
					}
				}
				else if (true)
				{
					s_cs_main.outbranch(MESSAGE)
					{
						s_cs_main.send(null);
					}
				}
				else if (true)
				{
					s_cs_main.outbranch(PRIVATE_CONVERSATION)
					{
						s_cs_main.inbranch()
						{
							case ACCEPT:
							{
								final noalias protocol p_private { cbegin.@(p_private_conversation) }
								
								final noalias SJService c_private_conversation = SJService.create(p_private, "localhost", 1234);
								
								s_cs_main.copy(c_private_conversation);
							}
							case REJECT:
							{
								
							}
						}
					}
				}
				else if (true)
				{
					s_cs_main.outbranch(USER_LEFT)
					{
						s_cs_main.send(null);
					}
				}
				else
				{
					s_cs_main.outbranch(QUIT)
					{
						s_cs_main.send(null);
					}
				}
			}
		}
		finally
		{
			
		}
	}*/
}
