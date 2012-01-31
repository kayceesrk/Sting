/**
 * 
 */
package sessionj.runtime2.transport;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;

/**
 * @author Raymond
 *
 */
public interface SJConnectionAcceptor
{
	public SJConnection accept() throws SJIOException;
	public void close();
	
	public int getPort();
	
	public boolean isClosed();
	public boolean interruptToClose();	
	
	public SJComponentId getTransportId();
}
