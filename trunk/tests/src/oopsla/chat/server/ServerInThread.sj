//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/server/ServerInThread.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.chat.server.ServerInThread 8888 8898

package oopsla.chat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;

public class ServerInThread extends SJThread
{	
	//private ChatProtocols dummy;
	
	public static final noalias protocol p_cs_in { cbegin.@(ChatProtocols.p_event_in_stream) }
	
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
			
			SJPort port_s_out = SJRuntime.reserveFreeSJPort();
			
			ChatServer.debugPrintln("[ServerInThread] Assigning " + userName + " (" + userId + ") should connect to: " + port_s_out);
			
			final noalias SJService c_cs_in = SJService.create(p_cs_in, s_sc_main.getLocalHostName(), port_s_out);
						
			this.sot = new ServerOutThread(this, port_s_out);
			
			ChatServer.debugPrintln("[ServerInThread] Starting server out thread...");
			
			this.sot.start();
			
			s_sc_main.copy(c_cs_in);
			
			sendTheOtherUsers();
			
			server.addUser(user, sot);
			
			server.propagateEvent(new UserJoinedEvent(userId, userName));
			
			s_sc_main.inwhile()
			{
				/*if (!run) // Just check here, since if while flag has been received, event message will be pretty much following directly. But this may not be completely correct. 
				{
					throw new SJIOException("[ServerInThread] Breaking out of main loop.");
				}*/
				
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
