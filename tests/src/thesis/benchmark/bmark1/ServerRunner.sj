//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.ServerRunner false 8888 SJ
//$ bin/sessionj -cp tests/classes/ -j -Djava.rmi.server.codebase=file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-hg/tests/classes/ -j -Djava.security.policy=C:/cygwin/home/Raymond/code/java/eclipse/sessionj-hg/tests/src/thesis/benchmark/bmark1/rmi/security.policy thesis.benchmark.bmark1.ServerRunner false 8888 RMI 

package thesis.benchmark.bmark1;

import thesis.benchmark.Server;
import thesis.benchmark.SignalServer;

// Spawns a pair of Server and SignalServer.
public class ServerRunner 
{
	private static final String RMI = "RMI";
	private static final String SJ = "SJ";
	private static final String SOCKET_MANUAL = "SOCKm";
	private static final String SOCKET_STREAM = "SOCKs";	
	
  public static void main(String args[]) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final String flag = args[2];
  	
  	final Server server;
  	if (flag.equals(RMI))
		{
			server = new thesis.benchmark.bmark1.rmi.RMIServerImpl(debug); // Port not needed
		}
  	else if (flag.equals(SJ))
		{
			server = new thesis.benchmark.bmark1.sj.SJServer(debug, port);
		}
  	else if (flag.equals(SOCKET_MANUAL))
		{
			server = new thesis.benchmark.bmark1.socket.ManualServer(debug, port);
		}
  	else if (flag.equals(SOCKET_STREAM))
		{
			server = new thesis.benchmark.bmark1.socket.StreamServer(debug, port);
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
