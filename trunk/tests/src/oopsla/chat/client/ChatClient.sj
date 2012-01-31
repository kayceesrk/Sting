//$ bin/sessionjc -sourcepath tests/src/oopsla/chat/common/';'tests/src/oopsla/chat/common/events/';'tests/src/oopsla/chat/client/';'tests/src/oopsla/chat/client/gui';'tests/src/oopsla/chat/client/net tests/src/oopsla/chat/client/ChatClient.sj -d tests/classes/
//($ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/client/ChatClient.sj -d tests/classes/)
//$ bin/sessionj -cp tests/classes/ oopsla.chat.client.ChatClient false 

package oopsla.chat.client;

//import java.awt.event.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;
import oopsla.chat.client.gui.*;
import oopsla.chat.client.net.*;

public class ChatClient //implements ActionListener
{
	public static final String DEFAULT_USER_NAME = "sj-user";
	public static final String DEFAULT_SERVER_ADDRESS = "localhost:8888";	
	
	private static final Integer STATUS_TAB_KEY = new Integer(0);
	private static final Integer GLOBAL_COMM_KEY = new Integer(1);
	
	private static final String STATUS_TAB_TITLE = "Status";
	private static final String GLOBAL_COMM_TITLE = "Global";
	
	private static final String WELCOME_MESSAGE = "Welcome to the SJ Chat Client 0.1a by Ray (20090305-0017).\n";		
	
	private static final Integer NOT_CONNECTED_USER_ID = new Integer(-1);
	private static final String NOT_CONNECTED_USER_NAME = null;
	
	protected static boolean debug;

	private UserList userList; // Aliased by the ChatClientGui (it's actually a GUI component).	
	
	private ChatClientGui gui;
	private ChatClientCommunicator comm;
	
	private Integer userId = NOT_CONNECTED_USER_ID;
	private String userName = NOT_CONNECTED_USER_NAME;
	
	public ChatClient()//boolean debug)
	{
		//this.debug = debug;
		
		this.userList = new UserList();
		
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
			
			gui.createTab(GLOBAL_COMM_KEY, GLOBAL_COMM_TITLE);
			
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
			System.err.println("[ChatClient] Shouldn't get in here: " + targetId + ", " + msg);
		}
	}
	
	public void handleEvent(ChatEvent e)
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
			else // Private message.
			{
				System.err.println("[ChatClient] Shouldn't get in here: " + targetId + ", " + msg);
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
		}
	}	
	
	public void disconnect() // This actually performs both communication and GUI routines - will be called by closing the GUI window even if not connected. 
	{			
		gui.closeTab(GLOBAL_COMM_KEY);
	
		gui.performDisconnect();
		
		if (comm.isConnected())
		{
			ChatClient.debugPrintln("[ChatClient] Sending user left event...");
			
			comm.sendEvent(new UserLeftEvent(getUserId()));
			
			userList.removeAll();
		}
		
		setUserId(NOT_CONNECTED_USER_ID);
		setUserName(NOT_CONNECTED_USER_NAME);
		
		if (comm.isConnected())
		{
			comm.disconnect();
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
				
		ChatClient.debug = debug;
		
		ChatClient c = new ChatClient();//debug);
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
}
