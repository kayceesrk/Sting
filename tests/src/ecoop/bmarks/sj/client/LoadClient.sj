//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.LoadClient false localhost 8888 1234 100
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks.sj.client.LoadClient false localhost 8888 1234 100

package ecoop.bmarks.sj.client;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.*;
//import ecoop.bmarks.sj.server.event.Server;

public class LoadClient 
{
  protocol pClient cbegin.rec X [!{REC: !<ClientMessage>.?(MyObject).#X, QUIT: }]
	//protocol pClient ^(Server.pServer)

	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;

  public LoadClient(boolean debug, String host, int port, int clientNum, int messageSize) 
  {
  	LoadClient.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
  }

  public void run() throws Exception
  {
  	//SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "sa");  	
  	
  	final noalias SJService c = SJService.create(pClient, host, port);
  	
    final noalias SJSocket s;
    
    try (s) 
    {
      //s = serv.request(params);
    	s = c.request();

      MyObject mo;
      
      boolean run = true; 
      int iters = 0;
      
      s.recursion(X) 
      {
        if (run) 
        {
          s.outbranch(REC) 
          {
            s.send(new ClientMessage(clientNum, Integer.toString(iters++), messageSize));
            
            mo = (MyObject) s.receive();      
            
            run = !mo.killSignal();
            
            debugPrintln("[LoadClient " + clientNum + "] Received: " + mo);

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
            debugPrintln("[LoadClient " + clientNum + "] Quitting.");
          }
        }
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

    new LoadClient(debug, host, port, clientNum, messageSize).run();
  }
}


/*//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.client.LoadClient false localhost 8888 1234 100

package ecoop.bmarks.sj.client;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.sj.common.*;
import ecoop.bmarks.sj.server.Server;

public class LoadClient 
{
  //protocol pClient cbegin.rec X[!{QUIT: , REC: !<int>.?(MyObject).#X}]
	protocol pClient ^(Server.pServer)

	private static boolean debug;
	
  private String host;
  private int port;
  
  private int clientNum;
  private int messageSize;

  public LoadClient(boolean debug, String host, int port, int clientNum, int messageSize) 
  {
  	LoadClient.debug = debug;
  	
  	this.host = host;
  	this.port = port;
    
  	this.clientNum = clientNum;
    this.messageSize = messageSize;
  }

  public void run() throws Exception
  {
  	SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "a");  	
  	
  	final noalias SJService serv = SJService.create(pClient, host, port);
  	
    final noalias SJSocket s;
    
    try (s) 
    {
      s = serv.request(params);

      MyObject mo;
      
      boolean run = true; 
      int iters = 0;
      
      s.recursion(X) 
      {
        if (run) 
        {
          s.outbranch(REC) 
          {
            s.send(new ClientMessage(clientNum, Integer.toString(iters++), messageSize));
            
            mo = (MyObject) s.receive();      
            
            run = !mo.killSignal();
            
            debugPrintln("[LoadClient " + clientNum + "] Received: " + mo);

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
            debugPrintln("[LoadClient " + clientNum + "] Quitting.");
          }
        }
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

    new LoadClient(debug, host, port, clientNum, messageSize).run();
  }
}*/
