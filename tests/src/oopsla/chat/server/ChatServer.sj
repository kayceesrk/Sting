//$ bin/sessionjc -sourcepath tests/src/oopsla/chat/common/';'tests/src/oopsla/chat/common/events/';'tests/src/oopsla/chat/server/ tests/src/oopsla/chat/server/ChatServer.sj -d tests/classes/
//($ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/server/ChatServer.sj -d tests/classes/)
//$ bin/sessionj -cp tests/classes/ oopsla.chat.server.ChatServer true 8888 

package oopsla.chat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;

public class ChatServer
{	
	private ChatProtocols dummy;
	
	public static final Integer CHAT_SERVER_ID = new Integer(10);
	
	protected static final int USER_ID_START_VALUE = 101;
	
	protected static boolean debug;
	
	private int id = USER_ID_START_VALUE;
	
	private Map users = new HashMap(); // Map<User, ServerOutThread>
	
	public ChatServer(int port_s) throws Exception
	{ 
		final noalias SJServerSocket ss_s;
		
		try (ss_s)
		{
			ChatServer.debugPrintln("[ChatServer] Server starting...");
			
			ss_s = SJServerSocket.create(ChatProtocols.p_sc, port_s);

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
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port_s = Integer.parseInt(args[1]);
		
		ChatServer.debug = debug;
		
		new ChatServer(port_s);
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
	
	protected Integer getFreshId()
	{
		return new Integer(id++);
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
