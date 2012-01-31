package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;
/**
 * @author Raymond
 * 
 * FIXME: rename to SJChannel, and rename channels to services. Also, need an isClosed.  
 */
public interface SJConnection
{
	void disconnect();

	// A bit naive (wrt. to all or nothing written?). Should we only signal EOF through exceptions? 
	void writeByte(byte b) throws SJIOException;
	void writeBytes(byte[] bs) throws SJIOException;

	byte readByte() throws SJIOException;
	void readBytes(byte[] bs) throws SJIOException; // This is intended to block until the buffer is filled.
	
	void flush() throws SJIOException;
	
	String getHostName();
	int getPort();
	
	int getLocalPort();

	String getTransportName();
    SJTransport getTransport();

    boolean supportsBlocking();

	boolean arrived();

    //<By MQ>
    public byte peekByte() throws SJIOException;
    public void peekBytes(byte[] bs) throws SJIOException;
    //</By MQ>

}
