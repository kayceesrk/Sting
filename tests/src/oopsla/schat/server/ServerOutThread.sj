//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/server/ServerOutThread.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.schat.server.ServerOutThread 8888 8898

package oopsla.schat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;

public class ServerOutThread extends Thread
{	
	private ChatProtocols dummy;
	
	private final noalias protocol p_ar_private { cbegin.@(ChatProtocols.p_ar_private_main) }
	
	private ChatServer server;
	
	private SJPort port_s_out;
	
	private ServerInThread sit;
	
	private List events = new LinkedList();
	
	private boolean run = true;
	
	public ServerOutThread(ChatServer server, ServerInThread sit, SJPort port_s_out) 
	{ 
		this.server = server;
		this.sit = sit;		
		this.port_s_out = port_s_out;
	}
	
	public void run()
	{
		System.err.println("[ServerOutThread] Server out thread started.");
		
		final noalias SJServerSocket ss_s;
		
		try (ss_s)
		{
			ss_s = SJServerSocket.create(ChatProtocols.p_s_out, port_s_out); // Although it's simpler conceptually to open a fresh server socket for each client, performance-wise, it's better to do this through a dedicated secondary server thread.

			ChatServer.debugPrintln("[ServerOutThread] Waiting for Client to connect...");
			
			noalias SJSocket s_sc_out;
			
			try (s_sc_out)
			{
				s_sc_out = ss_s.accept(); 
				
				ChatServer.debugPrintln("[ServerOutThread] Client connected.");
				
				ss_s.getCloser().close();
				
				/*s_sc_out.outwhile(run)
				{
					ChatEvent e = nextEvent();
					
					ChatServer.debugPrintln("[ServerOutThread] Sending event: " + e);
					
					s_sc_out.send(e);
				}*/
								
				s_sc_out.outwhile(run) 
				{
					ChatEvent e = nextEvent();
					
					ChatServer.debugPrintln("[ServerOutThread] Sending event: " + e);
					
					if (e instanceof UserJoinedEvent)
					{
						s_sc_out.outbranch(USER_JOINED)
						{
							s_sc_out.send((UserJoinedEvent) e);
						}
					}
					else if (e instanceof MessageEvent)
					{
						s_sc_out.outbranch(MESSAGE)
						{
							s_sc_out.send((MessageEvent) e);
						}
					}
					else if (e instanceof PrivateConversationEvent)
					{
						PrivateConversationEvent pce = (PrivateConversationEvent) e;
						
						s_sc_out.outbranch(PRIVATE_CONVERSATION)
						{
							s_sc_out.send(pce);
							
							s_sc_out.inbranch()
							{
								case ACCEPT:
								{
									pce.accept();
									
									String hostAddress = pce.getHostAddress();
									int port_private_conversation_in = pce.getPort();						
									
									//final noalias SJService c_ar_private = SJService.create(p_ar_private, server.findUser()..., port_private_conversation_in);
									final noalias SJService c_ar_private = SJService.create(p_ar_private, hostAddress, port_private_conversation_in);
									
									s_sc_out.copy(c_ar_private);								
								}
								case REJECT:
								{
									pce.reject();
								}
							}
						}
					}
					else if (e instanceof UserLeftEvent)
					{
						s_sc_out.outbranch(USER_LEFT)
						{
							s_sc_out.send((UserLeftEvent) e);
						}
					}
					else //if (e instanceof QuitEvent)
					{
						s_sc_out.outbranch(QUIT)
						{
							s_sc_out.send((QuitEvent) e);
						}
					}
				}				
			}
			finally
			{
				
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[ServerOutThread] Shouldn't get in here: " + ise);
		}
		catch (SJIOException ioe)
		{
			ChatServer.debugPrintln("[ServerOutThread] I/O error: " + ioe);
		}
		finally
		{
			sit.disconnect();
			
			ChatServer.debugPrintln("[ServerOutThread] Server out thread stopped.");
		}
	}
	 
	protected void disconnect()
	{
		ChatServer.debugPrintln("[ServerOutThread] Told to disconnect...");
		
		if (run)
		{
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
					ChatServer.debugPrintln("[ServerOutThread] Creating quit event...");
					
					//throw new SJIOException("[ClientOutThread] Disconnecting.");
					
					return new QuitEvent(ChatServer.CHAT_SERVER_ID);
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
