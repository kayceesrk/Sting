//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.macro.smtp.ClientRunner false localhost 8888 -1 100 1 10
//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.macro.smtp.ClientRunner false localhost 9088 -1 100 1 10

package ecoop.bmarks2.macro.smtp;

import java.io.*;
import java.net.*;
import ecoop.bmarks2.micro.Common;
import ecoop.bmarks2.micro.StartSpinningController;

// Mostly duplicated from the microbenchmark equivalent: spawns DummyClients.
public class ClientRunner 
{
  public static void main(String [] args) throws Exception
  {
    final boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
    final String host = args[1];
    final int serverPort = Integer.parseInt(args[2]);
    final int scriptPort = Integer.parseInt(args[3]);
    
    int delay = Integer.parseInt(args[4]);
    final int numClients = Integer.parseInt(args[5]);    
    final int msgSize = Integer.parseInt(args[6]);
    final int basePort = Integer.parseInt(args[7]);
      
  	final boolean[] ack = new boolean[] { false };
  	final boolean[] spin = new boolean[] { false };

    Thread.sleep(2000);
  	
    for (int i = 0; i < numClients; i++)	
    {
      final int cid = i;
      
      new Thread() 
      {
        public void run() 
        {
        	try
        	{        		
        		new ecoop.bmarks2.macro.smtp.client.DummyClient(debug).run(host, serverPort, msgSize, ack, spin);        		
        	}
        	catch (Exception x)
        	{
        		throw new RuntimeException(x);
        	}
        }
      }.start();
      
    	synchronized (ack)  
    	{
    		while (!ack[0])
    		{
    			ack.wait();
    		}
    		
            ack[0] = false;	    	
    	}
    	
    	
    	System.out.println("[ClientRunner] Ack received from Client: " + cid);
      
      	Thread.sleep(delay);
    }
    
    // Here, threads have been created (and started?) but the LoadClients are not necessarily connected yet. 
    if (scriptPort > 0) 
    {
	    Socket s = null;
	    
	    try
	    {
	    	String script;
	    	
	    	if (host.equals("localhost")) // FIXME: stupid hack for python server sockets.
	    	{
	    		script = host;
	    	}
	    	else
	    	{
	    		script = InetAddress.getLocalHost().getHostName();
	    	}
	    	
	    	s = new Socket(script, scriptPort);
	    }
	    finally
	    {
				ecoop.bmarks2.micro.Common.closeSocket(s);    	
	    }
    }

    Socket s1 = null;
    InputStream is = null;
    try {
        Common.debugPrintln(debug, "Waiting for spinning signal from server, connecting to: " 
            + host + ":" + (basePort+StartSpinningController.OFFSET));
        s1 = new Socket(host, basePort+StartSpinningController.OFFSET);
        is = s1.getInputStream();
        is.read();
    } finally {
        Common.closeSocket(s1);
        Common.closeInputStream(is);
    }
    synchronized (spin) {
        spin[0] = true;
        spin.notifyAll();
    }
    
    Common.debugPrintln(debug, "Received start signal from server, spinning");
  }
}
