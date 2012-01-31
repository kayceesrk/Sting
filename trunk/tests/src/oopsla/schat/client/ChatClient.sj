//$ bin/sessionjc -sourcepath tests/src/util';'tests/src/oopsla/schat/common/';'tests/src/oopsla/schat/common/events/';'tests/src/oopsla/schat/client/';'tests/src/oopsla/schat/client/gui';'tests/src/oopsla/schat/client/net tests/src/oopsla/schat/client/ChatClient.sj -d tests/classes/
//($ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/ChatClient.sj -d tests/classes/)
//$ bin/sessionj -cp tests/classes/ oopsla.schat.client.ChatClient false d d

package oopsla.schat.client;

//import java.awt.event.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import util.*;

import oopsla.schat.common.*;
import oopsla.schat.common.events.*;
import oopsla.schat.client.gui.*;
import oopsla.schat.client.net.*;

public class ChatClient //implements ActionListener
{
	public static final String DEFAULT_USER_NAME = "sj-user";
	public static final String DEFAULT_SERVER_ADDRESS = "localhost:8888";	
	
	private static final Integer STATUS_TAB_KEY = new Integer(0);
	private static final Integer GLOBAL_COMM_KEY = new Integer(1);
	
	private static final String STATUS_TAB_TITLE = "Status";
	private static final String GLOBAL_COMM_TITLE = "Global";
	
	private static final String WELCOME_MESSAGE = "Welcome to the SJ Chat Client 0.1a by Ray (20090311).\n";		
	
	private static final Integer NOT_CONNECTED_USER_ID = new Integer(-1);
	private static final String NOT_CONNECTED_USER_NAME = null;
	
	protected static boolean debug;

	/*private String setups;
	private String transports;*/
	
	private SJSessionParameters params;
	
	private UserList userList; // Aliased by the ChatClientGui (it's actually a GUI component).	
	private Map privateConversations; // Map<User, Object> (either RequestorOutThread or AcceptorOutThread).
	
	private ChatClientGui gui;
	private ChatClientCommunicator comm;
	
	private Integer userId = NOT_CONNECTED_USER_ID;
	private String userName = NOT_CONNECTED_USER_NAME;
	
	public ChatClient(String setups, String transports)//boolean debug)
	{
		//this.debug = debug;
		
		/*this.setups = setups; // FIXME: allow these parameters to be configured from the GUI.
		this.transports = transports;*/
		
		this.params = TransportUtils.createSJSessionParameters(setups, transports);
		
		this.userList = new UserList(this);
		this.privateConversations = new HashMap();
		
		this.gui = new ChatClientGui(this, userList);
		this.comm = new ChatClientCommunicator(this);
		
		gui.show();
		
		gui.createTab(STATUS_TAB_KEY, STATUS_TAB_TITLE);
		gui.appendMessage(STATUS_TAB_KEY, WELCOME_MESSAGE);
	}
	
	public boolean connect(String host_s, int port_s, String userName) // Need synchronization?
	{
		try
		{
			ChatClient.debugPrintln("[ChatClient] Connecting to " + host_s + ":" + port_s);
			
			setUserId(comm.connect(host_s, port_s, userName));
			
			ChatClient.debugPrintln("[ChatClient] Connected with user ID: " + getUserId());
			
			printStatusMessage("Connected to " + host_s + ":" + port_s + " with user ID: " + getUserId());
			
			setUserName(userName);
			
			gui.createTab(GLOBAL_COMM_KEY, GLOBAL_COMM_TITLE); // FIXME: only want to reuse the old tab if reconnecting to the same server. 
			
			return true;
		}
		catch (SJIncompatibleSessionException ise)
		{
			reportError("Client and server protocols incompatible.", ise.toString());
		}
		catch (SJIOException ioe)
		{
			reportError("Connection failed.", ioe.toString());
		}
		
		return false;
	}
	
