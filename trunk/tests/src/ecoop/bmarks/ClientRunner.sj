//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.ClientRunner false localhost 8888 1 100 JT

package ecoop.bmarks;

import ecoop.bmarks.java.thread.client.*;
import ecoop.bmarks.java.event.client.*;
import ecoop.bmarks.sj.client.*;

// Spawns LoadClients.
public class ClientRunner 
{
  public static void main(String [] args) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final String host = args[1];
    final int port = Integer.parseInt(args[2]);

    int loadClients = Integer.parseInt(args[3]);    
    final int messageSize = Integer.parseInt(args[4]);
    
  	final String server = args[5];
  	
  	if (!(server.equals(SignalClient.JAVA_THREAD) || server.equals(SignalClient.JAVA_EVENT) || server.equals(SignalClient.SJ_THREAD) || server.equals(SignalClient.SJ_EVENT)))
		{
  		System.out.println("[SignalServer] Bad server flag: " + server);
  		
  		return;
		}   
    
    int clientNum = 0;

    for (int i = 0; i < loadClients; i++)	
    {
      final int cn = clientNum++;
      
      new Thread() 
      {
        public void run() 
        {
        	try
        	{
        		if (server.equals(SignalClient.JAVA_THREAD))
        		{
        			new ecoop.bmarks.java.thread.client.LoadClient(debug, host, port, cn, messageSize).run();
        		}
        		else if (server.equals(SignalClient.JAVA_EVENT))
        		{
        			new ecoop.bmarks.java.event.client.LoadClient(debug, host, port, cn, messageSize).run();
        		}
        		else if (server.equals(SignalClient.SJ_THREAD) || server.equals(SignalClient.SJ_EVENT))
        		{
        			new ecoop.bmarks.sj.client.LoadClient(debug, host, port, cn, messageSize).run();
        		}
        		else
        		{
        			System.out.println("[ClientRunner] Unrecognised flag: " + server);
        			System.exit(0);
        		}
        	}
        	catch (Exception x)
        	{
        		throw new RuntimeException(x);
        	}
        }
      }.start();
      
      try
      {
      	Thread.sleep(100);
      }
      finally
      {
      	
      }
    }
  }
}
