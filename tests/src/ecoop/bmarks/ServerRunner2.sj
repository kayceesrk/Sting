//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.ServerRunner2 false 8888 1 JT
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks.ServerRunner2 false 8888 1 SE

package ecoop.bmarks;

import ecoop.bmarks.java.thread.server.*;
import ecoop.bmarks.java.event.server.*;
import ecoop.bmarks.sj.server.thread.*;
import ecoop.bmarks.sj.server.event.*;

// Spawns a pair of Server and SignalClient.
public class ServerRunner2 
{
  public static void main(String args[]) 
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final int numClients = Integer.parseInt(args[2]); // NB: TimerClients count as two clients.
    final String server = args[3];
    
  	if (!(server.equals(SignalClient.JAVA_THREAD) || server.equals(SignalClient.JAVA_EVENT) || server.equals(SignalClient.SJ_THREAD) || server.equals(SignalClient.SJ_EVENT)))
		{
  		System.out.println("[ServerRunner2] Bad server flag: " + server);
  		
  		return;
		}
  	
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    			if (server.equals(SignalClient.JAVA_THREAD))
    			{
    				new ecoop.bmarks.java.thread.server.Server2(debug, port, numClients).run();
    			}  
    			else if (server.equals(SignalClient.JAVA_EVENT))
    			{
    				new ecoop.bmarks.java.event.server.Server2(debug, port, numClients).run();
    			}
    			else if (server.equals(SignalClient.SJ_THREAD))
    			{
    				new ecoop.bmarks.sj.server.thread.Server2(debug, port, numClients).run();
    			}
    			else if (server.equals(SignalClient.SJ_EVENT))
    			{
    				new ecoop.bmarks.sj.server.event.Server2(debug, port, numClients).run();
    			}
      		else
      		{
      			System.out.println("[ServerRunner2] Unrecognised flag: " + server);
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
    	    new SignalServer2(port, server).run(); // SignalServer sorts out port offset to avoid collision with Server.
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  }
}
