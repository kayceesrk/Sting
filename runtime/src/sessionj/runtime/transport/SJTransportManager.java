/**
 * 
 */
package sessionj.runtime.transport;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * @author Raymond
 *
 */
abstract public class SJTransportManager
{
	public static final byte SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED = 31; // If the setup is already the most preferred Client transport.
	public static final byte SJ_CLIENT_TRANSPORT_NO_FORCE = 32; // If the setup isn't one of the Client transports.
	public static final byte SJ_CLIENT_TRANSPORT_NEGOTIATION_START = 33; // If the setup is one of the Client transports, but not the most preferred one.
	
	public static final byte SJ_SERVER_TRANSPORT_FORCE = 41; // Server doesn't have any other transports other than the setup. With SJ_CLIENT_TRANSPORT_NO_FORCE, negotiation has failed. 
	public static final byte SJ_SERVER_TRANSPORT_SUPPORTED = 42; // Server has other transports available, including the setup. With SJ_CLIENT_NEGOTIATION_NOT_NEEDED, negotiation not needed.
	public static final byte SJ_SERVER_TRANSPORT_NOT_SUPPORTED = 43; // Server has other transports available, but not including the setup.
	
	protected static final byte REUSE_SETUP_CONNECTION = 51;
	protected static final byte CLOSE_SETUP_CONNECTION = 52;	
	
	abstract public SJAcceptorThreadGroup openAcceptorGroup(int port, SJSessionParameters params) throws SJIOException; // should be session port?
	abstract public void closeAcceptorGroup(int port);
	
	abstract public SJConnection openConnection(String hostName, int port, SJSessionParameters params) throws SJIOException;
	abstract public void closeConnection(SJConnection conn);
	
	abstract public SJConnection openAuthenticatedConnection(String hostName, int port, SJSessionParameters params, String user, String pwd) throws SJIOException; // HACK: Nuno's SRP authenticated session connections hacked in for now.
	
	abstract public List<SJTransport> activeNegotiationTransports();

    /**
     * Ensures the transports designated by the letter codes given as parameter
     * are loaded, and returns a list with references to these transports.
     * The transport will be instantiated if required.
     * @param transportLetterCodes The letter codes for the required transports.
     * @return The list of transports that were just loaded.
     */
  //abstract public List<SJTransport> loadNegotiationTransports(String transportLetterCodes) throws SJIOException;
	//abstract public List<SJTransport> loadSessionTransports(String transportLetterCodes) throws SJIOException;
    
	abstract public List<SJTransport> loadNegotiationTransports(List<Class<? extends SJTransport>> transportLetterCodes) throws SJIOException;
	abstract public List<SJTransport> loadSessionTransports(List<Class<? extends SJTransport>> transportLetterCodes) throws SJIOException;	
	
	abstract public List<SJTransport> activeSessionTransports();

	
	abstract protected void registerConnection(SJConnection conn);
	
	abstract protected SJConnection clientNegotiation(SJSessionParameters params, String hostName, int port, List<SJTransport> ss, List<SJTransport> ts, List<String> tn, int boundedBufferSize) throws SJIOException;
	abstract protected boolean serverNegotiation(SJSessionParameters params, SJAcceptorThreadGroup atg, SJConnection conn) throws SJIOException;

    public abstract List<SJTransport> defaultSessionTransports();
    public abstract List<SJTransport> defaultNegotiationTransports();
    
  abstract public List<Class<? extends SJTransport>> defaultSessionTransportClasses();
  abstract public List<Class<? extends SJTransport>> defaultNegotiationTransportClasses();
}
