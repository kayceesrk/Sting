//$ bin/sessionj -cp tests/classes/ ecoop.bmarks.sj.server.thread.Server false 8888 1

package ecoop.bmarks.sj.server.thread;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.*;

// The difference with Server is that we don't print the count on exit. 
public class Server 
{
	public protocol pBody rec X [?{REC: ?(ClientMessage).!<MyObject>.#X, QUIT: }]
	public protocol pServer sbegin.@(pBody)	
	
	public static final int REC = 1;
	public static final int QUIT = 2;

	public static int signal = MyObject.NO_SIGNAL;
	public static boolean counting = false;
	public static boolean counted = false;
	
	private static boolean debug;
		
  private int port;
  private int numClients; // NB: a TimerClient counts as two clients.
  
  //private long count = 0;
  private int[] counts;

  private Object lock = new Object();
  private int active; 
  
  public Server(boolean debug, int port, int numClients) 
  {
  	Server.debug = debug;
  	
    this.port = port;
    this.numClients = numClients;
    
    this.active = numClients;
  }

  class ServerThread extends SJThread
  {  	
  	private int tid;
  	
  	private Server server;
  	
  	public ServerThread(int tid, Server server)
  	{
  		this.tid = tid;
  		this.server = server;	
  	}
  	
  	public void srun(noalias @(pBody) s) 
  	{
			try (s) 
			{				
				//System.out.println("start of thread: tid=" + tid + ", active=" + server.active);
				
				s.recursion(X) 
				{
					s.inbranch() 
					{
						case REC:
						{
							ClientMessage cm = (ClientMessage) s.receive();
							
							debugPrintln("[Server] Received: " + cm);
							
							s.send(new MyObject(signal, cm.getSize()));
							
							if (counting) 
							{
								//count++;
								counts[tid]++;
								
								debugPrintln("[Server] Current count:" + counts[tid]);
							}
								
							s.recurse(X);
						}
						case QUIT:
						{
							numClients--; // This is not thread safe, but we'll leave it because numClients isn't used for anything important (we're joining for termination).
							
							debugPrintln("[Server] Clients remaning: " + numClients);
						}
					}
				} 	   
			}
			catch (Exception x)
			{
				throw new RuntimeException(x);
			}
			finally
			{
				synchronized (server.lock)
				{
					if (--server.active == 0)
					{						
						server.lock.notify();
					}
				}					
			}
		}
  }
  
  public void run() throws Exception
  {		
  	//SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "s");
  	
  	final noalias SJServerSocket ss;

  	noalias SJSocket s;

		try (ss) 
		{
			//ss = SJServerSocket.create(pServer, port, params);
			ss = SJServerSocket.create(pServer, port);
			
			debugPrintln("[Server] Listening on: " + port);
			
			counts = new int[numClients];
			
			int nc = numClients;
			
			for (int i = 0; i < nc; i++) 
			{
				try (s) 
				{
					s = ss.accept();
					
					//System.out.println("server spawning thread for: tid=" + i);
					
	    		<s>.spawn(new ServerThread(i, this));
				} 				
    		finally 
    		{
    			
    		}
			}
			
			synchronized (lock)
			{
				while (active > 0)
				{
					try
					{
						//System.out.println("server waiting: active=" + active);
						
						lock.wait();
						
						//System.out.println("server woke up: active=" + active);
					}
					catch (InterruptedException ie)
					{
						throw new RuntimeException(ie);
					} 
				}
			}
		}
   	finally 
   	{
   		if (counted)
			{
   			int total = 0;
   			
   			for (int i = 0; i < counts.length; i++)
   			{
   				total += counts[i];
   			}
   			
				System.out.println("[Server] Total count: " + total);
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
	
/*protocol pBody rec X [?{REC: ?(ClientMessage).!<MyObject>.#X, QUIT: }]
 		protocol pServer sbegin.@(pBody)
  
  	private int port;
 		private int numClients;
  	private long count = 0;
	
		private static boolean debug;

  	private static long t = 0;
  	public static int signal = MyObject.NO_SIGNAL;

  	private static boolean counting = false;

  	public Server(boolean debug, int port, int numClients) {

				Server.debug = debug;

    		this.port = port;
    		this.numClients = numClients;
  	}

  	static class ServerThread extends SJThread {

			public void srun(noalias @(sendInt) s) {

		  	try (s) {
					
      			s.recursion(X) {
	        			s.inbranch() {
	          	  		case REC:
													ClientMessage m = (ClientMessage) s.readObject();
													s.send(new MyObject(signal, m.getSize()));
													if (counting) 
	          							{
	          	  						count++;
								            debugPrintln("[Server] Current count:" + count);
	          							}
													s.recurse(X);
	          	      case QUIT:
	          	      		numClients--;
	          	          debugPrintln("[Server] Clients remaning: " + numClients);
	          	  }
		        } 	   
		    }
		    catch(Exception e){e.printStackTrace();}
			}

		}


	  public void server(int port, int numClient) {
		    noalias SJServerSocket ss;
		    noalias SJSocket s;
		    SJSessionParameters params = null;

		  	try
		  	{
				  	params = SJTransportUtils.createSJSessionParameters("s", "a");
		  	}
		  	catch(Exception e){e.printStackTrace();}


		    try (ss) {
			      ss = SJServerSocket.create(serverSide, port, params);
			      while (numClient -- != 0) {
		        		try (s) {
										s = ss.accept();
			          		<s>.spawn(new ServerThread());
      		  		} catch(Exception e) {e.printStackTrace();}
      		    		finally {}
      			}
    		} catch(Exception e) {e.printStackTrace();}
      		finally {}
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

}*/
