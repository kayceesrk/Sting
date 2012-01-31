/**
 * 
 */
package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.SJConnection;

/**
 * @author Raymond
 *
 * This serializer is intended for use with SJNonSjCompatibilityProtocols. TODO: need similar for e.g. binary and XML.
 *
 * In principle, this serializer as a standalone component could implement the session protocols like branching, iteration and delegation, similarly to SJManualSerializer. But in such a case, we can just use SJManualSerializer. 
 *
 */
public class SJCustomSerializer extends SJAbstractSerializer
{
	private SJCustomMessageFormatter cmf;
	
	//private final BufferedWriter bw; 
	//private final BufferedReader br;
	
	public SJCustomSerializer(SJCustomMessageFormatter cmf, SJConnection conn) throws SJIOException
	{
		super(conn);
		
		cmf.bindConnection(conn); 
		
		this.cmf = cmf;
		
		/*Charset cs = Charset.forName(SJConstants.CHARSET_UTF8);
		
		if (conn instanceof SJStreamConnection)
		{
			SJStreamConnection sc = (SJStreamConnection) conn;

			//if (sc.getRole() == Role.ACCEPTOR)
			{
				bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream(), cs)); 
				br = new BufferedReader(new InputStreamReader(sc.getInputStream(), cs));
			}
			/*else
			{					
				br = new BufferedReader(new InputStreamReader(sc.getInputStream()));				
				bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())); 
			}*
		}
		else
		{
			throw new SJIOException("[SJUtf8Serializer] Connection type not supported: " + conn);
		}*/
	}
	
	public boolean zeroCopySupported()
	{
		//return conn instanceof SJLocalConnection; // FIXME: can be a local connection.
		return false; // Disable reference passing for now (serializer won't - or shouldn't - try to use reference passing if this returns false).
	}
	
	public void close()
	{
		/*if (!isClosed) // Need synchro.?
		{
			isClosed = true;
			
			try { closeWriter(bw); } catch (IOException ioe) { }
			try { closeReader(br); } catch (IOException ioe) { }
		}*/
		
		if (!isClosed) // Duplicated from SJManualSerializer.
		{
			isClosed = true;
			
			try
			{
				if (conn != null) 
				{
					conn.flush();				
				}
			} catch (SJIOException ignored) { }
		}				
	}
	
	public void writeObject(Object o) throws SJIOException
	{
		/*try
		{
			bw.write(m);
			bw.flush();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}*/
		
		cmf.writeMessage(o);
	}	
		
	public void writeByte(byte b) throws SJIOException
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation: " + b);
	}
	
	public void writeInt(int i) throws SJIOException
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation: " + i);
	}
	
	public void writeBoolean(boolean b) throws SJIOException
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation: " + b);
	}
	
	public void writeDouble(double d) throws SJIOException
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation: " + d);
	}
	
	public Object readObject() throws SJIOException, ClassNotFoundException
	{
		return cmf.readNextMessage();
	}
	
	public byte readByte() throws SJIOException, SJControlSignal
	{	
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}
	
	public int readInt() throws SJIOException, SJControlSignal
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}

  public boolean readBoolean() throws SJIOException, SJControlSignal
	{
  	throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}
	
	public double readDouble() throws SJIOException, SJControlSignal
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}
	
	public void writeReference(Object o) throws SJIOException
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}
	
	public Object readReference() throws SJIOException, SJControlSignal
	{			
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");				
	}

	public void writeControlSignal(SJControlSignal cs) throws SJIOException
	{
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}

	public SJControlSignal readControlSignal() throws SJIOException
	{			
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}
	
	public SJMessage nextMessage() throws SJIOException, ClassNotFoundException//, SJControlSignal
	{		
		//return cmf.readNextMessage();
		throw new SJIOException("[SJCustomSerializer] Unsupported operation.");
	}
}
