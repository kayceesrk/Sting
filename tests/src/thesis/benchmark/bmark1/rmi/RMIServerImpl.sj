//$ bin/sessionjc -cp tests/classes/ thesis/benchmark/bmark1/rmi/RMIServerImpl.sj -d tests/classes/
//$ rmiregistry &
//$ bin/sessionj -cp tests/classes/ -j -Djava.rmi.server.codebase=file:///c:/cygwin/home/Raymond/code/java/eclipse/sessionj-hg/tests/classes/ -j -Djava.security.policy=C:/cygwin/home/Raymond/code/java/eclipse/sessionj-hg/tests/src/thesis/benchmark/bmark1/rmi/security-localhost.policy thesis.benchmark.bmark1.rmi.RMIServerImpl false
//$ bin/sessionj -cp tests/classes/ -j -Djava.rmi.server.codebase=file:///homes/rhu/code/java/eclipse/sessionj-hg/tests/classes/ -j -Djava.security.policy=/homes/rhu/code/java/eclipse/sessionj-hg/tests/src/thesis/benchmark/bmark1/rmi/security.policy thesis.benchmark.bmark1.rmi.RMIServerImpl false

package thesis.benchmark.bmark1.rmi;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import thesis.benchmark.Util;
import thesis.benchmark.ServerMessage;
import thesis.benchmark.Server;

// Some methods duplicated from AbstractServer since we cannot extend it here.
public class RMIServerImpl extends UnicastRemoteObject implements RMIServer, Server 
{
	private boolean debug;
	private int serverMessageSize;
	private int len;

	private volatile RMIServer remoteObject;
	
	// Port is not needed 
	public RMIServerImpl(boolean debug) throws RemoteException 
	{
  	this.debug = debug;
	}

  public void run() throws Exception
  {	
  	debugPrintln("[RMIServer] codebase: " + System.getProperty("java.rmi.server.codebase"));
		debugPrintln("[RMIServer] security policy: " + System.getProperty("java.security.policy"));  	
  	
  	remoteObject = this;		
		System.setSecurityManager(new RMISecurityManager());		
		Registry registry = LocateRegistry.getRegistry();		
		registry.rebind(RMIServer.RMI_SERVER_OBJECT, remoteObject);
		
		debugPrintln("[RMIServer] Server object exported to RMI registry at localhost on standard port and bound to name: " + RMIServer.RMI_SERVER_OBJECT);
  }
  
  public void kill() throws NoSuchObjectException
  {  	  	
		UnicastRemoteObject.unexportObject(remoteObject, true);
  }
	
  public void init() //throws RemoteException
  {
  	len = 0;
  }
  
	public void setServerMessageSize(int serverMessageSize) //throws RemoteException
	{
		this.serverMessageSize = serverMessageSize;
	}
	
	public void close() //throws RemoteException
	{
		System.gc();
	}
	
	public ServerMessage getServerMessage(boolean flag) //throws RemoteException
	{
		if (flag)
		{
      ServerMessage msg = new ServerMessage(0, new Integer(len++).toString(), serverMessageSize);                
      
      debugPrintln("[SJServer] Dispataching: " + msg);
      
  		if (isDebug())
  		{
  			try
  			{
  				Thread.sleep(Util.DEBUG_DELAY);
  			}
  			catch (InterruptedException ie)
  			{
  				throw new RuntimeException(ie);
  			}
  		}
      
      return msg;
		}
		
		return null;
	}
	
	public int getPort()
	{
		throw new RuntimeException("[RMIServer] Port is not used.");
	}
	
	public boolean isDebug()
  {
  	return debug;
  }
  
  protected void debugPrintln(String m)
  {
  	Util.debugPrintln(debug, m);
  }
  
	public static void main(String[] args) throws Exception 
	{
  	boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
    
  	new RMIServerImpl(debug).run();	
	} 
}
