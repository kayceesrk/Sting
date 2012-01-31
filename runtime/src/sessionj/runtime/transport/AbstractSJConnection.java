package sessionj.runtime.transport;
import sessionj.runtime.SJIOException; //<By MQ>

public abstract class AbstractSJConnection extends AbstractWithTransport implements SJConnection {
    protected AbstractSJConnection(SJTransport transport) {
	    super(transport);
    }
	
    public boolean supportsBlocking() {
        return true;
    }

	public boolean arrived() {
		throw new UnsupportedOperationException("Does not support non-blocking mode");
	}

    //<By MQ>
    public byte peekByte() throws SJIOException
    {
	throw new UnsupportedOperationException("Does not support peek operation");
    }
    public void peekBytes(byte[] bs) throws SJIOException
    {
        throw new UnsupportedOperationException("Does not support peek operation");
    }
    //</By MQ>
}
