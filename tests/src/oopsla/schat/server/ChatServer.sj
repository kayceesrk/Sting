//$ bin/sessionjc -sourcepath tests/src/util/';'tests/src/oopsla/schat/common/';'tests/src/oopsla/schat/common/events/';'tests/src/oopsla/schat/server/ tests/src/oopsla/schat/server/ChatServer.sj -d tests/classes/
//($ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/server/ChatServer.sj -d tests/classes/)
//$ bin/sessionj -cp tests/classes/ oopsla.schat.server.ChatServer true d d 8888 

package oopsla.schat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;

//import util.*; // FIXME: for some reason, doesn't like specific classes (TransportUtils) to be specified.

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;

public class ChatServer
{	
	private ChatProtocols dummy;
	
	public static final Integer CHAT_SERVER_ID = new Integer(10);
	
	protected static final int USER_ID_START_VALUE = 101;
	
	protected static boolean debug;
	
	private SJSessionParameters params;
	
	private int id = USER_ID_START_VALUE;
	
	private Map users = new HashMap(); // Map<User, ServerOutThread>
	
	public ChatServer(String setups, String transports, int port_s) throws Exception
	{ 
		//this.params = SJTransportUtils.createSJSessionParameters(setups, transports);
		
		final noalias SJServerSocket ss_s;
		
		try (ss_s)
		{
			ChatServer.debugPrintln("[ChatServer] Server starting...");
			
			ss_s = SJServerSocket.create(ChatProtocols.p_sc_chat, port_s, null);
			//ss_s = SJServerSocket.create(ChatProtocols.p_sc_chat, port_s);

			ChatServer.debugPrintln("[ChatServer] Server listening on port: " + port_s);
			
			while (true)
			{
				noalias SJSocket s_sc_main;
				
				try (s_sc_main)
				{
					s_sc_main = ss_s.accept(); 
					
					ChatServer.debugPrintln("[ChatServer] Connection accepted from " + s_sc_main.getHostName() + ":" + s_sc_main.getPort());
					
					s_sc_main.spawn(new ServerInThread(this));
				}
				catch (SJIncompatibleSessionException ise)
				{
					ChatServer.debugPrintln("[ChatServer] Incompatible client: " + ise);
				}
				catch (SJIOException ioe)
				{
					ChatServer.debugPrintln("[ChatServer] I/O error: " + ioe);
				}
			}
		}
		finally
		{
			
		}
	}	
	
	protected void propagateEvent(ChatEvent e)
	{
		synchronized (users)
		{
			for (Iterator i = users.keySet().iterator(); i.hasNext(); )
			{
				((ServerOutThread) users.get((User) i.next())).queueEvent(e);
			}			
		}
	}

	protected void sendEvent(User user, ChatEvent e)
	{
		synchronized (users)
		{
			((ServerOutThread) users.get(user)).queueEvent(e);			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String setups = args[1];
		String transports = args[2];
		
		int port_s = Integer.parseInt(args[3]);
		
		ChatServer.debug = debug;
		
		new ChatServer(setups, transports, port_s);
	}
	
	//protected void registerOutThread(ServerOutThread sot)
	protected void addUser(User user, ServerOutThread sot)
	{
		//synchronized (outThreads)
		synchronized (users)
		{
			//outThreads.add(sot);
			users.put(user, sot);
		}
	}
	
	protected User findUser(Integer key)
	{
		synchronized (users)
		{
			for (Iterator i = getUsers().iterator(); i.hasNext(); )
			{
				User user = (User) i.next();
				
				if (user.getId().equals(key))
				{
					return user;
				}
			}
		}
		
		return null;
	}
	
	protected Set getUsers() // Set<User>
	{
		return users.keySet();
	}
	
	protected void removeUser(User user)
	{
		synchronized (users)
		{
			users.remove(user);
		}
	}
	
	protected Integer getFreshId()
	{
		return new Integer(id++);
	}

	public SJSessionParameters getSessionParameters()
	{
		return params;
	}
	
	public static void debugPrintln(String m)
	{
		if (getDebug())
		{
			System.out.println(m);
		}
	}
	
	public static boolean getDebug()
	{
		return debug;
	}
}
