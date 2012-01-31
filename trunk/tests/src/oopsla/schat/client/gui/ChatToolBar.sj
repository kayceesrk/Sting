//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/gui/ChatToolBar.sj -d tests/classes/

package oopsla.schat.client.gui;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import oopsla.schat.common.*;
import oopsla.schat.client.*;

public class ChatToolBar extends JToolBar implements ActionListener
{	
	private static final String CONNECT_ACTION = "CONNECT";
	private static final String DISCONNECT_ACTION = "DISCONNECT";

	private static final String TOOLBAR_CONNECT = "Connect";
	private static final String TOOLBAR_DISCONNECT = "Disconnect"; // A bit redundant to have separate connect and disconnect buttons.
	private static final String TOOLBAR_USER_NAME = "Username: "; 
	private static final String TOOLBAR_SERVER_ADDRESS = "SJChat server: ";
	
	private static final int USER_NAME_FIELD_WIDTH = 10;
	private static final int SERVER_ADDRESS_FIELD_WIDTH = 30; // Using columns doesn't seem reliable for setting the component size.
	
	private ChatClient client;
	private ChatClientGui gui;
	
	private JButton connectButton;
	private JButton disconnectButton;
	
	private JTextField userNameField;
	private JTextField serverAddressField;
	
	public ChatToolBar(ChatClient client, ChatClientGui gui) // Maybe not good to couple this GUI component to the Client in this way.
	{
		super();
		
		this.client = client;
		this.gui = gui;
		
		setFloatable(false);
    setRollover(true);
    
    addToolBarItems();
    enterOfflineMode();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		String action = e.getActionCommand();
		
		if (action.equals(CONNECT_ACTION)) 
		{
			String userName = userNameField.getText().trim();
			
			if (userName.length() < 1)
			{
				gui.showMessageDialog("Invalid username.", "Username must contain at least one non-whitespace character, not: " + userName);
				
				return;
			}
			
			String[] serverAddress = serverAddressField.getText().trim().split(":");
			
			if (serverAddress.length != 2)
			{
				gui.showMessageDialog("Invalid server address.", "Server address should be of the format [host]:[port], not: " + serverAddressField.getText());
				
				return;
			}
			
			String host = serverAddress[0].trim();
			
			int port;
			
			try
			{
				port = Integer.parseInt(serverAddress[1].trim());						
			}
			catch (NumberFormatException nfe)
			{
				gui.showMessageDialog("Invalid server address.", "Bad port number: " + serverAddress[1]);
				
				return;
			}
			
			if (client.connect(host, port, userName))
			{
				enterOnlineMode();
			}
		} 
		else if (action.equals(DISCONNECT_ACTION))
		{
			//performDisconnect();
			
			client.disconnect();	
		}
		else
		{
			System.err.println("[ChatToolBar] Shouldn't get in here: " + action);
		}
	}
	
	protected void performDisconnect()
	{
		enterOfflineMode();	
	}
	
	public void enterOnlineMode()
	{
		connectButton.setEnabled(false);
		disconnectButton.setEnabled(true);
		userNameField.setEditable(false);
		serverAddressField.setEditable(false);
	}

	public void enterOfflineMode()
	{
		connectButton.setEnabled(true);
		disconnectButton.setEnabled(false);
		userNameField.setEditable(true);
		serverAddressField.setEditable(true);
	}
	
	private void addToolBarItems() 
	{
		connectButton = makeButton(CONNECT_ACTION, TOOLBAR_CONNECT);
    disconnectButton = makeButton(DISCONNECT_ACTION, TOOLBAR_DISCONNECT);
    
    Dimension buttonSize = new Dimension(disconnectButton.getPreferredSize()); 
    
    connectButton.setPreferredSize(buttonSize);
    disconnectButton.setPreferredSize(buttonSize); // Weird, it doesn't seem to naturally use it's default preferred size.
    
    connectButton.addActionListener(this);
    disconnectButton.addActionListener(this);
    
    add(connectButton);
    add(disconnectButton);
    
    addSeparator();

    JLabel userNameLabel = new JLabel(TOOLBAR_USER_NAME);    
    
    userNameField = new JTextField(ChatClient.DEFAULT_USER_NAME, USER_NAME_FIELD_WIDTH);
    
    add(userNameLabel);
    add(userNameField);
    
    addSeparator();
    
    JLabel serverAddressLabel = new JLabel(TOOLBAR_SERVER_ADDRESS);
    
    serverAddressField = new JTextField(ChatClient.DEFAULT_SERVER_ADDRESS, SERVER_ADDRESS_FIELD_WIDTH);

    serverAddressField.setActionCommand(CONNECT_ACTION);
    serverAddressField.addActionListener(this);
    
    add(serverAddressLabel);
    add(serverAddressField);
	}
	
	private static JButton makeButton(String actionCommand, /*String toolTipText,*/ String text) 
	{
    JButton button = new JButton();
    
    button.setActionCommand(actionCommand);
    button.setText(text);
    //button.setToolTipText(toolTipText);
    //button.addActionListener(this);

    return button;
	}	
}
