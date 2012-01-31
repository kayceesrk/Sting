//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.socket.ManualServer false 8888
//$ bin/sessionj -cp tests/classes/ -server thesis.benchmark.bmark1.socket.ManualServer false 8888

package thesis.benchmark.bmark1.socket;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import sessionj.runtime.SJIOException;
import sessionj.runtime.util.SJRuntimeUtils;

import thesis.benchmark.Util;
import thesis.benchmark.AbstractServer;
import thesis.benchmark.ServerMessage;

public class ManualServer extends AbstractServer
{
	protected volatile boolean run = true;
	private volatile boolean finished = false;
	  
	private ServerSocket ss;
	
  public ManualServer(boolean debug, int port) 
  {
  	super(debug, port);
  }

  public void run() throws Exception
  {		
		Socket s = null;
		try 
		{
			ss = new ServerSocket(getPort());				
			
			debugPrintln("[ManualServer] Listening on: " + getPort());
			
			boolean debug = isDebug();
			
			while (run) 
			{				
				try 
				{
					s = ss.accept();					
			    doSession(debug, s);			    
				} 				
    		finally { }
    		
    		System.gc(); // The API seems to indicate that this call blocks until the GC is done
			}			
		}
		catch (IOException ioe) // ServerSocket was closed (hopefully by us)
		{
			//ioe.printStackTrace();
		}
   	finally 
   	{
   		this.finished = true;   		 		
   		Util.closeSocket(s);
   		Util.closeServerSocket(ss);
   	}
  }

  private void doSession(boolean debug, Socket s) throws IOException, SJIOException, InterruptedException
  {
  	s.setTcpNoDelay(Util.TCP_NO_DELAY);
  	
  	DataInputStream is = null;
  	DataOutputStream os = null; 	
  	try
  	{	  	  
	  	is = new DataInputStream(s.getInputStream());
	  	os = new DataOutputStream(s.getOutputStream());
	  	
			int serverMessageSize = is.readInt();		  			     
			
			debugPrintln("[ManualServer] Received message size parameter: " + serverMessageSize);
			
	    int len = 0;	    
	    while (is.readBoolean()) 
	    {
	      ServerMessage msg = new ServerMessage(0, new Integer(len).toString(), serverMessageSize);
	      byte[] bs = SJRuntimeUtils.serializeObject(msg);
	      os.writeInt(bs.length);
	      os.write(bs);
	      os.flush();
	      
	      debugPrintln("[ManualServer] Dispatached: " + msg);
	
	      if (debug)
	      {
	      	Thread.sleep(Util.DEBUG_DELAY);
	      }
	           	     
	      len++;
	    }
  	}
  	finally
  	{
   		Util.closeOutputStream(os);
   		Util.closeInputStream(is);    		
  	}
  }
  
  public void kill() throws Exception
  {  	  	
  	run = false; // It's important that no more clients are trying to connect after this point		
  	ss.close();  // Break the accepting loop (make the blocked accept throw an exception)		
		while (!this.finished);
  }

  public static void main(String [] args) throws Exception 
  {
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
  	int port = Integer.parseInt(args[1]);
    
  	new ManualServer(debug, port).run();
  }
}
