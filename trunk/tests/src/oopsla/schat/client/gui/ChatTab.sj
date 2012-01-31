//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/client/gui/ChatTab.sj -d tests/classes/

package oopsla.schat.client.gui;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import oopsla.schat.common.*;
import oopsla.schat.client.*;
 
public class ChatTab extends JPanel implements ActionListener
{	
	private static final String SEND_ACTION = "SEND_MESSAGE";
	
	private MainPane mainPane;
	
	private Integer key;
	
	private JTextArea view;
	private JScrollPane viewPane;
	
	private JTextField input;
	
	public ChatTab(MainPane mainPane, Integer key)
	{
		super(new BorderLayout());
		
		this.mainPane = mainPane;
		
		this.key = key; // The implicit communication target.
		
		this.view = new JTextArea();
		
		this.view.setEditable(false);
		this.view.setLineWrap(true);				
		
		viewPane = new JScrollPane(view);
		
		this.input = new JTextField();
		
		this.input.setActionCommand(SEND_ACTION);
		this.input.addActionListener(this);
		
		add(viewPane, BorderLayout.CENTER);
		add(input, BorderLayout.PAGE_END);
				
		addAnonymousListeners();
		
		//input.requestFocusInWindow(); // Doesn't seem to work.
		
		requestFocusOnInput();
	}
	
	private void requestFocusOnInput()
	{
		javax.swing.SwingUtilities.invokeLater // Needed when the application thread is updating the GUI (as opposed to the Swing thread(s)).
		(
			new Runnable()
			{
				public void run()
				{
					try
					{
						input.requestFocusInWindow(); // input doesn't need to be final? Because it's field? 
					}
					catch (Exception x)
					{
						System.err.print("[ChatTab] ");
						
						x.printStackTrace();
					}
				}
			}
		);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		String action = e.getActionCommand();
		
		if (action.equals(SEND_ACTION)) 
		{
			String msg = getAndClearInput();
			
			ChatClient.debugPrintln("[ChatTab] Message written for " + getTabKey() + ": " + msg);
			
			this.mainPane.getClient().sendMessage(getTabKey(), msg);
		}
		else
		{
			System.err.println("[ChatToolBar] Shouldn't get in here: " + action);
		}	
	}

	public void enableInput()
	{
		input.setEnabled(true); 
		
		requestFocusOnInput(); // FIXME: isn't working as intended.
	}
	
	public void disableInput()
	{
		input.setEnabled(false);
	}
	
	public void appendText(String text)
	{
		//synchronized (view)
		{
			/*boolean foo = false; 
			
			JScrollBar scrollBar = viewPane.getVerticalScrollBar();
			
			if (scrollBar.getValue() + scrollBar.getVisibleAmount() == scrollBar.getMaximum()) // Trying to only auto-scroll if currently at the bottom of the scroll pane. But not reliable, even with synchronization.
			{			
				//view.setCaretPosition(view.getDocument().getLength());
				
				//moveCaretToEnd();
				
				foo = true;
			}*/
			
			view.setText(view.getText() + text);
			
			//if (foo)
			{
				moveCaretToEnd();
			}
		}
	}
	
	private void moveCaretToEnd()
	{
		SwingUtilities.invokeLater
		(
			new Runnable() 
			{
				public void run() 
				{
					view.setCaretPosition(view.getDocument().getLength());
				}
			}
		);
	}
	
	private String getAndClearInput()
	{
		synchronized (input)
		{
			String m = input.getText();
			
			input.setText("");
			
			return m;
		}
	}

	public Integer getTabKey() // This isn't the tab index.
	{
		return key;
	}
	
	private void addAnonymousListeners()
	{
		addFocusListener
		(
			new FocusListener()
			{
				public void focusGained(FocusEvent e) 
				{
					//input.requestFocus();
					input.requestFocusInWindow();
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
					input.requestFocusInWindow(); 
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
	
	/*public JTextArea getViewArea()
	{
		return view;
	}
	
	public JTextField getInputArea()
	{
		return input;
	}*/
}
