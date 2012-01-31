//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/gui/ChatMenuBar.sj -d tests/classes/

package oopsla.schat.client.gui;

import java.util.*;
import javax.swing.*;

import oopsla.schat.common.*;

public class ChatMenuBar extends JMenuBar
{	
	private static final String FILE_MENU = "File";
	private static final String SESSION_MENU = "Session";
	private static final String WINDOW_MENU = "Window";
	
	private static final String FILE_CLOSE = "Close";
	
	public ChatMenuBar()
	{
		super();
		
		makeFileMenu();
		makeSessionMenu();
		makeWindowMenu();
	}
	
	private void makeFileMenu()
	{
		JMenu file = new JMenu(FILE_MENU);
				
		//file.setMnemonic(KeyEvent.VK_A);
		//file.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		
		/*JMenuItem fileClose = new JMenuItem(FILE_CLOSE);
		
		//fileClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		//fileClose.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		
		file.add(fileClose);*/
		
		add(file);
	}
	
	private void makeSessionMenu()
	{
		JMenu file = new JMenu(SESSION_MENU);
				
		//file.setMnemonic(KeyEvent.VK_A);
		//file.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
				
		add(file);
	}		
	
	private void makeWindowMenu()
	{
		JMenu file = new JMenu(WINDOW_MENU);
				
		//file.setMnemonic(KeyEvent.VK_A);
		//file.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		
		add(file);
	}		
}
