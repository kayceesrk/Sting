//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark2.noaliaz.NoaliasServer false 8888
//$ bin/sessionj -cp tests/classes/ -server thesis.benchmark.bmark2.noaliaz.NoaliasServer false 8888

package thesis.benchmark.bmark2.noaliaz;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.net.SJServerSocket;
import sessionj.runtime.net.SJServerSocketCloser;
import sessionj.runtime.net.SJSocket;

import thesis.benchmark.Util;
import thesis.benchmark.AbstractServer;
//import thesis.benchmark.bmark2.NoaliasMessage;
import thesis.benchmark.bmark2.NoaliasBinaryTree;

public class NoaliasServer extends AbstractServer
{
	//private static protocol pServerBody ?(int).?[!<NoaliasMessage>.?(NoaliasMessage)]*
	private static protocol pServerBody ?(int).?[!<NoaliasBinaryTree>.?(NoaliasBinaryTree)]*
	private static protocol pServer sbegin.@(pServerBody)
	
	protected volatile boolean run = true;
	private volatile boolean finished = false;
	  
	private SJServerSocketCloser ssc;
	
  public NoaliasServer(boolean debug, int port) 
  {
  	super(debug, port);
  }

  public void run() throws Exception
  {			
  	final noalias SJServerSocket ss;  	
		try (ss) 
		{
			ss = SJServerSocket.create(pServer, getPort());				
			ssc = ss.getCloser();
			
			debugPrintln("[NoaliasServer] Listening on: " + getPort());
			
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

  private void doSession(boolean debug, final noalias @(pServerBody) s) throws SJIOException, ClassNotFoundException, InterruptedException
  {
		int serverMessageSize = s.receiveInt();		  			     
		
		debugPrintln("[NoaliasServer] Received message size parameter: " + serverMessageSize);
			    
    //noalias NoaliasMessage msg = new NoaliasMessage(-1, 0, serverMessageSize);
		noalias NoaliasBinaryTree msg = NoaliasBinaryTree.createDepth(-1, 0, serverMessageSize);
    s.inwhile() 
    {                  
      debugPrintln("[NoaliasServer] Dispataching: " + msg);      
    	
    	s.send(msg);     

      //msg = (NoaliasMessage) s.receive();
      msg = (NoaliasBinaryTree) s.receive();
      
      debugPrintln("[NoaliasServer] Received: " + msg);
      
      msg.incrementMessageId();
      
      if (debug)
      {
      	Thread.sleep(Util.DEBUG_DELAY);
      }
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
    
  	new NoaliasServer(debug, port).run();
  }
}
