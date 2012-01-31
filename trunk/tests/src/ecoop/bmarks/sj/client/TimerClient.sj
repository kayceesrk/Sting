//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.TimerClient false localhost 8888 -1 100 10
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks.sj.client.TimerClient false localhost 8888 -1 100 10

package ecoop.bmarks.sj.client;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.*;
//import ecoop.bmarks.sj.event.server.Server;

// This counts as two clients (from the Server's view), due to the dummy run.
public class TimerClient 
{
	protocol pClient cbegin.rec X [!{REC: !<ClientMessage>.?(MyObject).#X, QUIT: }]
	//protocol pClient ^(Server.pServer)

	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;
  private int sessionLength;

  public TimerClient(boolean debug, String host, int port, int clientNum, int messageSize, int sessionLength) 
  {
  	TimerClient.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
    this.sessionLength = sessionLength;    
  }

  public void run() throws Exception
  {
  	try
  	{
	  	run(false); // Dummy run for warm up.
	  	
	  	debugPrintln("[TimerClient] Finished dummy run, now taking measurements.");
	  	
	  	run(true);
  	}
  	finally
  	{
	  	debugPrintln("[TimerClient] Sending KILL.");
	  	
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
	          
	          debugPrintln("[TimerClient " + clientNum + "] Received: " + mo);
	
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
	          debugPrintln("[TimerClient " + clientNum + "] Quitting.");
	        }
	      }
	      
	      iters++;
	    }
	    	    
	    long finish = System.nanoTime();
	    
	    if (time)
	    {
	    	System.out.println("[TimerClient] Session duration: " + (finish - start) + " nanos");
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

    new TimerClient(debug, host, port, clientNum, messageSize, sessionLength).run();
  }
}


/*//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.TimerClient false localhost 8888 -1 100 10

package ecoop.bmarks.sj.client;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.sj.common.*;
import ecoop.bmarks.sj.server.Server;

// This counts as two clients (from the Server's view), due to the dummy run.
public class TimerClient 
{
  //protocol pClient cbegin.rec X[!{QUIT: , REC: !<int>.?(MyObject).#X}]
	protocol pClient ^(Server.pServer)

	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;
  private int sessionLength;

  public TimerClient(boolean debug, String host, int port, int clientNum, int messageSize, int sessionLength) 
  {
  	TimerClient.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
    this.sessionLength = sessionLength;    
  }

  public void run() throws Exception
  {
  	try
  	{
	  	run(false); // Dummy run for warm up.
	  	
	  	debugPrintln("[TimerClient] Finished dummy run, now taking measurements.");
	  	
	  	run(true);
  	}
  	finally
  	{
	  	debugPrintln("Sending KILL.");
	  	
	  	new SignalClient().sendSignal(host, port, MyObject.KILL);  		
  	}
  }

  public void run(boolean time) throws Exception
  {
  	SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "a");
  	
	  final noalias SJService serv = SJService.create(pClient, host, port);
		
	  final noalias SJSocket s;
	  	  
	  try (s) 
	  {
	    s = serv.request(params);
	
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
	          
	          debugPrintln("[Client " + clientNum + "] Received: " + mo);
	
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
	          debugPrintln("[Client " + clientNum + "] Quitting.");
	        }
	      }
	      
	      iters++;
	    }
	    	    
	    long finish = System.nanoTime();
	    
	    if (time)
	    {
	    	System.out.println("[TimerClient] Session duration: " + (finish - start) + " nanos");
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

    new TimerClient(debug, host, port, clientNum, messageSize, sessionLength).run();
  }
}*/
