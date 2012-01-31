package sessionj.runtime.transport.http;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.AbstractSJTransport;
import sessionj.runtime.transport.SJConnection;
import sessionj.runtime.transport.SJConnectionAcceptor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SJHTTP extends AbstractSJTransport {
	
	public static final String TRANSPORT_NAME = "sessionj.runtime.transport.http.SJHTTP";

	private static final int LOWER_PORT_LIMIT = 1024; 
	private static final int PORT_RANGE = 10000;
	
	public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) throws SJIOException{
		
		return new SJHTTPAcceptor(port, this);
	}
	
	/*public SJConnection connect(SJServerIdentifier si) throws SJIOException{
		
		return connect(si.getHostName(), si.getPort());
	}*/
	
	public SJConnection connect(String hostName, int port) throws SJIOException
	{
		try 
		{
			Socket conn = new Socket(hostName, port);
			
			return new SJHTTPConnection(conn, conn.getOutputStream(), conn.getInputStream(), this);
		} 
		catch (IOException ioe) 
		{
			throw new SJIOException(ioe);
		}
	}

    public boolean portInUse(int port){
		
		ServerSocket ss = null; 
		
		try
		{
			ss = new ServerSocket(port);
		}
		catch (IOException ioe)
		{
			return true;
		}
		finally
		{
			if (ss != null) 
			{
				try
				{
					ss.close();
				}
				catch (IOException ioe) { }					
			}
		}
		
		return false;

	}
	
	public int getFreePort() throws SJIOException{
		
		int start = new Random().nextInt(PORT_RANGE);
		int seed = start + 1;
		
		for (int port = seed % PORT_RANGE; port != start; port = seed++ % PORT_RANGE)  
		{
			if (!portInUse(port + LOWER_PORT_LIMIT))
			{
				return port + LOWER_PORT_LIMIT;
			}
		}
		
		throw new SJIOException("[" + getTransportName() + "] No free port available.");
	}
	
	public String getTransportName(){
		
		return TRANSPORT_NAME;
	}
	
	public String sessionHostToNegotiationHost(String hostName)
	{
		return hostName;
	}
	
	public int sessionPortToSetupPort(int port)
	{
		return port;
	}
}