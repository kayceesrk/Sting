//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/server/ServerInThread.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.schat.server.ServerInThread 8888 8898

package oopsla.schat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;

public class ServerInThread extends SJThread
{	
	//private ChatProtocols dummy;
	
	private static final noalias protocol p_cs_in { cbegin.@(ChatProtocols.p_event_in_stream) } // The dual to ChatProtocols.p_s_out.
	
	private ChatServer server;
	 
	private ServerOutThread sot = null; 
	
	private User user;
	
	private boolean run = true;
	
	public ServerInThread(ChatServer server) 
	{ 
		this.server = server;
	}
		
	public void srun(noalias @(ChatProtocols.p_sc_main) s_sc_main)
	//public void srun(noalias @(p_sc_main) s_sc_main)
	{
		System.err.println("[ServerInThread] Server in thread started.");
		
		try (s_sc_main)
		{
			String userName = (String) s_sc_main.receive();
			
			ChatServer.debugPrintln("[ServerInThread] User connected: " + userName);
			
			Integer userId = server.getFreshId();
			
			ChatServer.debugPrintln("[ServerInThread] Assigning " + userName + " user ID: " + userId);
			
			s_sc_main.send(userId.intValue());
			
			setUser(new User(userId, userName));
			
			SJPort port_s_out = SJRuntime.reserveFreeSJPort(server.getSessionParameters());
			
			ChatServer.debugPrintln("[ServerInThread] Assigning " + userName + " (" + userId + ") should connect to: " + port_s_out);
			
			final noalias SJService c_cs_in = SJService.create(p_cs_in, s_sc_main.getLocalHostName(), port_s_out);
						
			this.sot = new ServerOutThread(server, this, port_s_out);
			
			ChatServer.debugPrintln("[ServerInThread] Starting server out thread...");
			
			this.sot.start();
			
			s_sc_main.copy(c_cs_in);
			
			sendTheOtherUsers();
			
			server.addUser(user, sot);
			
			server.propagateEvent(new UserJoinedEvent(userId, userName));
			
			/*s_sc_main.inwhile()
			{
				/*if (!run) // Just check here, since if while flag has been received, event message will be pretty much following directly. But this may not be completely correct. 
				{
					throw new SJIOException("[ServerInThread] Breaking out of main loop.");
				}*
				
				ChatEvent e = (ChatEvent) s_sc_main.receive();
				
				ChatServer.debugPrintln("[ServerInThread] Handling event: " + e);
				
				if (e instanceof MessageEvent)
				{
					server.propagateEvent(e);
				}
				else if (e instanceof UserLeftEvent)
				{
					sot.disconnect();
					
					server.propagateEvent(e); // User left event not sent back to the leaving party.
					server.removeUser(getUser());
				}
				else if (e instanceof QuitEvent)
				{
					// A bit of a dummy event. Like a FIN ACK.
				}
				else
				{
					System.err.println("[ServerInThread] Shouldn't get in here: " + e);
				}
			}*/
			
			s_sc_main.inwhile()
			{
				/*if (!run) // Not useful? Server should be disconnecting (or connection will be broken) so can rely on implicit IO error?
				{
					throw new Exception("[ClientInThread] Breaking out of main loop.");
				}*/
				
				s_sc_main.inbranch()
				{
					case USER_JOINED:
					{
						UserJoinedEvent e = (UserJoinedEvent) s_sc_main.receive(); // Dummy.	
					}
					case MESSAGE:
					{
						MessageEvent me = (MessageEvent) s_sc_main.receive();
						
						ChatServer.debugPrintln("[ServerInThread] Handling event: " + me);
						
						server.propagateEvent(me);
					}
					case PRIVATE_CONVERSATION:
					{
						PrivateConversationEvent pce = (PrivateConversationEvent) s_sc_main.receive();
						
						server.sendEvent(server.findUser(pce.getTargetId()), pce);
						
						if (pce.accepted()) 
						{
							s_sc_main.outbranch(ACCEPT) 
							{
								final noalias SJService c_ar_private_conversation = (cbegin.@(ChatProtocols.p_ar_private_main)) s_sc_main.receive();
								
								SJServerIdentifier si = c_ar_private_conversation.getServerIdentifier();
								
								pce.setHostAddress(si.getHostName());
								pce.setPort(si.getPort());
							}
						}
						else
						{
							s_sc_main.outbranch(REJECT) 
							{
								
							}
						}
					}
					case USER_LEFT:
					{
						UserLeftEvent ule = (UserLeftEvent) s_sc_main.receive();
						
						ChatServer.debugPrintln("[ServerInThread] Handling event: " + ule);
												
						/*sot.disconnect(); // Now all done in finally (in case of failure).
						
						server.removeUser(getUser());
						server.propagateEvent(ule); // User left event not sent back to the leaving party. */		
					}
					case QUIT:
					{
						QuitEvent qe = (QuitEvent) s_sc_main.receive(); // A bit of a dummy event. Like a FIN ACK.
						
						ChatServer.debugPrintln("[ServerInThread] Handling event: " + qe);
					}
				}			
			}			
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[ServerInThread] Shouldn't get in here: " + cnfe);
		}
		catch (SJIOException ioe)
		{
			ChatServer.debugPrintln("[ServerInThread] I/O error: " + ioe);
		}
		finally
		{
			if (sot != null)
			{
				sot.disconnect();
			}
			
			User foo = getUser();
			
			if (foo != null && server.findUser(foo.getId()) != null)				
			{
				server.removeUser(getUser());
				server.propagateEvent(new UserLeftEvent(foo.getId()));				
			}
			
			ChatServer.debugPrintln("[ServerInThread] Server in thread stopped.");
		}		
	}	
	
	private void sendTheOtherUsers()
	{
		Set users = server.getUsers(); // Set<User>
		
		synchronized (users)
		{
			for (Iterator i = users.iterator(); i.hasNext(); )
			{
				User other = (User) i.next();
				
				sot.queueEvent(new UserJoinedEvent(other.getId(), other.getName()));
			}
		}
	}
	
	protected void disconnect()
	{
		ChatServer.debugPrintln("[ServerInThread] Told to disconnect...");
		
		if (run)
		{
			this.run = false;
		}
	}
	
	protected User getUser()
	{
		return user;
	}
	
	protected void setUser(User user)
	{
		this.user = user;
	}
}
