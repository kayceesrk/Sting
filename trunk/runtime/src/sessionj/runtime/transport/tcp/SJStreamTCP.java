package sessionj.runtime.transport.tcp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

class SJStreamTCPAcceptor extends AbstractWithTransport implements SJConnectionAcceptor
{
	private final ServerSocket ss;
	
    SJStreamTCPAcceptor(int port, SJTransport transport) throws SJIOException
	{
		super(transport);
        try
		{
			ss = new ServerSocket(port); // Didn't bother to explicitly check portInUse.
		}
		catch (IOException ioe)
		{
			throw new SJIOException("Could not open StreamTCPAcceptor on port: " + port, ioe);
		}
	}
	
	public SJStreamTCPConnection accept() throws SJIOException
	{
		try
		{
			if (ss == null)
			{
				throw new SJIOException('[' + getTransportName() + "] Connection acceptor not open.");
			}
			
			Socket s = ss.accept();
			
			s.setTcpNoDelay(SJStreamTCP.TCP_NO_DELAY);
			
			return new SJStreamTCPConnection(s, s.getInputStream(), s.getOutputStream(), getTransport());
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	public void close()
	{	
		try 
		{ 
			if (ss != null)
			{
				ss.close(); 
			}
		}
		catch (IOException ignored) { }
	}
	
	public boolean interruptToClose()
	{
		return false;
	}
	
	public boolean isClosed()
	{
		return ss.isClosed();
	}
}

class SJStreamTCPConnection extends SJStreamConnection
{
	private final Socket s;
	
	protected SJStreamTCPConnection(Socket s, InputStream is, OutputStream os, SJTransport transport) {
		super(is, os, transport);
		
		this.s = s;
	}
	
	public void disconnect() //throws SJIOException 
	{
		super.disconnect();				
		
		try 
		{ 
			if (s != null)
			{
				s.close(); 			
			}
		}
		catch (IOException ioe) { }
	}

	public String getHostName()
	{
		return s.getInetAddress().getHostName();
	}
	
	public int getPort()
	{
		return s.getPort();
	}
	
	public int getLocalPort()
	{
		return s.getLocalPort();
	}
	
	public String getTransportName()
	{
		return SJStreamTCP.TRANSPORT_NAME;
	}
}

/**
 * @author Raymond
 *
 */
public class SJStreamTCP extends AbstractSJTransport
{
	public static final String TRANSPORT_NAME = "sessionj.runtime.transport.tcp.SJStreamTCP";

	public static final int TCP_PORT_MAP_ADJUST = 0;
	
	protected static final boolean TCP_NO_DELAY = true;
	
	private static final int LOWER_PORT_LIMIT = 1024; 
	private static final int PORT_RANGE = 65535 - 1024;

    public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) throws SJIOException
	{
		return new SJStreamTCPAcceptor(port, this);
	}
	
	public SJStreamTCPConnection connect(String hostName, int port) throws SJIOException // Transport-level values.
	{
		try 
		{
			Socket s = new Socket(hostName, port);
			
			s.setTcpNoDelay(TCP_NO_DELAY);
			
			return new SJStreamTCPConnection(s, s.getInputStream(), s.getOutputStream(), this); // Have to get I/O streams here for exception handling.
		} 
		catch (IOException ioe) 
		{
			throw new SJIOException(ioe);
		}
	}

    public boolean portInUse(int port)
	{
        return isTCPPortInUse(port);
	}

	public int getFreePort() throws SJIOException
	{
        return getFreeTCPPort(getTransportName());
	}

    static int getFreeTCPPort(String transportName) throws SJIOException {
        int start = new Random().nextInt(PORT_RANGE);
        int seed = start + 1;

        for (int port = seed % PORT_RANGE; port != start; port = seed++ % PORT_RANGE)
        {
            if (!isTCPPortInUse(port + LOWER_PORT_LIMIT))
            {
                return port + LOWER_PORT_LIMIT;
            }
        }

        throw new SJIOException('[' + transportName + "] No free port available.");
    }

    public String getTransportName()
	{
		return TRANSPORT_NAME;
	}
	
	public String sessionHostToNegotiationHost(String hostName)
	{
		return hostName;
	}
	
	public int sessionPortToSetupPort(int port) // Maybe can factor out to an abstract TCP-based parent class.
	{
		return port + TCP_PORT_MAP_ADJUST;
	}

    static boolean isTCPPortInUse(int port) {
        ServerSocket ss = null;

        try {
            ss = new ServerSocket(port);
        } catch (IOException ignored) {
            return true;
        } finally {
            if (ss != null) try {
                ss.close();
            } catch (IOException ignored) { }
        }

        return false;
    }
}
