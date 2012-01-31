package benchmarks.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import benchmarks.BigObject;

public interface RMIServer extends Remote 
{
	//public void setBigObjectSize(int size) throws RemoteException;
	//public BigObject getBigObject100(boolean b) throws RemoteException;
	//public BigObject getBigObject10000(boolean b) throws RemoteException;
	
	public BinaryTree inc(BinaryTree theTree) throws RemoteException;
	
	public BigObject getDummy(boolean b) throws RemoteException;
}
