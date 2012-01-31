//$ bin/sessionjc -cp tests/classes/ thesis/benchmark/bmark1/rmi/RMIClient.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.rmi.RMIClient false HZHL2 8888 -1 10 2 1 BODY

package thesis.benchmark.bmark1.rmi;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import thesis.benchmark.Util;
import thesis.benchmark.ServerMessage;
import thesis.benchmark.TimerClient;

public class RMIClient extends TimerClient
{
  public RMIClient(boolean debug, String host, int cid, int serverMessageSize, int sessionLength, int iters, String flag) 
  {
  	super(debug, host, -1, cid, serverMessageSize, sessionLength, iters, flag);  	
  }
	
  public void run(boolean warmup, boolean timer) throws Exception
  {
		boolean debug = isDebug();
		int cid = getClientId();
		int serverMessageSize = getServerMessageSize();
    int sessionLength = (warmup && !debug) ? WARMUP_SESSION_LENGTH : getSessionLength();
	
  	startTimer();
		
		Registry registry = LocateRegistry.getRegistry(getHost());		
		RMIServer server = (RMIServer) registry.lookup(RMIServer.RMI_SERVER_OBJECT);
	    
  	debugPrintln("[RMIClient] Connected.");
  	
		initialised();
  	
		server.init();
  	server.setServerMessageSize(serverMessageSize);
		
    int len = 0;	    
    while(len < sessionLength) 
    {
    	ServerMessage msg = server.getServerMessage(true);            
      
      debugPrintln("[RMIClient " + cid + "] Received: " + msg);

      if (debug)
      {
      	Thread.sleep(Util.DEBUG_DELAY);
      }
           	     
      len++;
    }
    	    
    server.getServerMessage(false);    
    server.close();
    
  	bodyDone();
  
	 	stopTimer();		  	  	
	  
	  if (timer)
		{
			printTimer();
		}	 	  
	  
		resetTimer();		
	}

  public int getPort()
  {
  	throw new RuntimeException("[RMIClient] Port is not used.");
  }
  
	public static void main(String[] args) throws Exception
	{
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	String host = args[1]; // The RMI Registry host
    int port = Integer.parseInt(args[2]); // Leave this here to make the python script simpler; but we're ignoring this value
    int cid = Integer.parseInt(args[3]);
    int serverMessageSize = Integer.parseInt(args[4]);
    int sessionLength = Integer.parseInt(args[5]);
    int iters = Integer.parseInt(args[6]);
    String flag = args[7];

    new RMIClient(debug, host, cid, serverMessageSize, sessionLength, iters, flag).run();
	}
}
