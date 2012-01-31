//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/applet/gui/AppletUserList.sj -d tests/classes/

package oopsla.schat.applet.gui;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import oopsla.schat.common.*;
import oopsla.schat.applet.*;
import oopsla.schat.applet.gui.*;

public class AppletUserList extends UserList implements ListSelectionListener, MouseListener
{	
	private DefaultListModel users; // Vector<User> // An empty ListModel seems to mess up the size of the List.

	private ChatApplet applet;
	
	public AppletUserList()
	{
		super();
	}
	
	public AppletUserList(ChatAppplet applet)
	{
		super(new DefaultListModel());
		
		this.users = (DefaultListModel) getModel();
		
		this.applet = applet;
		
		setLayoutOrientation(JList.VERTICAL);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setVisibleRowCount(-1);		
		
		addListSelectionListener(this);
		addMouseListener(this);
	}
 
	public void mouseClicked(MouseEvent e) 
	{
    if (e.getClickCount() == 2)
    {    	
    	synchronized (users)
    	{
	    	int index = getSelectedIndex();	    
	    	
	    	applet.sendPrivateConversationRequest(((User) users.get(index)).getId());
    	}
    }
	}
}
