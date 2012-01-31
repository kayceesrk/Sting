//$ bin/sessionjc -cp tests/classes/ tests/src/sjapplet/applet/SJApplet.sj -d tests/classes/ 
//$ appletviewer -J-Djava.security.policy=file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-cvs/tests/src/sjapplet/applet/security.policy.hzhl2 file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-cvs/tests/src/sjapplet/applet/applet.html

package sjapplet.applet;

import java.util.*;
import javax.swing.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import util.*;

import sjapplet.server.Server;

/**
 * FIXME: the security policy isn't working as intended - for some reason, the applet cannot read the jars directly from the sessionj-cvs/lib directory. Currently, the necessary jars are duplicated in tests/classes/lib. 
 */
public class SJApplet extends JApplet
{		
	//private final noalias protocol p_client { cbegin.!<int> }
	private final noalias protocol p_client { ^(Server.p_server) }
	
	private boolean debug;
	
	private String setups;
	private String transports;
	
	private String server;
	private int port;
	
	private JTextArea tarea = new JTextArea("foo");
	
	public void init()
	{
		String args[] = new String[] { "false", "d", "d", "localhost", "8888" };
		
		this.debug = Boolean.parseBoolean(args[0]);
		
		this.setups = args[1];
		this.transports = args[2];
 
		this.server = args[3];
		this.port = Integer.parseInt(args[4]);
		
		add(tarea);
	}
	
	public void start()
	{
		try
		{
			run(debug, setups, transports, server, port);
		}
		catch (Exception x)
		{
			x.printStackTrace();
			
			tarea.setText("FAILED");
		}
	}
	
	public void stop()
	{
		
	}
	
	public void destroy()
	{
		
	}
	
	public void run(boolean debug, String setups, String transports, String server, int port) throws Exception
	{
		final noalias SJSocket s;	
		
		try (s)
		{
			s = SJService.create(p_client, server, port).request(TransportUtils.createSJSessionParameters(setups, transports));

			s.send("Hello from Applet!");
			
			//System.out.println("Received: " + (String) s.receive());
			tarea.setText("Received: " + (String) s.receive());
		}
		finally
		{
			
		}
	}
}
