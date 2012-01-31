//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.server.event.Server false 8888 1
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks.sj.server.event.Server false 8888 1

package ecoop.bmarks.sj.server.event;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.*;

public class Server  
{
	public protocol pRecursion rec X [?{REC: ?(ClientMessage).!<MyObject>.#X, QUIT: }]
  public protocol pReceive ?(ClientMessage).!<MyObject>.@(pRecursion) 
  public protocol pServer sbegin.@(pRecursion)
  
  private protocol pSelector { @(pRecursion), @(pReceive) }

	public static int signal = MyObject.NO_SIGNAL;
	public static boolean counting = false;
	public static boolean counted = false;
	
	private static boolean debug;
		
  private int port;
  private int numClients; // NB: a TimerClient counts as two clients.
  
  private long count = 0;

  public Server(boolean debug, int port, int numClients) 
  {
  	Server.debug = debug;
  	
    this.port = port;
    this.numClients = numClients;
  }

  public void run() throws Exception
  {
		//SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "a");
			
		final noalias SJSelector sel = SJRuntime.selectorFor(pSelector);
		
		try (sel) 
		{
			noalias SJServerSocket ss;
			
			try (ss) 
			{
				//ss = SJServerSocket.create(pServer, port, params);
				ss = SJServerSocket.create(pServer, port);
				
				debugPrintln("[Server] Listening on: " + port);
				
			  sel.registerAccept(ss);
			}
	    finally 
	    {
	    	
	    }

			noalias SJSocket s;
	    
		  try (s) 
		  {
		    while (numClients > 0) 
		    {
		      s = sel.select();
		      
		      typecase (s) 
		      {
		        when(@(pRecursion)) 
		        {
		          s.recursion(X) 
		          {
		            s.inbranch() 
		            {
		              case REC:
		              {
		              	sel.registerInput(s);
		              }
		              case QUIT:
		              {
		                numClients--;
		                
		                debugPrintln("[Server] Clients remaning: " + numClients);
		              }
		            }
		          }
		        }
		        when(@(pReceive)) 
		        {
		        	ClientMessage cm = (ClientMessage) s.receive();
		          
		          debugPrintln("[Server] Received: " + cm);
		          
		          s.send(new MyObject(signal, cm.getSize()));
		          
		          if (counting) 
		          {
		            count++;
		            
		            debugPrintln("[Server] Current count:" + count);		            
	            }
		          
	            sel.registerInput(s);
	          }
	        }
	      }		   
		  }
		  finally
		  {
		  	
		  }
		}
		finally
		{
			if (counted)
			{
				System.out.println("[Server] Total count: " + count);
			}
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
  	int port = Integer.parseInt(args[1]);
  	int numClients = Integer.parseInt(args[2]);
  	
    new Server(debug, port, numClients).run();
  }
}
