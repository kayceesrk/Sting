//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.server.Server false 8888 1 

package ecoop.bmarks.sj.server;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.sj.common.*;

//@deprecated
public class Server  
{
	public protocol pRecursion rec X [?{REC: ?(ClientMessage).!<MyObject>.#X, QUIT: }]
  public protocol pReceive ?(ClientMessage).!<MyObject>.@(pRecursion) 
  public protocol pServer sbegin.@(pRecursion)
  
  private protocol pSelector { @(pRecursion), @(pReceive) }

	private static boolean debug;
	
	protected static int signal = MyObject.NO_SIGNAL;
	protected static boolean counting = false;
	
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
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "a");
			
		final noalias SJSelector sel = SJRuntime.selectorFor(pSelector);
		
		try (sel) 
		{
			noalias SJServerSocket ss;
			
			try (ss) 
			{
				ss = SJServerSocket.create(pServer, port, params);
				
				debugPrintln("[Server] Listening on: " + port);
				
			  sel.registerAccept(ss);
			}
	    finally 
	    {
	    	
	    }

			noalias SJSocket s;
	    
		  try (s) 
		  {
		    while(numClients != 0) 
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
		        	ClientMessage m = (ClientMessage) s.receive();
		          
		          debugPrintln("[Server] Received: " + m);
		          
		          s.send(new MyObject(signal, m.getSize()));
		          
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
			if (counting)
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
  
  /*public static void sendKill() 
  {
    signal |= MyObject.KILL_LOAD;
  }

  public static void sendTiming() 
  {
    signal |= MyObject.BEGIN_TIMING;
  }

  public static void sendCounting() 
  {
    counting = true;
  }*/

  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0]);
  	int port = Integer.parseInt(args[1]);
  	int numClients = Integer.parseInt(args[2]);
  	
    new Server(debug, port, numClients).run();
  }
}
