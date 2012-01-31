package sessionj.runtime2.transport;

import java.io.*;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;

/**
 * @author Raymond
 * 
 */
public interface SJConnection 
{
	public void disconnect()/* throws SJIOException*/;

	public void writeByte(byte b) throws SJIOException;
	public void writeBytes(byte[] bs) throws SJIOException;

	public byte readByte() throws SJIOException;
	public void readBytes(byte[] bs) throws SJIOException; // Block until filled.
	
	public void flush() throws SJIOException;
	
	public boolean isClosed();
		
	public String getHostAddress();
	//public String getHostName();
	public int getPort();	
	public int getLocalPort();
	
	public SJComponentId getTransportId();
}
