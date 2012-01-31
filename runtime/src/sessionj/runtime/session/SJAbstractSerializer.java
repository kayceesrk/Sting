package sessionj.runtime.session;

import sessionj.runtime.transport.SJConnection;
import sessionj.runtime.SJIOException; //<By MQ>
/**
 * Defines the methods needed for a Serializer, so different serialization
 * protocols can be used if necessary
 * 
 * @see SJDefaultSerializer
 * @author Raymond
 *
 */
abstract public class SJAbstractSerializer implements SJSerializer 
{	
	protected static final byte SJ_CONTROL = 1;
	//private static final byte SJ_DELEGATION = 2;
	
	protected static final byte SJ_OBJECT = 12; // SJ_Message flags are 2, 3, etc.
	protected static final byte SJ_REFERENCE = 13;
	protected static final byte SJ_BYTE = 14;
	protected static final byte SJ_INT = 15;
	protected static final byte SJ_BOOLEAN = 16;
	protected static final byte SJ_DOUBLE = 17;
	
	protected SJConnection conn; // Could be moved into an abstract super class (with constructor), but would be tying it to Java serialization.
	
	protected boolean isClosed = false; // FIXME: this is currently assigned to by subclasses (on close).
	
	protected SJAbstractSerializer(SJConnection conn)
	{
		this.conn = conn;
	}

    //abstract protected boolean hasBoundedBuffer();

    //<By MQ>
    public byte peekByte() throws SJIOException
    {
	//	if(conn instanceof SJManualTCPConnection)
	    return conn.peekByte();
	    //else
	    //throw new SJIOException("SJAbstractSerializer: Unsupported operation.");
    }

    public void peekBytes(byte[] bs) throws SJIOException
    {
        //if(conn instanceof SJManualTCPConnection)
            conn.peekBytes(bs);
	    //else
            //throw new SJIOException("SJAbstractSerializer: Unsupported operation.");
    }

    public boolean arrived()
    {
        return conn.arrived();
    }
    //</By MQ>
    public boolean isClosed()
	{
		return isClosed;
	}
	
	public SJConnection getConnection()
	{
		return conn;
	}

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' + conn + '}';
    }
}