	public void sendMessage(Integer targetId, String msg)
	{
		Integer senderId = getUserId();
		
		ChatClient.debugPrintln("[ChatClient] Preparing a message for sending: " + senderId + ", " + targetId + ", " + msg);
		
		if (targetId.equals(STATUS_TAB_KEY))
		{
			gui.appendMessage(STATUS_TAB_KEY, formatStatusCommand(msg));
		}
		else if (targetId.equals(GLOBAL_COMM_KEY))
		{
			comm.sendEvent(new MessageEvent(senderId, targetId, msg));
		}
		else
		{
			synchronized (privateConversations)
			{
				if (privateConversations.size() == 0)
				{
					System.err.println("[ChatClient] Shouldn't get here.");
				}
				
				for (Iterator i = privateConversations.keySet().iterator(); i.hasNext(); )
				{
					User user = (User) i.next();
					
					if (user.getId().equals(targetId))
					{
						MessageEvent me = new MessageEvent(senderId, targetId, msg);
						
						Object ot = privateConversations.get(user);
						
						if (ot instanceof RequestorOutThread)
						{
							((RequestorOutThread) ot).queueEvent(me);
						}
						else //if (ot instanceof AcceptorOutThread)
						{
							((AcceptorOutThread) ot).queueEvent(me);
						}
						
						gui.appendMessage(targetId, formatMessage(senderId, msg));
						
						break;
					}
					
					if (!i.hasNext())
					{
						System.err.println("[ChatClient] Shouldn't get in here: " + targetId + ", " + msg);		
					}
				}
			}			
		}
	}
	
	public void sendPrivateConversationRequest(Integer targetId)
	{
		Integer requestorId = getUserId();
		
		if (targetId.equals(requestorId))
		{
			reportError("Invalid private conversation request.", "Cannot initiate a private conversation with yourself.");
		}
		else
		{
			ChatClient.debugPrintln("[ChatClient] Preparing a private conversation request: " + requestorId + ", " + targetId);
			
			comm.sendEvent(new PrivateConversationEvent(requestorId, targetId));
		}
	}
	
	public boolean handleEvent(ChatEvent e)
	{
		ChatClient.debugPrintln("[ChatClient] Handling event: " + e);
		
		if (e instanceof UserJoinedEvent)
		{
			UserJoinedEvent uje = (UserJoinedEvent) e;
			
			//gui.addUser(new User(uje.getUserId(), uje.getUserName()));
			userList.addUser(new User(uje.getUserId(), uje.getUserName()));
		}
		else if (e instanceof MessageEvent)
		{
			MessageEvent me = (MessageEvent) e;
			
			Integer senderId = me.getSenderId();
			Integer targetId = me.getTargetId();
			String msg = me.getMessage();
			
			if (targetId.equals(GLOBAL_COMM_KEY))
			{
				gui.appendMessage(GLOBAL_COMM_KEY, formatMessage(senderId, msg));
			}			
			else if (targetId.equals(getUserId())) // Private message.
			{
				gui.appendMessage(senderId, formatMessage(senderId, msg));
			}
			else
			{
				System.err.println("[ChatClient] Shouldn't get in here 3: " + targetId + ", " + msg);
				
				return false;
			}
		}
		else if (e instanceof PrivateConversationEvent)
		{
			PrivateConversationEvent pce = (PrivateConversationEvent) e;
						
			Integer requestorId = pce.getRequestorId();
			Integer targetId = pce.getTargetId();
			
			if (!targetId.equals(getUserId()))
			{
				System.err.println("[ChatClient] Shouldn't get in here: " + targetId);
				
				return false;
			}
			
			User requestor = userList.findUser(requestorId);
			
			synchronized (privateConversations)
			{
				if (privateConversations.keySet().contains(requestor))
				{
					ChatClient.debugPrintln("[ChatClient] Duplicate private conversation request rejected: " + requestor);
					
					return false;
				}			
			}
		}
		else if (e instanceof UserLeftEvent)
		{
			//gui.removeUser(((UserLeftEvent) e).getUserId());
			userList.removeUser(((UserLeftEvent) e).getUserId());
		}
		else if (e instanceof QuitEvent)
		{
			// This is a bit of a dummy event. Like a FIN ACK.
		}
		else
		{
			System.err.println("[ChatClient] Shouldn't get in here: " + e);
			
			return false;
		}
		
		return true;
	}	
	
