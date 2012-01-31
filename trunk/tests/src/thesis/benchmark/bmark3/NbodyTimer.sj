package thesis.benchmark.bmark3;

import thesis.benchmark.Util;
import thesis.benchmark.bmark3.Common;

// Simplified from benchmark.TimerClient
abstract public class NbodyTimer 
{
	public static final String BODY = "BODY";
	public static final String INIT_AND_BODY = "INIT_AND_BODY";
	public static final String FULL = "FULL";	
	
	protected static final int WARMUP_SESSION_LENGTH = 50;
	private static final int ITERATION_DELAY = 100; // Millis between each iteration using the same Server and Client instances. need to give enough time to be sure Server GC is done (before we start the timer for session initiation)
	
	private boolean debug;
	private int iters;   // This means "inner repeats", i.e. the number of measurements to take per Server (JVM) instance
	private String flag; // Set whether we want to include initialisation/close or not
  
  //private long preInit;
  private long postInit;
  private long preClose;
  //private long postClose;
	
  private int state = 0;
  
  public NbodyTimer(boolean debug, int iters, String flag) 
  {
  	this.debug = debug;
  	this.iters = iters;
    this.flag = flag.toUpperCase();
  }

  // Does a GC step (so subclasses don't need to)
  public abstract void run() throws Exception;
  /*{
  	try
  	{
	  	run(true, false); // Dummy run: warms up the Client and Server JVMs
	  	
	  	Common.debugPrintln(debug, "[NbodyTimer] Finished dummy run, now taking measurements."); 
  		
  		for (int i = 0; i < iters; i++)
  		{		  	
		  	run(false, true);		    
		  	System.gc(); // The API seems to indicate that this call blocks until the GC is done		  	
		  	Thread.sleep(ITERATION_DELAY); // Maybe factor this out
  		}
  	}
  	finally
  	{
  		
  	}
  }*/
  
  //abstract public void run(boolean warmup, boolean timer) throws Exception; 
  
  public boolean isDebug()
  {
  	return debug;
  }
  
  public int getIterations()
  {
  	return iters;
  }
  
  public long startTimer()
  {
  	postInit = System.nanoTime();   	
  	state++; // Could also check state is 0, similarly for the other timer methods.  	
  	return postInit;
  }

  public long stopTimer()
  {
  	preClose = System.nanoTime();  	
  	state++;  	
  	return preClose;
  }
  
  public void resetTimer()
  {
  	state = 0;
  }
  
  public void printTimer()
  {
  	if (state != 2)
  	{
  		throw new RuntimeException("[NbodyTimer] Missing timer method, current state is: " + state);
  	}
  	
  	//long initialisation = postClose - preClose;
  	long body = preClose - postInit;
  	//long close = postClose - preClose;
  	
  	/*if (flag.equals(INIT_AND_BODY) || flag.equals(FULL))
  	{
  		System.out.println("[NbodyTimer] Initialisation: " + initialisation + " nanos");
 		}*/
  	
  	//if (flag.equals(BODY) || flag.equals(INIT_AND_BODY) || flag.equals(FULL))
		{
  		System.out.println("[NbodyTimer] Body: " + body + " nanos");
		}
  	
		/*if (flag.equals(FULL))
		{
			System.out.println("[NbodyTimer] Close: " + close + " nanos");
		}*/
  	
		if (!(flag.equals(BODY)/* || flag.equals(INIT_AND_BODY) || flag.equals(FULL)*/))
  	{
  		throw new RuntimeException("[NbodyTimer] Bad timer flag: " + flag);  		
  	}
  }
  
  protected void debugPrintln(String m)
  {
  	Common.debugPrintln(debug, m);
  }
}
