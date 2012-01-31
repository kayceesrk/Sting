//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.sj.StreamClient false localhost 8888 -1 10 2 1 BODY 

package thesis.benchmark.bmark1.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import thesis.benchmark.Util;
import thesis.benchmark.ServerMessage;
import thesis.benchmark.TimerClient;

public class StreamClient extends TimerClient 
{
  public StreamClient(boolean debug, String host, int port, int cid, int serverMessageSize, int sessionLength, int iters, String flag) 
  {
  	super(debug, host, port, cid, serverMessageSize, sessionLength, iters, flag);
  }

  public void run(boolean warmup, boolean timer) throws Exception
  {	  	  
  	Socket s = null;
  	ObjectOutputStream os = null;
  	ObjectInputStream is = null;
  	
	  try 
	  {
	  	boolean debug = isDebug();
			int cid = getClientId();
			int serverMessageSize = getServerMessageSize();
	    int sessionLength = (warmup && !debug) ? WARMUP_SESSION_LENGTH : getSessionLength();
  	
    	startTimer();
			
	  	s = new Socket(getHost(), getPort());		    
	  	s.setTcpNoDelay(Util.TCP_NO_DELAY);
	  	os = new ObjectOutputStream(s.getOutputStream());
	  	is = new ObjectInputStream(s.getInputStream());	  	
	  	
	  	debugPrintln("[StreamClient] Connected.");
	  	
  		initialised();
	  	
    	os.writeInt(serverMessageSize);
    	os.flush();
  		
	    int len = 0;	    
	    while(len < sessionLength) 
	    {
	    	os.writeBoolean(true);
	    	os.flush();
	    	
	    	ServerMessage msg = (ServerMessage) is.readObject();            
        
        debugPrintln("[StreamClient " + cid + "] Received: " + msg);

        if (debug)
        {
        	Thread.sleep(Util.DEBUG_DELAY);
        }
             	     
	      len++;
	    }
	    
	    os.writeBoolean(false);
	    os.flush();
	    	    
    	bodyDone();
    	
    	//Thread.sleep(50);
	  }
	  finally 
	  { 
   		Util.closeOutputStream(os);
   		Util.closeInputStream(is);   		
   		Util.closeSocket(s);	  	
	  }
	  
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

    new StreamClient(debug, host, port, cid, serverMessageSize, sessionLength, iters, flag).run();
  }
}
