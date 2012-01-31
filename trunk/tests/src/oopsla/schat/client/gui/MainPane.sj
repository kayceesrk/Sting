//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/gui/MainPane.sj -d tests/classes/

package oopsla.schat.client.gui;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import oopsla.schat.common.*;
import oopsla.schat.client.*;

public class MainPane extends JTabbedPane
{		
	private ChatClient client;
	
	private Map tabs; // Map<Integer, ChatTab> 
	
	public MainPane(ChatClient client)
	{
		this.client = client;
		
		this.tabs = new HashMap();
		
		addAnonymousListeners();
	}
	
	/*protected void addStatusTab()
	{
		addChatTab(STATUS_TAB_KEY, STATUS_TAB_TITLE);		
		
		appendMessage(STATUS_TAB_KEY, WELCOME_MESSAGE);
	}*/
	
	public void appendMessage(Integer key, String msg)
	{
		ChatTab tab = getChatTab(key);
		
		synchronized (tab)
		{
			tab.appendText(msg + "\n");
		}
	}
	
	/*public String readInput(Integer key)
	{
		ChatTab tab = getChatTab(key);
		
		synchronized (tab)
		{
			return tab.getAndClearInput();
		}
	}*/
	
	public void addChatTab(Integer key, String title)
	{		
		ChatTab tab = new ChatTab(this, key);
		
		synchronized (tabs)
		{
			if (tabs.containsKey(key))
			{
				enableChatTab(key);
			}
			else
			{
				tabs.put(key, tab);
			
				addTab(title, tab);
				//setMnemonicAt(0, KeyEvent.VK_1);
			}
		}
	}
	
	private ChatTab getChatTab(Integer key)
	{
		synchronized (tabs)
		{
			return (ChatTab) tabs.get(key);
		}
	}

	public void enableChatTab(Integer key)
	{
		synchronized (tabs)
		{
			((ChatTab) tabs.get(key)).enableInput();
		}
	}
	
	public void disableChatTab(Integer key)
	{
		synchronized (tabs)
		{
			((ChatTab) tabs.get(key)).disableInput();
		}
	}
	
	public void removeChatTab(Integer key)
	{
		synchronized (tabs)
		{
			remove((ChatTab) tabs.remove(key));
		}
	}	
	
	protected ChatClient getClient()
	{
		return client;
	}
	
	private void addAnonymousListeners()
	{
		addFocusListener
		(
			new FocusListener() // Should allow focus changes to be chained through components.
			{
				public void focusGained(FocusEvent e) 
				{
					//((ChatTab) getSelectedComponent()).requestFocus();
					((ChatTab) getSelectedComponent()).requestFocusInWindow();	
				}
	
				public void focusLost(FocusEvent e) 
				{
	
				} 				
			}
		);
		
		addMouseListener
		( 
			new MouseListener()
			{
				public void mouseClicked(MouseEvent e) 
				{ 
					((ChatTab) getSelectedComponent()).requestFocusInWindow(); 
				}
				
				public void mouseEntered(MouseEvent e) 
				{ 
					
				}
				
				public void mouseExited(MouseEvent e) 
				{ 
					
				} 
				
				public void mousePressed(MouseEvent e) 
				{
					
				} 
				
				public void mouseReleased(MouseEvent e) 
				{
					
				}			
			}
		);
	}	
}
