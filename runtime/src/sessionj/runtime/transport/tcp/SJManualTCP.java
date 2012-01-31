package sessionj.runtime.transport.tcp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;
import static sessionj.runtime.util.SJRuntimeUtils.closeStream;
import sessionj.runtime.util.SJRuntimeUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

class SJManualTCPAcceptor extends AbstractWithTransport implements SJConnectionAcceptor
{
	private final ServerSocket ss;

    SJManualTCPAcceptor(int port, SJTransport transport) throws SJIOException
	{
		super(transport);
        try
		{
			ss = new ServerSocket(port); // Didn't bother to explicitly check portInUse.
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	public SJManualTCPConnection accept() throws SJIOException
	{
		try
		{
			if (ss == null)
			{
				throw new SJIOException("[" + getTransportName() + "] Connection acceptor not open.");
			}
			
			Socket s = ss.accept();
			
			s.setTcpNoDelay(SJManualTCP.TCP_NO_DELAY);
			
			return new SJManualTCPConnection(s, s.getInputStream(), s.getOutputStream(), getTransport());
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
		catch (IOException ioe) { }
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

class SJManualTCPConnection extends AbstractSJConnection
{
	private final Socket s;
	
	private final DataOutputStream dos;
	private final DataInputStream dis;
        private final BufferedInputStream bis;  //<By MQ> I added this to allow peek functionality

    SJManualTCPConnection(Socket s, OutputStream os, InputStream is, SJTransport transport) {
        super(transport);
        this.s = s;
        dos = new DataOutputStream(os);
        dis = new DataInputStream(is);
	bis = new BufferedInputStream(dis); //<By MQ>
	}

	SJManualTCPConnection(Socket s, InputStream is, OutputStream os, SJTransport transport) {
        super(transport);
        this.s = s;
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);		
	bis = new BufferedInputStream(dis); //<By MQ>
	}
	
    //<By MQ>
    @Override
    public boolean arrived()
    {
	try
	{
	    return bis.available() > 0;
	}
	catch (IOException ioe)
	{
	    System.out.println("[SJManualTCPConnection].arrived(): " + ioe);
	}
	return false;
    }

    public byte peekByte() throws SJIOException
    {
	if(arrived() == false)
	    throw new SJIOException("SJManualTCPConnection: No data available.");
	int available = 0;
	try
	{
	    available = bis.available();
	}
	catch(Exception ex){}
	//System.out.println("Data available, peeking byte! Available: " + available);
	byte b;
	try
	{	    
	    bis.mark(1);
	    b = (byte) bis.read();
	    bis.reset();
	}
	catch (IOException ioe)
	{
	    throw new SJIOException(ioe);
	}
	try
	    {
		available = bis.available();
	    }
	catch(Exception ex){}
	//System.out.println("Exiting peekByte(). available: " + available);
	return b;
    }

    public void peekBytes(byte[] bs) throws SJIOException
    {
	int available = 0;
	try
	{
	    available = bis.available();
	}
	catch(IOException ex) 
	{
	    System.out.println("[SJManualTCPConnection].peekBytes(): " + ex);
	}

	if(available < bs.length)
	    throw new SJIOException("SJManualTCPConnection: Not enough data available, required: " + bs.length + ", available: " + available);
	//System.out.println("Data available, peeking bytes!");
	try
	{
	    //BufferedInputStream bis = new BufferedInputStream(dis);
	    bis.mark(bs.length);
	    bis.read(bs, 0, bs.length);
	    bis.reset();
	}
	catch (IOException ioe)
	{
	    throw new SJIOException(ioe);
	}
	try
            {
                available = bis.available();
            }
        catch(Exception ex){}
        //System.out.println("Exiting peekBytes(). available: " + available);
    }
    //</By MQ>
	public void disconnect() //throws SJIOException 
	{
		try { closeStream(dos); } catch (IOException ioe) { }		
		try { closeStream(dis); } catch (IOException ioe) { }
		try { closeStream(bis); } catch (IOException ioe) { } //<By MQ>
		
		try 
		{ 
			if (s != null)
			{
				s.close(); 			
			}
		}
		catch (IOException ioe) { }
	}

	public void writeByte(byte b) throws SJIOException
	{
		try
		{
			dos.writeByte(b);
			//dos.flush(); // FIXME: since ATI exports flush, we shouldn't do this implicitly (no point to ATI flush otherwise); flush should be done by e.g. SJManualSerializer
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	public void writeBytes(byte[] bs) throws SJIOException
	{
		try
		{
			dos.write(bs);
			//dos.flush();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	public byte readByte() throws SJIOException
	{
		try
		{
		    return (byte) bis.read();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}		
	}
	
	public void readBytes(byte[] bs) throws SJIOException
	{
		try
		{
		    //dis.readFully(bs);
		    bis.read(bs);
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}			
	}
	
	public void flush() throws SJIOException
	{
		try
		{
			dos.flush();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}			
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

    @Override
    public String toString() {
        return "SJManualTCPConnection{" + s + '}';
    }
}

/**
 * @author Raymond
 *
 */
public class SJManualTCP extends AbstractSJTransport
{
	public static final String TRANSPORT_NAME = "sessionj.runtime.transport.tcp.SJManualTCP";

	public static final int TCP_PORT_MAP_ADJUST = 20; // FIXME: sort out port potential mapping clashes better.

	protected static final boolean TCP_NO_DELAY = true;
    private static final Logger log = SJRuntimeUtils.getLogger(SJManualTCP.class);

    public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) throws SJIOException
	{
		return new SJManualTCPAcceptor(port, this);
	}
	
	public SJManualTCPConnection connect(String hostName, int port) throws SJIOException // Transport-level values.
	{

        try {
            log.finer("Opening socket to: " + hostName + ':' + port);
            Socket s = new Socket(hostName, port);
            s.setTcpNoDelay(TCP_NO_DELAY);
			
			return new SJManualTCPConnection(s, s.getOutputStream(), s.getInputStream(), this); // Have to get I/O streams here for exception handling.

		} catch (IOException ioe) {
			throw new SJIOException(ioe);
		} 
	}

    public boolean portInUse(int port)
	{
        return SJStreamTCP.isTCPPortInUse(port);
	}
	
	public int getFreePort() throws SJIOException
	{
        return SJStreamTCP.getFreeTCPPort(getTransportName());
	}
	
	public String getTransportName()
	{
		return TRANSPORT_NAME;
	}
	
	public String sessionHostToNegotiationHost(String hostName)
	{
		return hostName;
	}
	
	public int sessionPortToSetupPort(int port)
	{
		return port + TCP_PORT_MAP_ADJUST; // To be compatible with SJStreamTCP.
	}
}
