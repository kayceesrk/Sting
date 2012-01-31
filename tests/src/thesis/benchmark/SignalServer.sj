package thesis.benchmark;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import thesis.benchmark.Util;

public class SignalServer 
{
	public static final int SIGNAL_SERVER_PORT_OFFSET = -100;
	
	public static final int KILL = 1;
	
	private boolean debug; 
  private int port; // The same value as for Server: we apply the offset in here  
  //private Server server;
  private Killable server;
  
  public SignalServer(boolean debug, int port, Killable server)
  {
  	this.debug = debug;
    this.port = port;
    this.server = server;
  }

  public void run() throws Exception
  {
  	ServerSocket ss = null;
  	Socket s  = null; 
    DataInputStream is = null;
    
    try 
    {
      ss = new ServerSocket(port + SIGNAL_SERVER_PORT_OFFSET);
      
      Util.debugPrintln(debug, "[SignalServer] Listening on: " + (port + SIGNAL_SERVER_PORT_OFFSET));
      
      for (boolean run = true; run; ) 
      {
        s = ss.accept();
        is = new DataInputStream(s.getInputStream());
        
        int signal = is.readInt();

        Util.debugPrintln(debug, "[SignalServer] Read: " + signal);
       
        switch (signal)
        {
        	case KILL:
        	{
        		run = false;        		
        		server.kill();        		        
        		break;
        	}
        	default:
        	{
        		throw new RuntimeException("[SignalServer] Unexpected signal: " + signal);
        	}
        }
      }
    }
    finally
    {
    	Util.closeInputStream(is);
    	Util.closeSocket(s);
    	Util.closeServerSocket(ss);
    }
    
    Util.debugPrintln(debug, "[SignalServer] Finished.");
  }
}
