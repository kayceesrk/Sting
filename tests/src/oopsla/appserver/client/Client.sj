//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/appserver/client/Client.sj -d tests/classes/ 
//$ appletviewer -J-Djava.security.policy=file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-cvs/tests/src/oopsla/appserver/client/security.policy.hzhl2 file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-cvs/tests/src/oopsla/appserver/client/client.html

//$ jar -cf appserv-client.jar java_cup/ oopsla/ polyglot/ ppg sessionj/ util/ 
//$ jarsigner -keystore c:/cygwin/home/Raymond/code/java/keystore -storepass storepass -keypass rhu2009 -signedjar appserv-client.signed.jar appserv-client.jar sj-keys

package oopsla.appserver.client;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import util.*;

import oopsla.appserver.frontend.Portal;

/**
 * FIXME: the security policy isn't working as intended - for some reason, the applet cannot read the jars directly from the sessionj-cvs/lib directory. Currently, the necessary jars are duplicated in tests/classes/lib. 
 */
public class Client extends JApplet implements ActionListener
{		
	public static final String DEBUG_PARAMETER = "DEBUG";
	public static final String SETUPS_PARAMETER = "SETUPS";
	public static final String TRANSPORTS_PARAMETER = "TRANSPORTS";
	public static final String PORTAL_HOST_PARAMETER = "PORTAL_HOST";
	public static final String PORTAL_PORT_PARAMETER = "PORTAL_PORT";
	public static final String EMAIL_IMAGE_PARAMETER = "EMAIL_IMAGE";
	public static final String CALENDAR_IMAGE_PARAMETER = "CALENDAR_IMAGE";
	public static final String WORD_PROCESSOR_IMAGE_PARAMETER = "WORD_PROCESSOR_IMAGE";
	
	private static final int APPLET_WIDTH = 600;
	private static final int APPLET_HEIGHT = 400;
	private static final int BUTTON_WIDTH = APPLET_WIDTH / 3;
	private static final int BUTTON_HEIGHT = 200;
	/*private static final int LABEL_WIDTH = BUTTON_WIDTH;
	private static final int LABEL_HEIGHT = 50;*/
	private static final int TEXT_AREA_WIDTH = APPLET_WIDTH; 
	//private static final int TEXT_AREA_HEIGHT = APPLET_HEIGHT - BUTTON_HEIGHT - LABEL_HEIGHT;
	private static final int TEXT_AREA_HEIGHT = APPLET_HEIGHT - BUTTON_HEIGHT;
	
	private static final String EMAIL_LABEL = "Web-mail Client";
	private static final String CALENDAR_LABEL = "Calendar";
	private static final String WORD_PROCESSOR_LABEL = "Word Processor";
	
	private static final String EMAIL_ACTION = "EMAIL";
	private static final String CALENDAR_ACTION = "CALENDAR";
	private static final String WORD_PROCESSOR_ACTION = "WORD_PROCESSOR";
	
	private final noalias protocol p_cp { ^(Portal.p_p) }
	
	private boolean debug = false; 
	
	private String setups = "d";
	private String transports = "d";
	
	private String host_p; // No default.
	private int port_p;
	
	private String emailImage;
	private String calendarImage;
	private String wordProcessorImage;
	
	private JPanel panel;
	private JButton emailButton;
	private JButton calendarButton;
	private JButton wordProcessorButton;
	private JTextArea textArea;
	
	public void init()
	{		
		parseParameters();

		try
		{
			SwingUtilities.invokeAndWait // Not invokeLater.
			(
				new Runnable() 
				{
					public void run()
					{
						assemble();
					}
				}
			);
		} 
		catch (Exception x) 
		{ 
	    System.err.println("[Client] GUI failed: " + x);
		}
	}
	
	public void start()
	{
		//textArea.setText("Portal applet started.");
		appendText("Portal applet started.");
	}
	
	public void stop()
	{
		appendText("Portal applet stopped.");
	}
	
	public void destroy()
	{
		//System.exit(0);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		String action = e.getActionCommand();
		
		if (action.equals(EMAIL_ACTION) || action.equals(CALENDAR_ACTION) || action.equals(WORD_PROCESSOR_ACTION)) 
		{
			appendText("Initiating action: " + action);
			
			try
			{
				run(debug, setups, transports, host_p, port_p, action);
			}
			catch (Exception x)
			{
				appendText("Failure: " + x.getMessage());
				
				x.printStackTrace();
			}
		}
		else
		{
			System.err.println("[Client] Shouldn't get in here: " + action);
		}	
	}
	
