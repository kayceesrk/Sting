//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.server.ServerRunner false 8888 1 

package ecoop.bmarks.sj.server;

//@deprecated
// Spawns a pair of Server and SignalServer.
public class ServerRunner 
{
  public static void main(String args[]) 
  {
    /*if (args.length < 2) 
    {
      System.out.println("Usage: sessionj ServerRunner <port> <clientNum>");
      
      return;
    }*/

    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final int numClients = Integer.parseInt(args[2]);
    //final int messageSize = Integer.parseInt(args[3]);
    
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    			new Server(debug, port, numClients).run();
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
    	    //new SignalServer(port + SignalServer.SIGNAL_SERVER_PORT_OFFSET).run();
    	    new SignalServer(port).run();
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  }
}
