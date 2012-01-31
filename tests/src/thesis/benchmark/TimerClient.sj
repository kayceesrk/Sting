package thesis.benchmark;

import thesis.benchmark.Util;

abstract public class TimerClient implements Client
{
	public static final String BODY = "BODY";
	public static final String INIT_AND_BODY = "INIT_AND_BODY";
	public static final String FULL = "FULL";	
	
	protected static final int WARMUP_SESSION_LENGTH = 50; 
	private static final int ITERATION_DELAY = 100; // Millis between each iteration using the same Server and Client instances. need to give enough time to be sure Server GC is done (before we start the timer for session initiation)
	
	private boolean debug;
	private String host;
	private int port;
	int cid;	
	int serverMessageSize;
  private int sessionLength;
	private int iters;   // This means "inner repeats", i.e. the number of measurements to take per Server (JVM) instance
	private String flag; // Set whether we want to include initialisation/close or not
  
  private long preInit;
  private long postInit;
  private long preClose;
  private long postClose;
	
  private int state = 0;
  
  public TimerClient(boolean debug, String host, int port, int cid, int serverMessageSize, int sessionLength, int iters, String flag) 
  {
  	this.debug = debug;
  	this.host = host;
  	this.port = port;
  	this.cid = cid;
  	this.serverMessageSize = serverMessageSize;
    this.sessionLength = sessionLength;
  	this.iters = iters;
    this.flag = flag.toUpperCase();
  }

  // Does a GC step (so subclasses don't need to)
  public final void run() throws Exception
  {
  	try
  	{
	  	run(true, false); // Dummy run: warms up the Client and Server JVMs
	  	
	  	debugPrintln("[TimerClient] Finished dummy run, now taking measurements."); 
  		
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
  }
  
  abstract public void run(boolean warmup, boolean timer) throws Exception; 
  
  public boolean isDebug()
  {
  	return debug;
  }
  
  public String getHost()
  {
  	return host;
  }
  
  public int getPort()
  {
  	return port;
  }
  
  public int getClientId()
  {
  	return cid;
  }
  
  public int getServerMessageSize()
  {
  	return serverMessageSize;
  }
  
  public int getSessionLength()
  {
  	return sessionLength;
  }
  
  public int getIterations()
  {
  	return iters;
  }
  
  public long startTimer()
  {
  	preInit = System.nanoTime();   	
  	state++; // Could also check state is 0, similarly for the other timer methods.  	
  	return preInit;
  }
  
  public long initialised()
  {
  	postInit = System.nanoTime();   	
  	state++;  	
  	return postInit;
  }
  
  public long bodyDone()
  {
  	preClose = System.nanoTime();  	
  	state++;  	
  	return preClose;
  }

  public long stopTimer()
  {
  	postClose = System.nanoTime();  	
  	state++;  	
  	return postClose;
  }
  
  public void resetTimer()
  {
  	state = 0;
  }
  
  public void printTimer()
  {
  	if (state != 4)
  	{
  		throw new RuntimeException("[TimerClient] Missing timer method, current state is: " + state);
  	}
  	
  	long initialisation = postClose - preClose;
  	long body = preClose - postInit;
  	long close = postClose - preClose;
  	
  	if (flag.equals(INIT_AND_BODY) || flag.equals(FULL))
  	{
  		System.out.println("[TimerClient] Initialisation: " + initialisation + " nanos");
 		}
  	
  	//if (flag.equals(BODY) || flag.equals(INIT_AND_BODY) || flag.equals(FULL))
		{
  		System.out.println("[TimerClient] Body: " + body + " nanos");
		}
  	
		if (flag.equals(FULL))
		{
			System.out.println("[TimerClient] Close: " + close + " nanos");
		}
  	
		if (!(flag.equals(BODY) || flag.equals(INIT_AND_BODY) || flag.equals(FULL)))
  	{
  		throw new RuntimeException("[TimerClient] Bad timer flag: " + flag);  		
  	}
  }
  
  protected void debugPrintln(String m)
  {
  	Util.debugPrintln(debug, m);
  }
}