	public void run(boolean debug, String setups, String transports, String host_p, int port_p, String action) throws Exception
	{
		final noalias SJService c_cp = SJService.create(p_cp, host_p, port_p);
		
		final noalias SJSocket s_cp;	
		
		try (s_cp)
		{		
			appendText("Connecting back to Portal: " + host_p + ":" + port_p); // FIXME: for some reason, the text is not appearing "immediately".
			
			s_cp = c_cp.request(TransportUtils.createSJSessionParameters(setups, transports));

			appendText("Connected to Portal.");
			
			s_cp.send("Hello from Applet!");
			
			appendText("Received: " + (String) s_cp.receive());
			
			appendText("Selecting application: " + action);
			
			if (action.equals(EMAIL_ACTION))
			{				
				s_cp.outbranch(SERVER1)
				{				
					s_cp.send("Hello from Applet!");
					
					appendText("Received: " + (String) s_cp.receive());
				}
			}
			else if (action.equals(CALENDAR_ACTION))
			{				
				s_cp.outbranch(SERVER2)
				{				
					s_cp.send("Hello from Applet!");
					
					appendText("Received: " + (String) s_cp.receive());
				}
			}
			else //if (action.equals(WORD_PROCESSOR_ACTION))
			{				
				s_cp.outbranch(SERVER3)
				{				
					s_cp.send("Hello from Applet!");
					
					appendText("Received: " + (String) s_cp.receive());
				}
			}
			
			appendText("Done.");
		}
		finally
		{
			
		}
	}

	private void appendText(final String text)
	{
		synchronized (textArea)  
		{
			/*try // No, only the Swing thread will be calling here.
			{
				SwingUtilities.invokeAndWait // Or invokeLater?
				(
					new Runnable() 
					{
						public void run()
						{*/
							textArea.setText(textArea.getText() + "\n" + text);	// Should use append instead.									
							
							System.out.println(text);
							
							moveCaretToEnd();
							
							//textArea.repaint(); // Doesn't seem to do anything.
						/*}
					}
				);
			}
			catch (Exception x)
			{
				System.err.println("[Client] Hopefully, won't get in here: " + x);
			}*/
		}
	}
	
	private ImageIcon createImageIcon(String path) 
	{
		ImageIcon imageIcon = null;
		
		try
		{
			URL url = new URL(path);
			
			if (url != null) 
			{
				imageIcon = new ImageIcon(url); 
				
				if (imageIcon.getIconHeight() == -1)
				{
					System.err.println("[Chat] Couldn't load image file: " + path);
				}
			} 
			else
			{
				System.err.println("[Chat] Couldn't load image file: " + path);	
			}
		}
		catch (MalformedURLException mue)
		{
			System.err.println("[Chat] Couldn't load image file: " + path);
		}
				
		return imageIcon;
	}
	
