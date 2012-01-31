//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/client/gui/UserList.sj -d tests/classes/

package oopsla.chat.client.gui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import oopsla.chat.common.*;

public class UserList extends JList implements ListSelectionListener
{	
	private DefaultListModel users; // Vector<User> // An empty ListModel seems to mess up the size of the List.
		
	public UserList()
	{
		super(new DefaultListModel());
		
		this.users = (DefaultListModel) getModel();
		
		setLayoutOrientation(JList.VERTICAL);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setVisibleRowCount(-1);		
		
		addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent e) 
	{
		if (e.getValueIsAdjusting() == false) 
		{
			if (getSelectedIndex() == -1) 
			{
			
			} 
			else 
			{
		
			}
		}
	}
	
	public void addUser(User user)
	{
		synchronized (users) // Does it need synchronization? Should be a Vector underneath.
		{	
			int i = 0;
			
			for (Enumeration e = users.elements(); e.hasMoreElements(); i++)
			{
				if (user.getName().compareTo(((User) e.nextElement()).getName()) < 0) // == 0 shouldn't be possible.
				{
					break;
				}
			}
			
			users.insertElementAt(user, i);
			
	    //ensureIndexIsVisible(i); // Seems to break this routine?
		}
	}
	
	public User findUser(Integer key)
	{
		synchronized (users)
		{
			for (Enumeration e = users.elements(); e.hasMoreElements(); )
			{
				User user = (User) e.nextElement();
				
				if (user.getId().equals(key)) 
				{
					return user;
				}
			}					
		}
		
		return null;
	}
	
	public void removeUser(Integer key)
	{
		synchronized (users)		
		{			
			users.removeElement(findUser(key));
		}
	}
	
	public void removeAll()
	{
		synchronized (users)		
		{			
			users.clear();
		}
	}
	
	/*public void removeUser(User user)
	{
		synchronized (users)		
		{
			users.removeElement(user);
		}
	}*/
}
