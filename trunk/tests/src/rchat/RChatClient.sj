//$ bin/sessionjc -cp tests/classes/ tests/src/rchat/RChatClient.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ rchat.RChatClient localhost 8888 ray 

package rchat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.util.*;

public class RChatClient implements KeyListener, WindowListener
{
	public final noalias protocol p_sendmsgs { sbegin.![!<String>]* }
	public final noalias protocol p_recmsgs { sbegin.![?(String)]* } 	

	public final noalias protocol p_events 
	{ 
		rec X[
			!{
				PING: 
					?[
						?{
							USER_JOINED: ?(int).?(String), 
							USER_LEFT: ?(int)
						}
					]*
					.#X, 
				BYE: 				
			}
		] 
	} 

	public final noalias protocol p_control 
	{ 
		cbegin
		.?(int).!<String>
		.!<^(p_sendmsgs)>		
		.!<^(p_recmsgs)>
		.@(p_events)
	}
	
	private static final int ROWS = 20;
	private static final int MESSAGE_AREA_COLUMNS = 60;
	private static final int USERS_AREA_COLUMNS = 10;
	
	private int cid;
	private String cname;

	private JTextArea messageArea = new JTextArea(ROWS, MESSAGE_AREA_COLUMNS);		
	private JTextArea usersArea = new JTextArea(ROWS, USERS_AREA_COLUMNS);	
	private JTextArea inputArea = null;

	private JScrollPane scrollPane = new JScrollPane(messageArea);	

	private	MessageLog log = new MessageLog();

	private boolean shutdown = false;

	public RChatClient(String server, int port, String cname) throws Exception
	{	
		this.cname = cname;

		final noalias SJService c_control = SJService.create(p_control, server, port);
		
		noalias SJSocket s_control;	
		
		try (s_control)
		{					
			s_control = c_control.request();		
					
			javax.swing.SwingUtilities.invokeLater
			(
				new Runnable()
				{
					public void run()
					{
						try
						{
							createAndShowGUI();
						}
						catch (Exception x)
						{
							x.printStackTrace();
						}
					}
				}
			);
			
			cid = s_control.receiveInt();
			
			s_control.send(cname);
			
			SJPort freep1 = SJRuntime.reserveFreeSJPort();
			SJPort freep2 = SJRuntime.reserveFreeSJPort();
			
			// Need multiparty? (Choreography?)			
			new SendMessagesThread(Integer.toString(cid), cid, freep1).start();
			new ReceiveMessagesThread(Integer.toString(cid), cid, freep2).start();
			
			final noalias protocol p_sendmsgs_dual { ^(p_sendmsgs) }
			final noalias protocol p_recmsgs_dual { ^(p_recmsgs) }
			
			final noalias SJService c_sendmsgs = SJService.create(p_sendmsgs_dual, server, freep1.getValue());
			final noalias SJService c_recmsgs = SJService.create(p_recmsgs_dual, server, freep2.getValue());
			
			s_control.copy(c_sendmsgs);
			s_control.copy(c_recmsgs);

			s_control.spawn(new ControlThread(cid));										
			
			System.out.println("Client started: " + cid);			
		}
		finally
		{

		}
	}

	private class ControlThread extends SJThread
	{
		private int cid;
		
		private Map users = new HashMap();		
		
		ControlThread(int cid)
		{
			this.cid = cid;
		}
		
		// rec X[!{PING: ?[?{USER_JOINED: ?(int).?(String), USER_LEFT: ?(int)}]*.#X, BYE: }]
		public void srun(noalias @(p_events) s_control) 
		{
			try (s_control)
			{
				s_control.recursion(X)
				{						
					if (!shutdown) // Shutdown (shared state) needed for communication between session thread and GUI (non-session) thread.
					{									
						s_control.outbranch(PING)
						{													
							s_control.inwhile()
							{
								s_control.inbranch()
								{
									case USER_JOINED:
									{
										users.put(new Integer(s_control.receiveInt()), (String) s_control.receive());
									}
									case USER_LEFT:
									{
										users.remove(new Integer(s_control.receiveInt()));
									}
								}
	
								updateUsersArea();																								
							}							
							
							Thread.sleep(1000);							
							
							s_control.recurse(X);
						}				
					}
					else
					{
						s_control.outbranch(BYE)
						{
							System.exit(0); // BYE message may not be delivered quickly enough?
						}
					}										
				}
			}
			catch (Exception x)
			{
				x.printStackTrace();
			}
		}

