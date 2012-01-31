//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/c/Server.sj -d tests/classes/
package pldi.benchmarks.benchmark1.c;

import java.rmi.Remote;
import java.rmi.RemoteException;

import pldi.benchmarks.BinaryTree;

public interface Server extends Remote 
{	
	public BinaryTree inc(BinaryTree theTree) throws RemoteException;
	public BinaryTree dummy(BinaryTree bt) throws RemoteException;
	public void end() throws RemoteException;
	public void reset() throws RemoteException;
	public void shutdown() throws RemoteException;
}
