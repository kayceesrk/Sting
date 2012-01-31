//$ bin/sessionj -Dsessionj.transports.negotiation=f -Dsessionj.transports.session=f -cp tests/classes/ thesis.benchmark.bmark2.SharedMemoryRunner false 8888 -1 1 2 1 BODY 3 NOALIAS 

package thesis.benchmark.bmark2;

import thesis.benchmark.Client;
import thesis.benchmark.SignalClient;
import thesis.benchmark.SignalServer;
import thesis.benchmark.Server;
import thesis.benchmark.bmark2.ServerRunner;

public class SharedMemoryRunner 
{
	private static final String NOALIAS = "NOALIAS";
	private static final String ORDINARY = "ORDINARY";
	
  public static void main(final String args[]) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final int cid = Integer.parseInt(args[2]);
    final int serverMessageSize = Integer.parseInt(args[3]);
    final int sessionLength = Integer.parseInt(args[4]);
    final int iters = Integer.parseInt(args[5]);
    final String timer = args[6];
    final int warmUp = Integer.parseInt(args[7]);
    //final int coolDown = Integer.parseInt(args[8]);
    final String flag = args[8];
    
  	final Server server;
  	final Client client;
  	if (flag.equals(NOALIAS))
		{
			client = new thesis.benchmark.bmark2.noaliaz.NoaliasClient(debug, "localhost", port, cid, serverMessageSize, sessionLength, iters, timer);
		}
  	else if (flag.equals(ORDINARY))
		{
			client = new thesis.benchmark.bmark2.ordinary.OrdinaryClient(debug, "localhost", port, cid, serverMessageSize, sessionLength, iters, timer);
		}
  	else
  	{
  		throw new RuntimeException("[SharedMemoryRunner] Bad flag: " + flag);
  	}
  	
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    			ServerRunner.main(new String[] { args[0], args[1], args[8] });
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}    		
    	}
    }.start();
    
    Thread.sleep(warmUp * 1000);
    
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    	    client.run();
    	    
    	    new SignalClient(false, "localhost", port).sendSignal(SignalServer.KILL);
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  }
}
