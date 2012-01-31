package sessionj.runtime.transport.httpservlet;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class SJHTTPServlet extends AbstractSJTransport
{	
	public static final String TRANSPORT_NAME = "sessionj.runtime.transport.http.SJHTTPServlet";

	public static final int TCP_PORT_MAP_ADJUST = 2;
	
	private static final int LOWER_PORT_LIMIT = 1024; 
	private static final int PORT_RANGE = 10000;
	
	public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) throws SJIOException
	{	
		return new SJHTTPServletAcceptor(port, this);
	}
		
	public SJConnection connect(String hostName, int port) throws SJIOException // FIXME: the transport manager will run the negotiation protocol after the connection is established - the servlet proxy needs to take care of it (not just forward it to the Server). // FIXME: how to accommodate alternative transport preferences after setup phase? 
	{
		return new SJHTTPServletConnection(hostName, port, this);
	}

    public boolean portInUse(int port)
	{	
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
	
	public int getFreePort() throws SJIOException
	{
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
		return port + TCP_PORT_MAP_ADJUST;
	}
}