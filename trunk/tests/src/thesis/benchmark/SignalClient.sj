//$ bin/sessionj -cp tests/classes/ thesis.benchmark.SignalClient false localhost 8888 KILL

package thesis.benchmark;

import java.io.DataOutputStream;
import java.net.Socket;

import thesis.benchmark.Util;

public class SignalClient 
{	
	private static final String KILL = "KILL";
	
	private boolean debug;
	private String host;
	private int port;
	
	public SignalClient(boolean debug, String host, int port)
  {		
  	this.debug = debug;
  	this.host = host;
  	this.port = port;
  }	
	
  public void sendSignal(int signal) throws Exception
  {
  	Socket s = null; 	
  	DataOutputStream os = null;
  	
    try 
    {
    	Util.debugPrintln(debug, "[SignalClient] Connecting to: " + host + ":" + (port + SignalServer.SIGNAL_SERVER_PORT_OFFSET)); 
    	
    	s = new Socket(host, port + SignalServer.SIGNAL_SERVER_PORT_OFFSET);      
      os = new DataOutputStream(s.getOutputStream());
      
      os.writeInt(signal);
      os.flush();
    }
    finally
    {
     	Util.closeOutputStream(os); 
     	Util.closeSocket(s);
    }    
  }

  public static void main(String args[]) throws Exception
  {
    boolean debug = Boolean.parseBoolean(args[0]);
  	String host = args[1];
    int port = Integer.parseInt(args[2]);    
    String command = args[3].toUpperCase();    
    
    int signal;        
    if (command.equals(KILL))
    {
      signal = SignalServer.KILL;
    }
    else
    {
    	throw new RuntimeException("[SignalClient] Unrecognised command: " + args[2]);
    }

    new SignalClient(debug, host, port).sendSignal(signal);
  }
}
