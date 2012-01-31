package aplas.bmarks2.micro;

import java.io.*;
import java.net.*;

import aplas.bmarks2.micro.*;

abstract public class TimerClient extends Client
{
	public static final String BODY = "BODY";
	public static final String INIT_AND_BODY = "INIT_AND_BODY";
	public static final String FULL = "FULL";
	
  private int sessionLength;
  private String flag; // Set whether we want to include initialisation/close or not.
	private int repeats; // This means "inner repeats", i.e. the number of measurements to take per Server instance.
  
  private long preInit;
  private long postInit;
  private long preClose;
  private long postClose;
	
  private int state = 0;
  
  public TimerClient(boolean debug, String host, int port, int cid, int serverMessageSize, int sessionLength, String flag, int repeats) 
  {
  	super(debug, host, port, cid, serverMessageSize);
  	
    this.sessionLength = sessionLength;
    this.flag = flag.toUpperCase();
  	this.repeats = repeats;
  }

  public int getSessionLength()
  {
  	return sessionLength;
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
  		throw new RuntimeException("[TimerClient] Missing timer method: " + state);
  	}
  	
  	long initialisation = postClose - preClose;
  	long body = preClose - postInit;
  	long close = postClose - preClose;
  	
  	if (flag.equals(BODY))
  	{
 	  	System.out.println("[TimerClient] Body: " + body + " nanos");
 		}
  	else if (flag.equals(INIT_AND_BODY))
		{
			System.out.println("[TimerClient] Initialisation: " + initialisation + " nanos");
			System.out.println("[TimerClient] Body: " + body + " nanos");
		}
  	else if (flag.equals(FULL))
		{
			System.out.println("[TimerClient] Initialisation: " + initialisation + " nanos");
			System.out.println("[TimerClient] Body: " + body + " nanos");
			System.out.println("[TimerClient] Close: " + close + " nanos");
		}
  	else
  	{
  		throw new RuntimeException("[TimerClient] Bad timer flag: " + flag);  		
  	}
  }
  
  public void run() throws Exception
  {
  	try
  	{
	  	run(false); // Dummy run for warm up.
	  	
	  	debugPrintln("[TimerClient] Finished dummy run, now taking measurements.");
  		
  		for (int i = 0; i < repeats; i++)
  		{		  	
		  	run(true);
		    
		  	Thread.sleep(50); // Maybe factor this out.
  		}
  	}
  	finally
  	{
	  	/*debugPrintln("[TimerClient] Sending KILL.");
	  	
	  	new SignalClient().sendSignal(host, port, MyObject.KILL);*/  		
  	}
  }
  
  abstract public void run(boolean timer) throws Exception;
}
