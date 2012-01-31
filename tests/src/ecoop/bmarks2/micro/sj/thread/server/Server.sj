//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.sj.thread.server.Server false 8888

package ecoop.bmarks2.micro.sj.thread.server;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks2.micro.*;

public class Server extends ecoop.bmarks2.micro.Server
{
	public protocol pBody rec X [?{REC: ?(ClientMessage).!<ServerMessage>.#X, QUIT: }]
	public protocol pServer sbegin.@(pBody)	
	
	volatile protected boolean kill = false;
	volatile private boolean finished = false;
	  
	private SJServerSocketCloser ssc;
	
  public Server(boolean debug, int port) 
  {
  	super(debug, port);
  }

  public void run() throws Exception
  {		
  	//SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "s");
  	
  	final noalias SJServerSocket ss;

  	noalias SJSocket s;

		try (ss) 
		{
			//ss = SJServerSocket.create(pServer, port, params);
			ss = SJServerSocket.create(pServer, getPort());
	
			this.ssc = ss.getCloser();
			
			debugPrintln("[Server] Listening on: " + getPort());
			
			boolean debug = isDebug();
			
			for (int i = 0; true; i++) 
			{
				try (s) 
				{
					s = ss.accept();
					
	    		<s>.spawn(new ServerThread(debug, this, i));
	    		
	    		addClient();
				} 				
    		finally 
    		{
    			
    		}
			}			
		}
		catch (SJIOException ioe) // Server socket was closed (hopefully by us).
		{
			//ioe.printStackTrace();
		}
   	finally 
   	{
   		this.finished = true;
   	}
  }

  public void kill() throws Exception
  {  	
  	int numClients = getNumClients(); // Unlike java.thread.Server, this will only tell us the number of LoadClients currently connected, not the number of TimerClients that had also previously connected. That's alright, the TimerClient (an individual) has already finished by now.
  	
  	this.kill = true; // It's important that no more clients are trying to connect after this point.
  	
  	while (getNumClients() > 0); 	
		
  	this.ssc.close(); // Break the accepting loop.
		
		while (!this.finished);
  	
		System.out.println("[Server] Finished running (" + numClients + " Clients joined).");
  }

  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	int port = Integer.parseInt(args[1]);
    
  	new Server(debug, port).run();
  }
}
	
class ServerThread extends SJThread
{  	
	private boolean debug;
	
	private Server server;
	private int tid;
	
	public ServerThread(boolean debug, Server server, int tid)
	{
		this.debug = debug;		
		this.server = server;		
		this.tid = tid;	
	}
	
	public void srun(noalias @(Server.pBody) s) 
	{
		try (s) 
		{				
			s.recursion(X) 
			{
				s.inbranch() 
				{
					case REC:
					{
						ClientMessage cm = (ClientMessage) s.receive();
						
						if (debug)
						{
							server.debugPrintln("[ServerThread] Received: " + cm);
						}
						
						s.send(new ServerMessage(cm.getServerMessageSize(), server.kill));
						
						if (server.isCounting()) 
	          {
	            server.incrementCount(tid);
	            
	            if (debug) // Redundant, but maybe faster.
	            {
	            	server.debugPrintln("[ServerThread] Current count:" + server.getCountTotal());
	            }
	          }
							
						s.recurse(X);
					}
					case QUIT:
					{
	  				server.removeClient();
	  				
	  				if (debug) // Redundant, but maybe faster.
	  				{
	  					server.debugPrintln("[ServerThread] Clients remaning: " + server.getNumClients());
	  				}
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

		}
	}
}
