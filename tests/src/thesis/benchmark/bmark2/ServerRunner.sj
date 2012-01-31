//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark2.ServerRunner false 8888 NOALIAS 

package thesis.benchmark.bmark2;

import thesis.benchmark.Server;
import thesis.benchmark.SignalServer;

// Spawns a pair of Server and SignalServer.
public class ServerRunner 
{
	private static final String NOALIAS = "NOALIAS";
	private static final String ORDINARY = "ORDINARY";
	
  public static void main(String args[]) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final String flag = args[2];
  	
  	final Server server;
  	if (flag.equals(NOALIAS))
		{
			server = new thesis.benchmark.bmark2.noaliaz.NoaliasServer(debug, port); 
		}
  	else if (flag.equals(ORDINARY))
		{
			server = new thesis.benchmark.bmark2.ordinary.OrdinaryServer(debug, port); 
		}
  	else
  	{
  		throw new RuntimeException("[ServerRunner] Bad server flag: " + flag);
  	}
  	
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    			server.run();
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
    	    new SignalServer(debug, port, server).run(); // SignalServer sorts out port offset internally to avoid collision with Server
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  }
}
