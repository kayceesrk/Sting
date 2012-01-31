//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/applet/gui/ChatAppletGui.sj -d tests/classes/

package oopsla.schat.applet.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import oopsla.schat.common.*;
import oopsla.schat.applet.*;
import oopsla.schat.client.gui.*;

public class ChatAppletGui extends ChatClientGui
{		
	private ChatApplet applet;
	
	private AppletUserList appletUserList;
	
	public ChatAppletGui(ChatApplet applet, AppletUserList appletUserList)
	{
		this.applet = applet;
		
		this.topLevelFrame = new JFrame(GUI_TITLE);
		
		this.mainPane = new MainPane(applet);
		this.menuBar = new ChatMenuBar();		
		this.toolBar = new ChatToolBar(applet, this);
		
		this.appletUserList = appletUserList;		
		this.userListScroller = new JScrollPane(appletUserList);
	}
	
	private void assemble()
	{
		//mainPane.addStatusTab(); // Now done by the applet. 		
	
		mainPane.setPreferredSize(new Dimension(MAIN_PANE_WIDTH, APP_HEIGHT));
		appletUserList.setPreferredSize(new Dimension(USER_LIST_WIDTH, APP_HEIGHT));
		
		applet.setJMenuBar(menuBar);

		applet.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		applet.getContentPane().add(mainPane, BorderLayout.CENTER);
		applet.getContentPane().add(userListScroller, BorderLayout.EAST);

		applet.addWindowListener(this);
		//applet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Uses System.exit, so bypasses finally statements.
		applet.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // Just shutdowns the GUI frame, rest of the application closed from the window close event.  
		
		mainPane.requestFocusInWindow();
		
		addAnonymousListeners();
		
		applet.pack();
		applet.setVisible(true);
	}	
	
	private void addAnonymousListeners()
	{
		applet.addWindowFocusListener
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
			
			applet.disconnect();
		}
		catch (Exception x)
		{
			ChatClient.debugPrintln("[ChatAppletGui] Exception: " + x.getMessage());
		}
	}
}