	private void assemble()
	{
		Border blackLineBorder = BorderFactory.createLineBorder(Color.black);
		
		this.panel = new JPanel(new BorderLayout());
		/*this.panel = new JPanel(new GridBagLayout()); // This is less stable if the image files cannot be loaded.
		
		GridBagConstraints c = new GridBagConstraints();*/				

		JPanel buttonsAndLabels = new JPanel(new BorderLayout());
		
		ImageIcon emailIcon = createImageIcon(emailImage);
		ImageIcon calendarIcon = createImageIcon(calendarImage);
		ImageIcon wordProcessorIcon = createImageIcon(wordProcessorImage);
		
		this.emailButton = new JButton(EMAIL_LABEL, emailIcon);
		this.calendarButton = new JButton(CALENDAR_LABEL, calendarIcon);
		this.wordProcessorButton = new JButton(WORD_PROCESSOR_LABEL, wordProcessorIcon);

		Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
		
		emailButton.setHorizontalTextPosition(AbstractButton.CENTER);
		emailButton.setVerticalTextPosition(AbstractButton.BOTTOM);	
		emailButton.setPreferredSize(buttonSize);
		emailButton.setBorder(blackLineBorder);
		emailButton.setActionCommand(EMAIL_ACTION);
		emailButton.addActionListener(this);
		
		calendarButton.setHorizontalTextPosition(AbstractButton.CENTER);
		calendarButton.setVerticalTextPosition(AbstractButton.BOTTOM);	
		calendarButton.setPreferredSize(buttonSize);
		calendarButton.setBorder(blackLineBorder);
		calendarButton.setActionCommand(CALENDAR_ACTION);
		calendarButton.addActionListener(this);

		wordProcessorButton.setHorizontalTextPosition(AbstractButton.CENTER);
		wordProcessorButton.setVerticalTextPosition(AbstractButton.BOTTOM);	
		wordProcessorButton.setPreferredSize(buttonSize);
		wordProcessorButton.setBorder(blackLineBorder);
		wordProcessorButton.setActionCommand(WORD_PROCESSOR_ACTION);
		wordProcessorButton.addActionListener(this);
		
		/*JPanel labels = new JPanel(new BorderLayout());
		
		JLabel emailLabel = new JLabel(EMAIL_LABEL, SwingConstants.CENTER);
		JLabel calendarLabel = new JLabel(CALENDAR_LABEL, SwingConstants.CENTER);
		JLabel wordProcessorLabel = new JLabel(WORD_PROCESSOR_LABEL, SwingConstants.CENTER);
		
		Dimension labelSize = new Dimension(LABEL_WIDTH, LABEL_HEIGHT);
		
		emailLabel.setPreferredSize(labelSize);
		//emailLabel.setBorder(blackLineBorder);
		
		calendarLabel.setPreferredSize(labelSize);
		//calendarLabel.setBorder(blackLineBorder);
		
		wordProcessorLabel.setPreferredSize(labelSize);
		//wordProcessorLabel.setBorder(blackLineBorder);
		
		labels.add(emailLabel, BorderLayout.WEST);
		labels.add(calendarLabel, BorderLayout.CENTER);
		labels.add(wordProcessorLabel, BorderLayout.EAST);*/
		
		buttonsAndLabels.setPreferredSize(new Dimension(APPLET_WIDTH, BUTTON_HEIGHT));		
		//buttonsAndLabels.setBorder(blackLineBorder);
		
		buttonsAndLabels.add(emailButton, BorderLayout.WEST);
		buttonsAndLabels.add(calendarButton, BorderLayout.CENTER);
		buttonsAndLabels.add(wordProcessorButton, BorderLayout.EAST);
		//buttonsAndLabels.add(labels, BorderLayout.SOUTH);
		
		this.textArea = new JTextArea();
		
		//textArea.setEditable(false);
		textArea.setLineWrap(true);		
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		scrollPane.setPreferredSize(new Dimension(TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT));
		scrollPane.setBorder(blackLineBorder);
		
		panel.add(buttonsAndLabels, BorderLayout.CENTER);
		panel.add(scrollPane, BorderLayout.SOUTH);

		panel.setPreferredSize(new Dimension(APPLET_WIDTH, APPLET_HEIGHT));
		panel.setBorder(blackLineBorder);
				
		/*c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1; // FIXME: weighting values are a bit messed up.
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(buttonsAndLabels, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		//c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		panel.add(scrollPane, c);*/
				
		//panel.addWindowListener(this);
		//panel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // Just shutdowns the GUI frame, rest of the application closed from the window close event.  		
		
		//setContentPane(panel);
		add(panel);
	}
	
	private void parseParameters()
	{
		this.debug = Boolean.parseBoolean(getParameter(DEBUG_PARAMETER));
		
		this.setups = getParameter(SETUPS_PARAMETER);
		this.transports = getParameter(TRANSPORTS_PARAMETER);
		
		this.host_p = getParameter(PORTAL_HOST_PARAMETER);
		this.port_p = Integer.parseInt(getParameter(PORTAL_PORT_PARAMETER));
		
		this.emailImage = getParameter(EMAIL_IMAGE_PARAMETER);
		this.calendarImage = getParameter(CALENDAR_IMAGE_PARAMETER);
		this.wordProcessorImage = getParameter(WORD_PROCESSOR_IMAGE_PARAMETER);
	}
	
	private void moveCaretToEnd()
	{
		/*SwingUtilities.invokeLater // Not needed - currently, always invoked from a Swing thread.
		(
			new Runnable() 
			{
				public void run()*/ 
				{
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
			/*}
		);*/
	}
	
	public String[][] getParameterInfo() 
	{
    String[][] info = new String[][]
    {
    	{DEBUG_PARAMETER, "String", "Debug mode [true/false]."},
      {SETUPS_PARAMETER, "String", "Setup flags."},
      {TRANSPORTS_PARAMETER, "String", "Transport flags."},
      {PORTAL_HOST_PARAMETER, "String", "Portal host name."},
      {PORTAL_PORT_PARAMETER, "int", "Portal port."}
    };
    
    return info;
	}	
	
	public String getAppletInfo() 
	{
    return "SJ transport-independent Application Server applet example by Raymond Hu.";
}
}
