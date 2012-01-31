package sessionj.runtime2.transport;

import sessionj.runtime.*;
import sessionj.runtime2.net.SJComponent;
import sessionj.runtime2.net.SJComponentId;

/**
 * @author Raymond
 * 
 */
public interface SJTransport extends SJTransportComponent 
{
	public SJConnectionAcceptor openAcceptor(int port) throws SJIOException; // Transport-level port.	
	
	public SJConnection connect(String hostName, int port) throws SJIOException;

	public int sessionPortToSetupPort(int port);
	
	public boolean portInUse(int port);
	//public int getFreePort() throws SJIOException;
}
