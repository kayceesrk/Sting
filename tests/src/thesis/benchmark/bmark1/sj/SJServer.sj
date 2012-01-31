//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.sj.SJServer false 8888
//$ bin/sessionj -cp tests/classes/ -server thesis.benchmark.bmark1.sj.SJServer false 8888

package thesis.benchmark.bmark1.sj;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.net.SJServerSocket;
import sessionj.runtime.net.SJServerSocketCloser;
import sessionj.runtime.net.SJSocket;

import thesis.benchmark.Util;
import thesis.benchmark.AbstractServer;
import thesis.benchmark.ServerMessage;

public class SJServer extends AbstractServer
{
	private static protocol pServerBody ?(int).?[!<ServerMessage>]*
	private static protocol pServer sbegin.@(pServerBody)
	
	protected volatile boolean run = true;
	private volatile boolean finished = false;
	  
	private SJServerSocketCloser ssc;
	
  public SJServer(boolean debug, int port) 
  {
  	super(debug, port);
  }

  public void run() throws Exception
  {		
  	//SJSessionParameters params = SJTransportUtils.createSJSessionParameters("s", "s");	
  	final noalias SJServerSocket ss;  	
		try (ss) 
		{
			//ss = SJServerSocket.create(pServer, port, params);
			ss = SJServerSocket.create(pServer, getPort());				
			ssc = ss.getCloser();
			
			debugPrintln("[SJServer] Listening on: " + getPort());
			
			boolean debug = isDebug();			
			while (run) 
			{
				final noalias SJSocket s;			
				try (s) 
				{
					s = ss.accept();					
			    doSession(debug, s);			    
				} 				
    		finally { }
    		
    		System.gc(); // The API seems to indicate that this call blocks until the GC is done
			}			
		}
		catch (SJIOException ioe) // ServerSocket was closed (hopefully by us)
		{
			//ioe.printStackTrace();
		}
   	finally 
   	{
   		this.finished = true;
   	}
  }

  private void doSession(boolean debug, final noalias @(pServerBody) s) throws SJIOException, InterruptedException
  {
		int serverMessageSize = s.receiveInt();		  			     
		
		debugPrintln("[SJServer] Received message size parameter: " + serverMessageSize);
		
    int len = 0;	    
    s.inwhile() 
    {
      ServerMessage msg = new ServerMessage(0, new Integer(len).toString(), serverMessageSize);                  
      s.send(msg);
      
      debugPrintln("[SJServer] Dispatached: " + msg);

      if (debug)
      {
      	Thread.sleep(Util.DEBUG_DELAY);
      }
           	     
      len++;
    }  	
  }
  
  public void kill() throws Exception
  {  	  	
  	run = false; // It's important that no more clients are trying to connect after this point		
  	ssc.close(); // Break the accepting loop (make the blocked accept throw an exception)		
		while (!this.finished);
  }

  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	int port = Integer.parseInt(args[1]);
    
  	new SJServer(debug, port).run();
  }
}
