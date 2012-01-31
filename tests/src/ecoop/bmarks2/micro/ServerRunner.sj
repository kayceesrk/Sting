//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.ServerRunner false 8888 JT
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks2.micro.ServerRunner false 8888 SE

package ecoop.bmarks2.micro;

// Spawns a pair of Server and SignalServer.
public class ServerRunner 
{
	public static final String JAVA_THREAD = "JT";
	public static final String JAVA_EVENT = "JE";
	public static final String SJ_THREAD = "ST";
	public static final String SJ_EVENT = "SE";
	
  public static void main(String args[]) 
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    //final int numClients = Integer.parseInt(args[2]); // NB: TimerClients count as two clients.
    final String flag = args[2];
    final int numWorkers = Integer.parseInt(args[3]);
    
    
  	if (!(flag.equals(JAVA_THREAD) || flag.equals(JAVA_EVENT) || flag.equals(SJ_THREAD) || flag.equals(SJ_EVENT)))
		{
  		System.out.println("[ServerRunner] Bad server flag: " + flag);
  		
  		return;
		}
  	
  	final Server server;
  	
  	if (flag.equals(JAVA_THREAD))
		{
			server = new ecoop.bmarks2.micro.java.thread.server.Server(debug, port);
		}  
		else if (flag.equals(JAVA_EVENT))
		{
			server = new ecoop.bmarks2.micro.java.event.server.Server(debug, port);
		}
		else if (flag.equals(SJ_THREAD))
		{
			server = new ecoop.bmarks2.micro.sj.thread.server.Server(debug, port);
		}
		else if (flag.equals(SJ_EVENT))
		{
			server = new ecoop.bmarks2.micro.sj.event.server.Server(debug, port);
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
    	    new SignalServer(debug, port, server).run(); // SignalServer sorts out port offset internally to avoid collision with Server.
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  
	Thread spinning = new Thread(new StartSpinningController(debug, port, numWorkers));
	spinning.start();
  }
}
