//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark3.WorkerRunner false 6666 m FW o 4440 4441 
//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark3.WorkerRunner false 7777 m W o 4442 localhost 4440
//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark3.WorkerRunner false 8888 m LW o localhost 4441 localhost 4442 2 1 BODY

package thesis.benchmark.bmark3;

import thesis.benchmark.Killable;
import thesis.benchmark.SignalServer;

// Spawns a pair of [First/Last]Worker and SignalServer.
public class WorkerRunner 
{
	private static final String FIRST_WORKER = "FW";
	private static final String WORKER = "W";
	private static final String LAST_WORKER = "LW";	
	private static final String ORDINARY = "o";
	private static final String NOALIAS = "n";
	
  public static void main(String args[]) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0]);
    final int port = Integer.parseInt(args[1]);
    final String transport = args[2];
    final String party = args[3]; // "FW", "W", or "LW"
    final String flag = args[4];  // "n" or "o"
  	
    if (!(flag.equals(ORDINARY) || flag.equals(NOALIAS)))
  	{
  		throw new RuntimeException("[WorkerRunner] Bad flag: " + flag);
  	}
    
  	final Killable worker;
  	if (party.equals(FIRST_WORKER))
		{
  		int port_l = Integer.parseInt(args[5]);
  		int port_r = Integer.parseInt(args[6]);	  		
  		
  		worker = (flag.equals(ORDINARY)) 
  		       ? new thesis.benchmark.bmark3.ordinary.FirstWorker(debug, port, port_l, port_r) 
  		       : null;//new thesis.benchmark.bmark3.noalias.FirstWorker(debug);
		}
  	else if (party.equals(WORKER))
		{
  		int port_l = Integer.parseInt(args[5]);
  		String host_r = args[6];
  		int port_r = Integer.parseInt(args[7]);		  		
  		
  		worker = (flag.equals(ORDINARY)) 
  		       ? new thesis.benchmark.bmark3.ordinary.Worker(debug, port, port_l, host_r, port_r) 
  		       : null;//new thesis.benchmark.bmark3.noalias.LastWorker(debug);
		}
  	else if (party.equals(LAST_WORKER))
		{
  		String host_l = args[5];
  		int port_l = Integer.parseInt(args[6]);
  		String host_r = args[7];
  		int port_r = Integer.parseInt(args[8]);				
  		int steps = Integer.parseInt(args[9]);
  		int iters = Integer.parseInt(args[10]);
  		String timerFlag = args[11]; // e.g. BODY 		
  		
  		worker = (flag.equals(ORDINARY)) 
  		       ? new thesis.benchmark.bmark3.ordinary.LastWorker(debug, port, host_l, port_l, host_r, port_r, steps, iters, timerFlag) 
  		       : null;//new thesis.benchmark.bmark3.noalias.LastWorker(debug);
		}
  	else
  	{
  		throw new RuntimeException("[WorkerRunner] Bad worker flag: " + party);
  	}
  	
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    			worker.run();
    		}
    		catch (Exception x)
    		{
    			//throw new RuntimeException(x); // Should be the exception due to being killed
    		}    		
    	}
    }.start();
    
    new Thread()
    {
    	public void run()
    	{
    		try
    		{
    	    new SignalServer(debug, port, worker).run(); // SignalServer sorts out port offset internally to avoid collision with Server
    		}
    		catch (Exception x)
    		{
    			throw new RuntimeException(x);
    		}
    	}
    }.start();
  }
}