		public void updateUsersArea()
		{
			String m = "";

			for (Iterator i = users.keySet().iterator(); i.hasNext(); )
			{
				Integer v = (Integer) i.next();

				m = m + users.get(v) + "\n";
			}

			usersArea.setText(m);
		}
	}

	private class SendMessagesThread extends Thread
	{
		private int cid;
		private SJPort port;
		
		private int i = 0;		
		
		SendMessagesThread(String name, int cid, SJPort port)
		{
			super(name);
			
			this.cid = cid;
			this.port = port;
		}
	
		public void run() 
		{
			final noalias SJServerSocket ss;
		
			try (ss)
			{
				ss = SJServerSocketImpl.create(p_sendmsgs, port);
				
				final noalias SJSocket s;

				try (s)
				{
					s = ss.accept();
					
					ss.getCloser().close();

					s.outwhile(!shutdown)
					{
						String msg = log.getmsg(i++);

						System.out.println("(" + cid + ") sending: " + msg);

						s.send(msg);				
					}				
				}
				finally
				{

				}
			}
			catch (Exception x) 
			{

			}			
		}
	}

	private class ReceiveMessagesThread extends Thread
	{
		private int cid;
		private SJPort port;
	
		ReceiveMessagesThread(String name, int cid, SJPort port)
		{
			super(name);
		
			this.cid = cid;
			this.port = port;
		}
		
		public void run() 
		{
			final noalias SJServerSocket ss;
		
			try (ss)
			{
				ss = SJServerSocketImpl.create(p_recmsgs, port);
				
				final noalias SJSocket s;

				try (s)
				{
					s = ss.accept();
					
					ss.getCloser().close();		
					
					s.outwhile(!shutdown)
					{
						String msg = (String) s.receive();

						System.out.println("(" + cid + ") received: " + msg);				

						String existing = messageArea.getText();

						if (!existing.equals("")) 
						{
							msg = "\n" + msg; 
						}

						messageArea.setText(existing + msg);				

						adjustScrollPane();			
					}
				}
				finally
				{
				
				}
			}
			catch (Exception x) 
			{

			}								
		}
	}

	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyChar() == '\n')
		{
			String input = inputArea.getText();

			//oos.writeObject(id + ": " + input.substring(0, input.length() - 1));
			log.putmsg(input.substring(0, input.length() - 1));

			inputArea.setText("");
		}
	}

	private void createAndShowGUI()
	{
		JFrame frame = new JFrame("RChat Client");
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
		frame.addWindowListener(this);

		messageArea.setBorder(BorderFactory.createLineBorder(Color.black));
		messageArea.setEditable(false);		
		//messageArea.setRequestFocusEnabled(false);
		messageArea.setLineWrap(true);

		usersArea.setBorder(BorderFactory.createLineBorder(Color.black));
		usersArea.setEditable(false);

		inputArea = new JTextArea(1, MESSAGE_AREA_COLUMNS + USERS_AREA_COLUMNS);
		inputArea.setBorder(BorderFactory.createLineBorder(Color.black));
		inputArea.addKeyListener(this);

		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.getContentPane().add(usersArea, BorderLayout.EAST);
		frame.getContentPane().add(inputArea, BorderLayout.PAGE_END);

		frame.pack();
		frame.setVisible(true);

		frame.addFocusListener(
			new FocusListener()
			{
				public void focusGained(FocusEvent e) 
				{
					inputArea.requestFocus();
				}

				public void focusLost(FocusEvent e) 
				{

				} 				
			}
		);

		inputArea.requestFocusInWindow();						
	}	

	public void windowClosing(WindowEvent e)
	{
		try
		{
			shutdown = true;
		
			System.out.println("Shutting down.");
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception
	{
		new RChatClient(args[0], Integer.parseInt(args[1]), args[2]);
	}

	public void keyPressed(KeyEvent e) { }
	public void keyReleased(KeyEvent e) { }

	public void windowActivated(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }

	private void adjustScrollPane()
	{
		SwingUtilities.invokeLater
		(
			new Runnable() 
			{
				public void run() 
				{
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				}
			}
		);		
	}
}
