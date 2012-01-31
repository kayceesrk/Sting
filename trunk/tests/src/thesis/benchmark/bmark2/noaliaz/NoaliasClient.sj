//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark2.noaliaz.NoaliasClient false localhost 8888 -1 10 2 1 BODY 

package thesis.benchmark.bmark2.noaliaz;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.net.SJService;
import sessionj.runtime.net.SJSocket;

import thesis.benchmark.Util;
import thesis.benchmark.TimerClient;
//import thesis.benchmark.bmark2.NoaliasMessage;
import thesis.benchmark.bmark2.NoaliasBinaryTree;

public class NoaliasClient extends TimerClient 
{
	//private static protocol pClient cbegin.!<int>.![?(NoaliasMessage).!<NoaliasMessage>]*
	private static protocol pClient cbegin.!<int>.![?(NoaliasBinaryTree).!<NoaliasBinaryTree>]*

  public NoaliasClient(boolean debug, String host, int port, int cid, int serverMessageSize, int sessionLength, int iters, String flag) 
  {
  	super(debug, host, port, cid, serverMessageSize, sessionLength, iters, flag);
  }

  public void run(boolean warmup, boolean timer) throws Exception
  {  	
	  final noalias SJService c = SJService.create(pClient, getHost(), getPort());
	  final noalias SJSocket s;
	  	  
	  try (s) 
	  {
	  	boolean debug = isDebug();
			int cid = getClientId();
			int serverMessageSize = getServerMessageSize();
	    int sessionLength = (warmup && !debug) ? WARMUP_SESSION_LENGTH : getSessionLength();
  	
    	startTimer();
			
	  	s = c.request();
		    
	  	debugPrintln("[NoaliasClient] Connected using: " + s.getConnection().getTransportName());
	  	
  		initialised();
	  	
    	s.send(serverMessageSize);
  		
	    int len = 0;	    
	    s.outwhile(len < sessionLength) 
	    {
	    	//noalias NoaliasMessage msg = (NoaliasMessage) s.receive();
	    	noalias NoaliasBinaryTree msg = (NoaliasBinaryTree) s.receive();
        
        debugPrintln("[NoaliasClient " + cid + "] Received: " + msg);

        msg.incrementMessageId();
        
        debugPrintln("[NoaliasClient " + cid + "] Dispataching: " + msg);
        
        s.send(msg);      
        
        if (debug)
        {
        	Thread.sleep(Util.DEBUG_DELAY);
        }
             	     
	      len++;
	    }
	    	    
    	bodyDone();
    	
    	//Thread.sleep(50);
	  }
	  finally { }
	  
   	stopTimer();		  	  	
	  
	  if (timer)
  	{
  		printTimer();
  	}	 	  
	  
  	resetTimer();
	}
  
  public static void main(String [] args) throws Exception
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	String host = args[1];
    int port = Integer.parseInt(args[2]);
    int cid = Integer.parseInt(args[3]);
    int serverMessageSize = Integer.parseInt(args[4]);
    int sessionLength = Integer.parseInt(args[5]);
    int iters = Integer.parseInt(args[6]);
    String flag = args[7];

    new NoaliasClient(debug, host, port, cid, serverMessageSize, sessionLength, iters, flag).run();
  }
}
