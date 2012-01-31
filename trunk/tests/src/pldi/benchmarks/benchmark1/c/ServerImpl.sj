//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/c/ServerImpl.sj -d tests/classes/
//$ rmiregistry &
//$ bin/sessionj -cp tests/classes/ -Djava.rmi.server.codebase=file:///c:/cygwin/home/rh105/wrk/eclipse/sessionj-cvs/tests/classes/ -Djava.security.policy=file:///c:/cygwin/home/rh105/wrk/eclipse/sessionj-cvs/tests/src/pldi/benchmarks/benchmark1/c/security.policy.windows pldi.benchmarks.benchmark1.c.ServerImpl false RMIBenchmarkObject

//% bin/sessionj -cp tests/classes/ -Djava.rmi.server.codebase=file:///homes/rh105/wrk/eclipse/sessionj-cvs/tests/classes/ -Djava.security.policy=file:///homes/rh105/wrk/eclipse/sessionj-cvs/tests/src/pldi/benchmarks/benchmark1/c/security.policy.linux pldi.benchmarks.benchmark1.c.ServerImpl false RMIBenchmarkObject

package pldi.benchmarks.benchmark1.c;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

import pldi.benchmarks.BinaryTree;

public class ServerImpl extends UnicastRemoteObject implements Server 
{
	private static Server remObj;

	private boolean debug;
	private int counter = 0;
	
	public ServerImpl(boolean debug) throws RemoteException 
	{
		super();
		
		this.debug = debug;
	}
	
	public BinaryTree inc(BinaryTree bt)
	{	
		bt.inc();
		
		if (debug)
		{
			bt.println();
		}
		
		return bt;
	}
	
	public BinaryTree dummy(BinaryTree bt) throws RemoteException
	{				
		return bt;
	}
	
	public void end() throws RemoteException
	{
		System.out.println("Finished run: " + counter++);
		
		if (debug)
		{
			System.out.println();
		}
	}
	
	public void reset() throws RemoteException
	{
		counter = 0;	
	}
	
	public void shutdown() throws RemoteException
	{				
		UnicastRemoteObject.unexportObject(remObj, true);
		
		System.exit(0);
	}
	
	public static void main(String[] args) throws IOException 
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String name = args[1];
		
		remObj = new ServerImpl(debug);
		
		System.setSecurityManager(new RMISecurityManager());
		
		Registry registry = LocateRegistry.getRegistry();
		
		registry.rebind(name, remObj);
	}
}