	public void openPrivateConversation(Integer key, Object ot)
	{
		User user = userList.findUser(key);
		
		ChatClient.debugPrintln("[ChatClient] Opening private conversation request with: " + user);
		
		printStatusMessage("Private conversation opened with: " + user);
		
		gui.createTab(key, user.getName());
		
		synchronized (privateConversations)
		{
			privateConversations.put(user, ot);
		}
	}
	
	public void closePrivateConversation(User user)
	{
		synchronized (privateConversations)
		{
			Object ot = privateConversations.get(user);
			
			if (ot instanceof RequestorOutThread)
			{
				((RequestorOutThread) ot).disconnect();
			}
			else //if (ot instanceof AcceptorOutThread)
			{
				((AcceptorOutThread) ot).disconnect();
			}
			
			privateConversations.remove(user);
			
			//gui.closeTab(user.getId());
			gui.disableTab(user.getId());
		}
			
		printStatusMessage("Closed private conversation with: " + user);
	}
	
	public void closePrivateConversation(Integer key)
	{
		synchronized (privateConversations)
		{
			for (Iterator i = privateConversations.keySet().iterator(); i.hasNext(); )
			{
				User user = (User) i.next();
				
				if (user.getId().equals(key))
				{
					closePrivateConversation(user);
					
					break;
				}
				
				if (!i.hasNext())
				{
					System.err.println("[ChatClient] Shouldn't get in here: " + key);
				}
			}
		}
	}		
	
	public void disconnect() // This actually performs both communication and GUI routines - will be called by closing the GUI window even if not connected. 
	{			
		//gui.closeTab(GLOBAL_COMM_KEY);
		gui.disableTab(GLOBAL_COMM_KEY);
	
		gui.performDisconnect();
		
		if (comm.isConnected())
		{
			ChatClient.debugPrintln("[ChatClient] Sending user left event...");
			
			comm.sendEvent(new UserLeftEvent(getUserId()));
			
			synchronized (privateConversations)
			{
				for (Iterator i = privateConversations.keySet().iterator(); i.hasNext(); )
				{
					closePrivateConversation((User) i.next());
				}
			}
			
			userList.removeAll();
		}
		
		setUserId(NOT_CONNECTED_USER_ID);
		setUserName(NOT_CONNECTED_USER_NAME);
		
		if (comm.isConnected())
		{
			comm.disconnect(); // FIXME: currently, unsatisfied RequestorInThreads have not been cleaned up and can block system exit. 
		}
		
		printStatusMessage("Disconnected from server.");
	}
	
	private String formatStatusCommand(String msg)
	{
		return (Utils.makeTimeStamp() + " <User> " + msg); 
	}
	
	private String formatMessage(Integer senderId, String msg)
	{
		return (Utils.makeTimeStamp() + " <" + userList.findUser(senderId).getName() + "> " + msg); 
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
				
		String setups = args[1];
		String transports = args[2];
		
		ChatClient.debug = debug;
		
		ChatClient c = new ChatClient(setups, transports);//debug);
	}

	public void printStatusMessage(String msg)
	{
		gui.appendMessage(STATUS_TAB_KEY, Utils.makeTimeStamp() + " " + msg);
	}
	
	public void reportError(String title, String msg)
	{
		gui.showMessageDialog(title, msg);
	}
	
	public Integer getUserId()
	{
		return userId;
	}
	
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	private void setUserName(String userName)
	{
		this.userName = userName;
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
	
	/*public String getSetups()
	{
		return setups;
	}
	
	public String getTransports()
	{
		return transports;
	}*/
	
	public SJSessionParameters getSessionParameters()
	{
		return params;
	}
}
