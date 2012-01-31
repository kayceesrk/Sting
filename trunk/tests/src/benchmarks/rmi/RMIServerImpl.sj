//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/rmi/RMIServerImpl.sj -d tests/classes/
//$ rmiregistry &
//$ java -cp tests/classes/ -Djava.rmi.server.codebase=file:///c:/cygwin/home/rh105/wrk/eclipse/sessionj-cvs/tests/classes/ -Djava.security.policy=c:/cygwin/home/rh105/wrk/eclipse/sessionj-cvs/tests/src/benchmarks/rmi/security.policy benchmarks.rmi.RMIServerImpl RMIBenchmarkObject

//vertex60% java -cp tests/classes -Djava.rmi.server.codebase=file:///homes/rh105/wrk/eclipse/sessionj-cvs/tests/classes/ -Djava.security.policy=file:///homes/rh105/wrk/eclipse/sessionj-cvs/tests/src/benchmarks/rmi/security.policy.foo benchmarks.rmi.RMIServerImpl RMIBenchmarkObject

package benchmarks.rmi;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

import benchmarks.BigObject;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer 
{
	private int bigObjectSize;
	
	private int run = 0;
	private int id = 0;

	private boolean foo = true;

	public RMIServerImpl() throws RemoteException 
	{
		super();
	}

	/*public void setBigObjectSize(int size) throws RemoteException 
	{
		this.bigObjectSize = size;
	}*/
	/*
	public BigObject getBigObject100(boolean b) throws RemoteException 
	{		
		//System.out.println("Served a client.");
		
		if (b)
		{
			if (foo)
			{
				//System.out.println("Run: " + run++);			
				
				foo = false;
			}
		
			return new BigObject(id++, 100 - 86);
		}
		else
		{
			return null;
		}
	}

	public BigObject getBigObject10000(boolean b) throws RemoteException 
	{		
		//System.out.println("Served a client.");
		
		if (b)
		{
			foo = true;
		
			return new BigObject(id++, 10000 - 86);
		}
		else
		{
			return null;
		}
	}
	*/
	
	public BinaryTree inc(BinaryTree theTree){
		
		theTree.inc();
		return theTree;
	}
	public static void main(String[] args) throws IOException 
	{
		RMIServer remoteObject = new RMIServerImpl();
		
		System.setSecurityManager(new RMISecurityManager());
		
		Registry registry = LocateRegistry.getRegistry();
		
		registry.rebind(args[0], remoteObject);
		
		//System.out.println("Server object exported to rmiregistry at localhost on standard port and bound to name: " + args[0]);
	}
	
	public BigObject getDummy(boolean b) throws RemoteException
	{				
		if (b)
		{
			return new BigObject(-1, 100 - 86);
		}
		else
		{
			return null;
		}	
	}
}
