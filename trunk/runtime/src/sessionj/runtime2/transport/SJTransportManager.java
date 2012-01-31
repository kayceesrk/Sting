/**
 * 
 */
package sessionj.runtime2.transport;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * @author Raymond
 *
 */
abstract public class SJTransportManager
{
	/*public static final byte SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED = 31; // If the setup is already the most preferred Client transport.
	public static final byte SJ_CLIENT_TRANSPORT_NO_FORCE = 32; // If the setup isn't one of the Client transports.
	public static final byte SJ_CLIENT_TRANSPORT_NEGOTIATION_START = 33; // If the setup is one of the Client transports, but not the most preferred one.
	
	public static final byte SJ_SERVER_TRANSPORT_FORCE = 41; // Server doesn't have any other transports other than the setup. With SJ_CLIENT_TRANSPORT_NO_FORCE, negotiation has failed. 
	public static final byte SJ_SERVER_TRANSPORT_SUPPORTED = 42; // Server has other transports available, including the setup. With SJ_CLIENT_NEGOTIATION_NOT_NEEDED, negotiation not needed.
	public static final byte SJ_SERVER_TRANSPORT_NOT_SUPPORTED = 43; // Server has other transports available, but not including the setup.
	
	protected static final byte REUSE_SETUP_CONNECTION = 51;
	protected static final byte CLOSE_SETUP_CONNECTION = 52;*/	
	
	//abstract public SJAcceptorThreadGroup openAcceptorGroup(int port, SJSessionParameters params) throws SJIOException;
	//abstract public void closeAcceptorGroup(int port);
	
	abstract public String sessionHostToSetupHost(String hostAddress);
	
	abstract public SJConnection openConnection(String hostAddress, int port) throws SJIOException;
	abstract public void closeConnection(SJConnection conn);
	
	//abstract protected void registerConnection(SJConnection conn);
	
	//abstract protected boolean serverNegotiation(SJAcceptorThreadGroup atg, SJConnection conn) throws SJIOException;
	//abstract protected SJConnection clientNegotiation(String hostName, int port, List<SJTransport> ss, List<SJTransport> ts, List<String> tn) throws SJIOException;
	
	abstract public List<SJTransport> getRegisteredSetups();
	abstract public void configureSetups(List<SJTransport> setups);
	
	abstract public List<SJTransport> getRegisteredTransports();
	abstract public void configureTransports(List<SJTransport> transports);	
}
