/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;
import static sessionj.runtime.util.SJRuntimeUtils.closeStream;

import java.io.*;

/**
 * @author Raymond
 *
 * FIXME: the streams have become a bit mixed up: the Data streams can interfere with whoever is using the get...Stream methods. but as long as former is only used by e.g. transport negotiation and latter by SJSerializer after session starts, it may be OK
 */
abstract public class SJStreamConnection extends AbstractSJConnection {
	private final OutputStream os;
	private final InputStream is;
	
	private final DataOutputStream dos;
	private final DataInputStream dis;

    protected SJStreamConnection(InputStream in, OutputStream out, SJTransport transport)
    // HACK: to avoid deadlock when setting up I/O streams.
	// ie. order of parameters, in before out
    {
        super(transport);
        os = out; // Used by SJStreamSerializer
        is = in;
        
        dis = new DataInputStream(in); // Used by e.g. transport negotiation // FIXME: does it affect performance that the SJStreamSerializer wraps around the Data streams rather than the underlying streams directly?
        dos = new DataOutputStream(out);
	}
	
	public OutputStream getOutputStream()
	{
		//return dos;
		return os;
	}
	
	public InputStream getInputStream()
	{
		//return dis;
		return is;
	}
	
	public void disconnect() //throws SJIOException 
	{
		try { closeStream(dos); } catch (IOException ignored) {}
		try { closeStream(dis); } catch (IOException ignored) {}
		
		try { closeStream(os); } catch (IOException ignored) {}
		try { closeStream(is); } catch (IOException ignored) {}
	}

	public void writeByte(byte b) throws SJIOException
	{
		//throw new RuntimeException("[SJStreamConnection] These methods should not be used directly."); // Used by e.g. transport negotiation
		try
		{
			dos.writeByte(b);
			//dos.flush(); // Let upper layers sort out flushing (e.g. transport negotiation --- not SJStreamSerializer here)
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	public void writeBytes(byte[] bs) throws SJIOException
	{
		//throw new RuntimeException("[SJStreamConnection] These methods should not be used directly.");
		try
		{
			dos.write(bs, 0, bs.length);
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	public byte readByte() throws SJIOException
	{
		//throw new RuntimeException("[SJStreamConnection] These methods should not be used directly.");
		try
		{
			return dis.readByte(); // Can raise EOFException. Only the read operation that returns number of bytes read can signal via a -1 in such a case.
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}		
	}
	
	public void readBytes(byte[] bs) throws SJIOException
	{
		//throw new RuntimeException("[SJStreamConnection] These methods should not be used directly.");
		try
		{
			dis.readFully(bs); // Here, ATI different to standard TCP API. 
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}	
	}	
	
	public void flush() throws SJIOException
	{
		//throw new RuntimeException("[SJStreamConnection] These methods should not be used directly.");
		try
		{		
			dos.flush();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}				
	}

}
