//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark1.socket.StreamServer false 8888
//$ bin/sessionj -cp tests/classes/ -server thesis.benchmark.bmark1.socket.StreamServer false 8888

package thesis.benchmark.bmark1.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import thesis.benchmark.Util;
import thesis.benchmark.AbstractServer;
import thesis.benchmark.ServerMessage;

public class StreamServer extends AbstractServer
{
	protected volatile boolean run = true;
	private volatile boolean finished = false;
	  
	private ServerSocket ss;
	
  public StreamServer(boolean debug, int port) 
  {
  	super(debug, port);
  }

  public void run() throws Exception
  {		
		Socket s = null;
		try 
		{
			ss = new ServerSocket(getPort());				
			
			debugPrintln("[StreamServer] Listening on: " + getPort());
			
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

  private void doSession(boolean debug, Socket s) throws IOException, InterruptedException
  {
  	s.setTcpNoDelay(Util.TCP_NO_DELAY);
  	
  	ObjectInputStream is = null;
  	ObjectOutputStream os = null; 	
  	try
  	{	  	  
	  	is = new ObjectInputStream(s.getInputStream());
	  	os = new ObjectOutputStream(s.getOutputStream());
	  	
			int serverMessageSize = is.readInt();		  			     
			
			debugPrintln("[StreamServer] Received message size parameter: " + serverMessageSize);
			
	    int len = 0;	    
	    while (is.readBoolean()) 
	    {
	      ServerMessage msg = new ServerMessage(0, new Integer(len).toString(), serverMessageSize);            	      
	      os.writeObject(msg);
	      //os.flush(); // Doesn't seem to make much difference here or after reset
	      os.reset(); // Writes a byte
	      os.flush();
	      
	      debugPrintln("[StreamServer] Dispatached: " + msg);
	
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
    
  	new StreamServer(debug, port).run();
  }
}
