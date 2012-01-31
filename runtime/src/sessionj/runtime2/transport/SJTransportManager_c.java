/**
 * 
 */
package sessionj.runtime2.transport;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.http.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.udp.*;

import static sessionj.runtime.util.SJRuntimeUtils.*;

/**
 * @author Raymond
 *
 */
public class SJTransportManager_c extends SJTransportManager
{	
	//private static final boolean debug = true;
	private static final boolean debug = false;
	
	private List<SJTransport> setups = new LinkedList<SJTransport>(); // These may need some synchronisation (getters and setters are currently public and non-defensive).
	private List<SJTransport> transports = new LinkedList<SJTransport>(); 
	
	//private HashMap<Integer, SJAcceptorThreadGroup> acceptorGroups = new HashMap<Integer, SJAcceptorThreadGroup>(); 
	
	/*private HashMap<String, SJConnection> connections = new HashMap<String, SJConnection>();		
	private HashMap<SJConnection, Integer> sessions = new HashMap<SJConnection, Integer>();*/
	
	public SJTransportManager_c() 
	{ 
		defaultSetups();
		defaultTransports();
	}	
	
	private void defaultSetups()
	{

	}
	
	private void defaultTransports()
	{
	
	}
	
	public String sessionHostToSetupHost(String hostAddress)
	{
		return hostAddress;
	}
	
	//public SJAcceptorThreadGroup openAcceptorGroup(int port, SJSessionParameters params) throws SJIOException 		
	//private SJAcceptorThreadGroup openAcceptorGroup(int port, List<SJTransport> ss, List<SJTransport> ts, List<String> sn) throws SJIOException // Synchronized where necessary from calling scope. 	
	//public void closeAcceptorGroup(int port)
	
	public SJConnection openConnection(String hostAddress, int port) throws SJIOException
	{			
		return null;
	}
	
	public void closeConnection(SJConnection conn) // FIXME: add in close delay to allow connection reuse.
	{
		conn.disconnect();
	}
	
	public List<SJTransport> getRegisteredSetups() 
	{
		return null;
	}
	
	public void configureSetups(List<SJTransport> setups)
	{
		
	}	
	
	public List<SJTransport> getRegisteredTransports()
	{
		return null;
	}
	
	public void configureTransports(List<SJTransport> transports)
	{
		
	}			
	
	//protected void registerConnection(SJConnection conn)
}
