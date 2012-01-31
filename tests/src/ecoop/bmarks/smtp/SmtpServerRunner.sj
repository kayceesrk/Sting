//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.smtp.SmtpServerRunner false 2525 SMTPT 10
//$ bin/sessionj -Dsessionj.transports.transport=a -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks.smtp.SmtpServerRunner false 2525 SMTPE 10
//$ localhost 2525 1 100

package ecoop.bmarks.smtp;

import ecoop.bmarks.*;
import ecoop.bmarks.smtp.thread.*;
import ecoop.bmarks.smtp.event.*;

// Spawns a pair of Server and SignalClient.
public class SmtpServerRunner 
{
  public static void main(String args[]) 
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]); 
    final String server = args[2];
    final int numClients = Integer.parseInt(args[3]);
    
  	if (!(server.equals(SignalClient.SMTP_THREAD) || server.equals(SignalClient.SMTP_EVENT)))
		{
  		System.out.println("[SmtpServerRunner] Bad server flag: " + server);
  		
  		return;
		}
  	
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    			if (server.equals(SignalClient.SMTP_THREAD))
    			{
    				new ecoop.bmarks.smtp.thread.Server().run(debug, port, "s", numClients);
    			}  
    			else if (server.equals(SignalClient.SMTP_EVENT))
    			{
    				new ecoop.bmarks.smtp.event.Server().run(debug, port, "a");
    			}
      		else
      		{
      			System.out.println("[SmtpServerRunner] Unrecognised flag: " + server);
      			System.exit(0);
      		}
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}    		
    	}
    }.start();
    
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    	    new SmtpSignalServer(port, server).run(); // SignalServer sorts out port offset to avoid collision with Server.
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  }
}
