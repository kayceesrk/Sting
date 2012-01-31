package thesis.benchmark.bmark1.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import thesis.benchmark.ServerMessage;

public interface RMIServer extends Remote 
{
	static final String RMI_SERVER_OBJECT = "RMI_SERVER_OBJECT"; 
	
	void init() throws RemoteException;
	void setServerMessageSize(int serverMessageSize) throws RemoteException;
	ServerMessage getServerMessage(boolean flag) throws RemoteException;
	void close() throws RemoteException;
}
