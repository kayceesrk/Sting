//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.ClientRunner false localhost 8888 1 100 

package ecoop.bmarks.sj.client;

//@deprecated
//Spawns LoadClients.
public class ClientRunner 
{
  public static void main(String [] args) 
  {
    /*if (args.length < 5) 
    {
      System.out.println("Usage: sessionj ClientRunner <host> <port> <LoadClients> <TimerClients> <session length>");
      
      return;
    }*/

    final boolean debug = Boolean.parseBoolean(args[0]);
    final String host = args[1];
    final int port = Integer.parseInt(args[2]);

    int loadClients = Integer.parseInt(args[3]);
    //int timerClients = Integer.parseInt(args[4]);
    
    final int messageSize = Integer.parseInt(args[4]);
    //final int iters = Integer.parseInt(args[5]);
    
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
        		new LoadClient(debug, host, port, cn, messageSize).run();
        	}
        	catch (Exception x)
        	{
        		throw new RuntimeException(x);
        	}
        }
      }.start();
    }

    /*for (; i < loadClients + timedClients; i++)	{
      final int j = i;
      new Thread() {
        public void run() {
          new Client(j, iterations).client();
        }
      }.start();
    }*/
  }
}
