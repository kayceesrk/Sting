//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/client/gui/ChatClientGui.sj -d tests/classes/

package oopsla.chat.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import oopsla.chat.common.*;
import oopsla.chat.client.*;

public class ChatClientGui implements WindowListener
{		
	private static final String GUI_TITLE = "SJChat Client";
	
	private static final int MAIN_PANE_WIDTH = 600;
	private static final int USER_LIST_WIDTH = 100;
	private static final int APP_HEIGHT = 400;
		
	private static final int MESSAGE_DIALOG_MAX_LINE_LENGTH = 100;
	
	private ChatClient client;
	
	private JFrame topLevelFrame;	
	
	private MainPane mainPane;
	private ChatMenuBar menuBar;
	private ChatToolBar toolBar; 
	
	private UserList userList; 		
	private JScrollPane userListScroller; 
	
	public ChatClientGui(ChatClient client, UserList userList)
	{
		this.client = client;
		
		this.topLevelFrame = new JFrame(GUI_TITLE);
		
		this.mainPane = new MainPane(client);
		this.menuBar = new ChatMenuBar();		
		this.toolBar = new ChatToolBar(client, this);
		
		this.userList = userList;		
		this.userListScroller = new JScrollPane(userList);
	}

	public void showMessageDialog(String title, String msg)
	{
		String m = "";
		
		for (int len = msg.length(); len > MESSAGE_DIALOG_MAX_LINE_LENGTH; len = msg.length())
		{
			int index = msg.indexOf('\n');
			
			if (index >= 0 && index < MESSAGE_DIALOG_MAX_LINE_LENGTH)
			{
				index += 1;
				
				m += msg.substring(0, index);
			}
			else
			{
				index = MESSAGE_DIALOG_MAX_LINE_LENGTH;
				
				m += msg.substring(0, index) + "\n";
			}
						
			msg = msg.substring(index);
		}
		
		m += msg;
		
		JOptionPane.showMessageDialog(topLevelFrame, m, title, JOptionPane.ERROR_MESSAGE);
	}
	
	/*public void addUser(User user)
	{
		userList.addUser(user);
	}
	
	public void removeUser(Integer key)
	{
		userList.removeUser(key);
	}*/
	
	public void createTab(Integer key, String title)
	{
		synchronized (mainPane)
		{
			mainPane.addChatTab(key, title);
			mainPane.setSelectedIndex(mainPane.getTabCount() - 1); // Adding a new chat tab adds to the end of the tab list.
		}
	}
	
	public void closeTab(Integer key)
	{
		synchronized (mainPane)
		{
			mainPane.removeChatTab(key);
		}
	}
	
	public void appendMessage(Integer key, String msg)
	{
		mainPane.appendMessage(key, msg);
	}
	
	/*public String readInput(Integer key)
	{
		return mainPane.readInput(key);
	}*/
	
	private void assemble()
	{
		//mainPane.addStatusTab(); // Now done by the client. 		
	
		mainPane.setPreferredSize(new Dimension(MAIN_PANE_WIDTH, APP_HEIGHT));
		userList.setPreferredSize(new Dimension(USER_LIST_WIDTH, APP_HEIGHT));
		
		topLevelFrame.setJMenuBar(menuBar);

		topLevelFrame.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		topLevelFrame.getContentPane().add(mainPane, BorderLayout.CENTER);
		topLevelFrame.getContentPane().add(userListScroller, BorderLayout.EAST);

		topLevelFrame.addWindowListener(this);
		//topLevelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Uses System.exit, so bypasses finally statements.
		topLevelFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // Just shutdowns the GUI frame, rest of the application closed from the window close event.  
		
		mainPane.requestFocusInWindow();
		
		addAnonymousListeners();
		
		topLevelFrame.pack();
		topLevelFrame.setVisible(true);
	}	

	public void performDisconnect()
	{
		toolBar.performDisconnect(); // The only component (container) that needs cleaning up.
	}
	
	private void addAnonymousListeners()
	{
		topLevelFrame.addWindowFocusListener
		(
			new WindowFocusListener()
			{
				public void windowGainedFocus(WindowEvent e) 
				{
					//mainPane.requestFocus();
					mainPane.requestFocusInWindow();
				}
	
				public void windowLostFocus(WindowEvent e) 
				{
	
				} 				
			}
		);
	}
	
	public void windowClosing(WindowEvent e)
	{
		try
		{
			//toolBar.performDisconnect(); // Do the same thing as the tool bar disconnect button.
			
			client.disconnect();
		}
		catch (Exception x)
		{
			ChatClient.debugPrintln("[ChatClientGui] Exception: " + x.getMessage());
		}
	}
	
	public void show()
	{
		javax.swing.SwingUtilities.invokeLater // Needed when the application thread is updating the GUI (as opposed to the Swing thread(s)). 
		(
			new Runnable()
			{
				public void run()
				{
					try
					{
						assemble();
					}
					catch (Exception x)
					{
						System.err.print("[ChatClientGui] ");
						
						x.printStackTrace();
					}
				}
			}
		);
	}
	
	public void windowActivated(WindowEvent e)
	{
		
	}
  
	public void windowClosed(WindowEvent e)
	{
		
	}
	
	public void windowDeactivated(WindowEvent e)
	{
		
	}
	  
	public void windowDeiconified(WindowEvent e)
	{
		
	}
	  
	public void windowIconified(WindowEvent e)
	{
		
	}
	  
	public void windowOpened(WindowEvent e)
	{
		
	}	
}
