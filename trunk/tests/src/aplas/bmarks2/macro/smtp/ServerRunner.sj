//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.macro.smtp.ServerRunner false 8888 s ST
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks2.macro.smtp.ServerRunner false 8888 a SE

package ecoop.bmarks2.macro.smtp;

import sessionj.runtime.transport.tcp.*;

import ecoop.bmarks2.macro.smtp.sj.*;
import ecoop.bmarks2.micro.StartSpinningController;

// Mostly duplicated from the microbenchmark equivalent: spawns a pair of Server and SignalServer.
public class ServerRunner 
{
	public static final String SJ_THREAD = "ST";
	public static final String SJ_EVENT = "SE";
	
  public static void main(String args[]) 
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final String setups = args[2];
    final String flag = args[3];
    final int numWorkers = Integer.parseInt(args[4]);
    
  	if (!(flag.equals(SJ_THREAD) || flag.equals(SJ_EVENT)))
		{
  		System.out.println("[ServerRunner] Bad server flag: " + flag);
  		
  		return;
		}
  	
  	final Server server;
  	
  	if (flag.equals(SJ_THREAD))
		{
			server = new ecoop.bmarks2.macro.smtp.sj.thread.server.Server(debug, port, setups);
		}
		else if (flag.equals(SJ_EVENT))
		{
			server = new ecoop.bmarks2.macro.smtp.sj.event.server.Server(debug, port, setups);
		}
  	else
  	{
  		throw new RuntimeException("[ServerRunner] Bad server flag: " + flag);
  	}
  	
  	Thread spinning = new Thread(new StartSpinningController(debug, port, numWorkers));
    //spinning.setDaemon(true);
    spinning.start();
    
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
    	    new ecoop.bmarks2.micro.SignalServer(debug, port, server).run(); // SignalServer sorts out port offset internally to avoid collision with Server.
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
    
  }
}
