//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.smtp.SmtpClientRunner false localhost 2525 1 100 

package ecoop.bmarks.smtp;

import ecoop.bmarks.smtp.client.*;

// Spawns DummyClients.
public class SmtpClientRunner 
{
  public static void main(String [] args) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final String host = args[1];
    final int port = Integer.parseInt(args[2]);

    int loadClients = Integer.parseInt(args[3]);    
    final int messageSize = Integer.parseInt(args[4]); 
    
    //int clientNum = 0;

    for (int i = 0; i < loadClients; i++)	
    {
      //final int cn = clientNum++;
      
      new Thread() 
      {
        public void run() 
        {
        	try
        	{
        		new ecoop.bmarks.smtp.client.DummyClient().run(debug, "sa", host, port, messageSize);        		
        	}
        	catch (Exception x)
        	{
        		throw new RuntimeException(x);
        	}
        }
      }.start();
      
      System.out.println(i);
      
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
