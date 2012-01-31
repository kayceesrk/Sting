/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

/**
 * @author Raymond
 *
 */
public interface SJConnectionAcceptor
{
	SJConnection accept() throws SJIOException;
	void close();
	
	boolean interruptToClose();
	
	boolean isClosed();
	String getTransportName(); 
	SJTransport getTransport();
}
