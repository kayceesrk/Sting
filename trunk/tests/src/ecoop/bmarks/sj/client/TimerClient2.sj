//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.TimerClient2 false localhost 8888 -1 100 10 5
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks.sj.client.TimerClient2 false localhost 8888 -1 100 10 5

package ecoop.bmarks.sj.client;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.*;
//import ecoop.bmarks.sj.event.server.Server;

// This counts as two clients (from the Server's view), due to the dummy run.
// The difference with TimerClient is that we can run many Client sessions (not hardcodede to two).
public class TimerClient2 
{
	protocol pClient cbegin.rec X [!{REC: !<ClientMessage>.?(MyObject).#X, QUIT: }]
	//protocol pClient ^(Server.pServer)

	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;
  private int sessionLength;
  private int repeats;

  public TimerClient2(boolean debug, String host, int port, int clientNum, int messageSize, int sessionLength, int repeats) 
  {
  	TimerClient2.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
    this.sessionLength = sessionLength;
    this.repeats = repeats;
  }

  public void run() throws Exception
  {
  	try
  	{
  		for (int i = 0; i < repeats; i++)
  		{
  			debugPrintln("[TimerClient2] Run: " + (i + 1) + "/" + repeats);
  			
		  	run(false); // Dummy run for warm up.
		  	
		  	debugPrintln("[TimerClient2] Finished dummy run, now taking measurements.");
		  	
		  	run(true);
  		}
  	}
  	finally
  	{
	  	debugPrintln("[TimerClient2] Sending KILL.");
	  	
	  	new SignalClient().sendSignal(host, port, MyObject.KILL);  		
  	}
  }

  public void run(boolean time) throws Exception
  {
  	//SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "sa");
  	
	  final noalias SJService c = SJService.create(pClient, host, port);
		
	  final noalias SJSocket s;
	  	  
	  try (s) 
	  {
	    //s = serv.request(params);
	  	s = c.request();
	
	    long start = System.nanoTime();
	    
	    MyObject mo;
	     
	    int iters = 0;
	    
	    s.recursion(X) 
	    {
	      if (iters < sessionLength) 
	      {
	        s.outbranch(REC) 
	        {
	        	s.send(new ClientMessage(clientNum, Integer.toString(iters), messageSize));
	          
	          mo = (MyObject) s.receive();            
	          
	          debugPrintln("[TimerClient2 " + clientNum + "] Received: " + mo);
	
	          if (debug)
	          {
	          	Thread.sleep(1000);
	          }
	          
	          s.recurse(X);
	        }
	      }
	      else 
	      {
	        s.outbranch(QUIT) 
	        {
	          debugPrintln("[TimerClient2 " + clientNum + "] Quitting.");
	        }
	      }
	      
	      iters++;
	    }
	    	    
	    long finish = System.nanoTime();
	    
	    if (time)
	    {
	    	System.out.println("[TimerClient2] Session duration: " + (finish - start) + " nanos");
	    }
	  }
	  finally
	  {

	  }
	}
  
  private static final void debugPrintln(String m)
  {
  	if (debug)
  	{
  		System.out.println(m);
  	}
  }
  
  public static void main(String [] args) throws Exception
  {
  	boolean debug = Boolean.parseBoolean(args[0]);
  	String host = args[1];
    int port = Integer.parseInt(args[2]);
    int clientNum = Integer.parseInt(args[3]);
    int messageSize = Integer.parseInt(args[4]);
    int sessionLength = Integer.parseInt(args[5]);
    int repeats = Integer.parseInt(args[6]);

    new TimerClient2(debug, host, port, clientNum, messageSize, sessionLength, repeats).run();
  }
}
