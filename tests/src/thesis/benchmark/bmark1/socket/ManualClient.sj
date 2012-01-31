//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.sj.ManualClient false localhost 8888 -1 10 2 1 BODY 

package thesis.benchmark.bmark1.socket;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import sessionj.runtime.SJIOException;
import sessionj.runtime.util.SJRuntimeUtils;

import thesis.benchmark.Util;
import thesis.benchmark.ServerMessage;
import thesis.benchmark.TimerClient;

public class ManualClient extends TimerClient 
{
  public ManualClient(boolean debug, String host, int port, int cid, int serverMessageSize, int sessionLength, int iters, String flag) 
  {
  	super(debug, host, port, cid, serverMessageSize, sessionLength, iters, flag);
  }

  public void run(boolean warmup, boolean timer) throws Exception
  {	  	  
  	Socket s = null;
  	DataOutputStream os = null;
  	DataInputStream is = null;
  	
	  try 
	  {
	  	boolean debug = isDebug();
			int cid = getClientId();
			int serverMessageSize = getServerMessageSize();
	    int sessionLength = (warmup && !debug) ? WARMUP_SESSION_LENGTH : getSessionLength();
  	
    	startTimer();
			
	  	s = new Socket(getHost(), getPort());		    
	  	s.setTcpNoDelay(Util.TCP_NO_DELAY);
	  	os = new DataOutputStream(s.getOutputStream());
	  	is = new DataInputStream(s.getInputStream());	  	
	  	
	  	debugPrintln("[ManualClient] Connected.");
	  	
  		initialised();
	  	
    	os.writeInt(serverMessageSize);
    	os.flush();
  		
	    int len = 0;	    
	    while(len < sessionLength) 
	    {
	    	os.writeBoolean(true);
	    	os.flush();
	    	
	    	int bsLen = is.readInt();
	    	byte[] bs = new byte[bsLen];
	    	is.readFully(bs);
	    	ServerMessage msg = (ServerMessage) SJRuntimeUtils.deserializeObject(bs);             
        
        debugPrintln("[ManualClient " + cid + "] Received: " + msg);

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

    new ManualClient(debug, host, port, cid, serverMessageSize, sessionLength, iters, flag).run();
  }
}
