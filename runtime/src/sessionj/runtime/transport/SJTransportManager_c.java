/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.session.SJCompatibilityMode;
import sessionj.runtime.transport.sharedmem.SJBoundedFifoPair;
import static sessionj.runtime.util.SJRuntimeUtils.*;
import sessionj.runtime.util.SJRuntimeUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author Raymond
 *
 */
public class SJTransportManager_c extends SJTransportManager
{
    private static final Logger log = SJRuntimeUtils.getLogger(SJTransportManager_c.class);

	private static final String DEFAULT_SETUPS_PROPERTY = "sessionj.transports.negotiation";
    private static final String DEFAULT_TRANSPORTS_PROPERTY = "sessionj.transports.session";
	
	private final Map<Integer, SJAcceptorThreadGroup> acceptorGroups = new HashMap<Integer, SJAcceptorThreadGroup>();

    private final TransportPreferenceList negotiationTransports; // Used to be called setups.
    private final TransportPreferenceList sessionTransports; // Used to be called just transports.

    public SJTransportManager_c() throws SJIOException {
        /*String defNegotiationTr = getDefault(DEFAULT_SETUPS_PROPERTY, "fs"); // Use "fs" if the system property was not specified to be something else.
        String defSessionTr = getDefault(DEFAULT_TRANSPORTS_PROPERTY, "fs");
        
        // Important: the TransportPreferenceList instances need to share that map.
        Map<Character, SJTransport> activeTransports = new HashMap<Character, SJTransport>();        
        negotiationTransports = new TransportPreferenceList(activeTransports, defNegotiationTr);
        sessionTransports = new TransportPreferenceList(activeTransports, defSessionTr);*/
        
    		// Allow some dependency here on the "letter code conventions" purely to make specifying runtime parameters more convenient. 
    		List<Class<? extends SJTransport>> defNegotiationTr = SJTransportUtils.parseTransportFlags(getDefault(DEFAULT_SETUPS_PROPERTY)); 
    		List<Class<? extends SJTransport>> defSessionTr = SJTransportUtils.parseTransportFlags(getDefault(DEFAULT_TRANSPORTS_PROPERTY));
    	
    		Map<Class<? extends SJTransport>, SJTransport> activeTransports = new HashMap<Class<? extends SJTransport>, SJTransport>();   
    		
        negotiationTransports = new TransportPreferenceList(activeTransports, defNegotiationTr);
        sessionTransports = new TransportPreferenceList(activeTransports, defSessionTr);

        log.finer("Default negotiation transports: " + defNegotiationTr
            + ": "+ negotiationTransports.getActive());
        log.finer("Default session transports: " + defSessionTr
            + ": " + sessionTransports.getActive());
    }

    //private String getDefault(String key, String fallback) {
    private String getDefault(String key) {
        String s = System.getProperty(key, "d"); // FIXME: factor out "d" constant (should be localised in SJTransportUtils). In principle, we should not be using "d" here but rather a proper default transport class list.
    		//String s = System.getProperty(key, "s"); // RAY: removed SJFifoPair from default transports for now.
        //if (s.equals("d")) s = fallback;
        return s;
    }

   public List<Class<? extends SJTransport>> defaultSessionTransportClasses() {
      return sessionTransports.defaultTransportClasses();
  }

  public List<Class<? extends SJTransport>> defaultNegotiationTransportClasses() {
      return negotiationTransports.defaultTransportClasses();
  }    
    
    public List<SJTransport> defaultSessionTransports() {
        return sessionTransports.defaultTransports();
    }

    public List<SJTransport> defaultNegotiationTransports() {
        return negotiationTransports.defaultTransports();
    }

    //public List<SJTransport> loadSessionTransports(String transportLetterCodes) throws SJIOException {
    public List<SJTransport> loadSessionTransports(List<Class<? extends SJTransport>> transportLetterCodes) throws SJIOException {
        return sessionTransports.loadTransports(transportLetterCodes);
    }

    //public List<SJTransport> loadNegotiationTransports(String transportLetterCodes) throws SJIOException {    	
    	public List<SJTransport> loadNegotiationTransports(List<Class<? extends SJTransport>> transportLetterCodes) throws SJIOException {
        return negotiationTransports.loadTransports(transportLetterCodes);
    }

    /*// The original intention was that these "letter codes" are not fundamental enough to be directly defined by the SJTransportManager. 
    static SJTransport createTransport(char code) throws SJIOException { 
        switch (code) {
            case 'f':
                return new SJFifoPair();
            case 'b':
                return new SJBoundedFifoPair();
            case 's':
                return new SJStreamTCP();
            case 'a':
                try {
                    return new SJAsyncManualTCP();
                } catch (IOException e) {
                    throw new SJIOException(e);
                }
            case 'm':
                return new SJManualTCP();
            case 'h':
                return new SJHTTPServlet();
        }

        throw new SJIOException("Unsupported transport code: " + code);
    }*/

    public SJAcceptorThreadGroup openAcceptorGroup(int port, SJSessionParameters params) throws SJIOException 
	{
        List<SJTransport> negTrans = params.getNegotiationTransports();
        List<SJTransport> sessTrans = params.getSessionTransports();
        
        return openAcceptorGroup(port, negTrans, sessTrans, params);
	}

    private List<String> transportNames(Iterable<SJTransport> ss) {
        // Retrieve negotiation transport names.
        List<String> sn = new LinkedList<String>();
        for (SJTransport t : ss) sn.add(t.getTransportName());
        return sn;
    }
    
	private SJAcceptorThreadGroup openAcceptorGroup(int port, Iterable<SJTransport> negotiationTrans, Iterable<SJTransport> sessionTrans, SJSessionParameters params) throws SJIOException // Synchronized where necessary from calling scope.
	{
		Collection<String> negotiationNames = transportNames(negotiationTrans);
		if (log.isLoggable(Level.FINER))
			log.finer("Openening acceptor group, port: " + port + ", negotiation: " + negotiationTrans + " session: " + sessionTrans + ", params: " + params);

        synchronized (acceptorGroups)
		{								
			if (acceptorGroups.keySet().contains(port)) 
            // FIXME: checks this transport manager hasn't already used the session p, but 
            // the session p could be used by another transport manager on the same machine,
            // or the underlying transport p might not be available.
			{
				throw new SJIOException("[SJTransportManager_c] Port already in use: " + port);
			}
			
			String name = ((Integer) port).toString(); // FIXME: Factor out threadgroup name scheme.
			
			SJAcceptorThreadGroup atg = new SJAcceptorThreadGroup(this, port, name, params); 
			
			Collection<SJSetupThread> sts = new LinkedList<SJSetupThread>();			
			
			for (SJTransport t : negotiationTrans) 
                sts.add(openAcceptorForNegotiation(port, params, atg, sts, t));
			
			if (sts.isEmpty())
                throw new SJIOException("[SJTransportManager_c] No valid negociationTrans: " + port);
			
			Collection<SJSessionAcceptorThread> ats = new LinkedList<SJSessionAcceptorThread>(); 
			
			for (SJTransport t : sessionTrans)
			{
                // If the transport is already a negotiation transport, reuse the negotiation acceptor.
				if (negotiationNames.contains(t.getTransportName()))
				{
					atg.addTransport(t, -1 * t.sessionPortToSetupPort(port)); 
                    // Marked as minus to show it's a setup port (transport connection needs to send NEGOTIATION_DONE). 
                    // Reuse the setup port for accepting (negotiated) transport connections.
				}
				else 
				{
                    SJSessionAcceptorThread thread = openAcceptorForSession(atg, t, params);
                    if (thread != null) ats.add(thread);
                }
			}
			
			if (ats.isEmpty() && atg.getTransports().isEmpty())
				throw new SJIOException("[SJTransportManager_c] No valid acceptors: " + port);
		
			acceptorGroups.put(port, atg);

			if (log.isLoggable(Level.FINE))
				log.fine("Opened acceptor group, port: " + port + ", negotiation: " + negotiationTrans + " session: " + sessionTrans + ", params: " + params);
            return atg;
		}		
	}

    private SJSessionAcceptorThread openAcceptorForSession(SJAcceptorThreadGroup atg, SJTransport t, SJSessionParameters params) {
        try {
            int freePort = t.getFreePort(); 
            // FIXME: need to lock the transport until after we have properly claimed the port. 
            // Either after we have opened the socket (or whatever, or need to manually manage 
            // which ports are free for each transport. But even this is not enough, another process
            // (e.g. any program opening TCP ports) may steal the port, so we may need to retry.

            SJSessionAcceptorThread at;

            if (t instanceof SJBoundedFifoPair) 
            // FIXME: currently hacked. Should there be a SJBoundedBufferTransport?
            {
                at = new SJSessionAcceptorThread(atg, 
	                ((SJBoundedFifoPair) t).openAcceptor(freePort, params.getBoundedBufferSize()));
            } else {
                at = new SJSessionAcceptorThread(atg, t.openAcceptor(freePort, params));
            }

            at.start();

            log.finer(t + " transport ready on: " + freePort);

            atg.addTransport(t, freePort);
            return at;
        }
        catch (SJIOException ioe) // Need to close the failed acceptor?
        {
            log.log(Level.FINER, "Could not open session acceptor", ioe);
            return null;
        }
    }

    private SJSetupThread openAcceptorForNegotiation(int port, SJSessionParameters params, SJAcceptorThreadGroup atg, Iterable<SJSetupThread> sts, SJTransport t) throws SJSetupException {
        try {
            SJSetupThread st;

            if (t instanceof SJBoundedFifoPair) // FIXME: currently hacked. Should there be a SJBoundedBufferTransport?
            {
                st = new SJSetupThread(atg, ((SJBoundedFifoPair) t).openAcceptor(t.sessionPortToSetupPort(port), params.getBoundedBufferSize()));
            } else {
                st = new SJSetupThread(atg, t.openAcceptor(t.sessionPortToSetupPort(port), params));
            }

            st.start();

            log.finer(t.getTransportName() + " setup ready on: " + port + '(' + t.sessionPortToSetupPort(port) + ")");

            return st;
        }
        catch (SJIOException ioe) // Need to close the failed acceptor?
        {
            log.log(Level.FINE, "Opening negotiation acceptor", ioe);

            System.out.println();
            System.out.println();            
            ioe.printStackTrace();
            System.out.println();
            System.out.println();
            
            for (SJSetupThread setupThread : sts) {
                setupThread.close();
            }

            throw new SJSetupException("[SJTransportManager_c] Setup \"" + t.getTransportName() + "\" could not be opened for session port: " + port, ioe);
        }
    }

	public void closeAcceptorGroup(int port)
	{
		synchronized (acceptorGroups)
		{
			acceptorGroups.remove(port).close();
		}
	}
	
    // FIXME: the two openConnections should be unified. This routine should do authentication as appropriate based on the transport type (as done in the client neogitation protocol). Then we will also not need the extra SJRuntime.reconnectAndAuthenticate routine, and the SJRuntime.connectSocket routine will also be working for the secure transport. Socket The issue is how to pass the credentials (as arguments, i.e. what should the method signature be?). 
	public SJConnection openConnection(String hostName, int port, SJSessionParameters params) throws SJIOException
	{
		List<SJTransport> ss = params.getNegotiationTransports();
    List<SJTransport> ts = params.getSessionTransports();
    List<String> tn = transportNames(ts);

    SJConnection conn = clientNegotiation(params, hostName, port, ss, ts, tn, params.getBoundedBufferSize());

    checkAndRegisterConnection(conn, hostName, port);

    return conn;		
	}
	
	// Duplicated from the above, but uses the authenticating client negotiation. Perhaps we should the compatibility mode (i.e. SECURE) to determine whether to do authentication or not.
	public SJConnection openAuthenticatedConnection(String hostName, int port, SJSessionParameters params, String user, String pwd) throws SJIOException
	{
    List<SJTransport> ss = params.getNegotiationTransports();
    List<SJTransport> ts = params.getSessionTransports();
    List<String> tn = transportNames(ts);

    SJConnection conn = clientNegotiationAndAuthentication(params, hostName, port, ss, ts, tn, params.getBoundedBufferSize(), user, pwd);
    
    checkAndRegisterConnection(conn, hostName, port);

    return conn;		
	}
	
	private void checkAndRegisterConnection(SJConnection conn, String hostName, int port) throws SJIOException
	{
		if (conn == null)
      throw new SJIOException("[SJTransportManager_c] Connection failed: " + hostName + ":" + port);

		log.finer("Connected on " + conn.getLocalPort() + " to " + hostName + ":" + port + " (" + conn.getPort() + ") using: " + conn.getTransportName());

		registerConnection(conn);		
	}
	
	public void closeConnection(SJConnection conn) // FIXME: add in close delay to allow connection reuse.
	{
		/*synchronized (sessions)
		{
			int active = sessions.get(conn).intValue() - 1;
			
			if (active == 0)
			{
				sessions.remove(conn);*/
		conn.disconnect();
		/*}
			else
			{
				sessions.put(conn, new Integer(active));
			}
		}*/
	}

    public List<SJTransport> activeNegotiationTransports() {
        return negotiationTransports.getActive();
    }

    public List<SJTransport> activeSessionTransports()
	{
		return sessionTransports.getActive();
	}
	protected void registerConnection(SJConnection conn)
	{
		/*synchronized (connections)
		{
			connections.put(conn.getHostName(), conn);
			
			synchronized (sessions)
			{				
				sessions.put(conn, new Integer(1));
			}
		}*/
	}
	
	protected boolean serverNegotiation(SJSessionParameters params, SJAcceptorThreadGroup atg, SJConnection conn) throws SJIOException
	{
		SJCompatibilityMode mode = params.getCompatibilityMode();
		
		if (mode == SJCompatibilityMode.CUSTOM)
		{
			return true; // Bypass negotiation protocol and use the existing connection for the session.
		}
		else if (mode != SJCompatibilityMode.SJ)
		{
			throw new SJRuntimeException("[SJTransportManager_c] Unknown compatibility mode: " + mode);
		}
			
		Map<String, Integer> tn = atg.getTransports();
		
		boolean transportAgreed;  
		
		String sname = conn.getTransportName();
		
		if (tn.containsKey(sname)) // FIXME: extend negotiation protocol with a SJ_SERVER_TRANSPORT_FORCE? Means the Client must reuse the setup. If the Client doesn't want it, can we rely on an implicit connection fail? (Maybe need a complementary SJ_CLIENT_NO_FORCE which means the same as NEGOTIATION_START, but no force option permitted.) May be useful for e.g. SJHTTPProxyServlet.   
		{
			if (tn.size() == 1) // If Server doesn't have any other transports, or for any other reason. // FIXME: maybe integrate into the ATI. Currently only used by SJHTTPProxyServlet.
			{
				conn.writeByte(SJ_SERVER_TRANSPORT_FORCE); 
				conn.flush();

                log.finer("SJ_SERVER_TRANSPORT_FORCE: " + sname);

                if (conn.readByte() == SJ_CLIENT_TRANSPORT_NO_FORCE) // Negotiation has failed.
				{
					throw new SJIOException("[SJTransportManager_c] Server supports no other transports: " + sname); 
				}
				
				transportAgreed = true;
			}
			else
			{
				conn.writeByte(SJ_SERVER_TRANSPORT_SUPPORTED);
				conn.flush();

                log.finer("SJ_SERVER_TRANSPORT_SUPPORTED: " + sname);

                transportAgreed = conn.readByte() == SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED;
			}
		}
		else
		{
			conn.writeByte(SJ_SERVER_TRANSPORT_NOT_SUPPORTED); 
			conn.flush();

            log.finer("SJ_SERVER_TRANSPORT_NOT_SUPPORTED: " + sname);

            conn.readByte(); // Doesn't matter if Client wants this transport or not. 
			
			transportAgreed = false;
		}
		
		boolean reuse = transportAgreed;
		
		if (!transportAgreed) // Start main negotiations.
		{
            // First send our transports (Client is also sending theirs now).

            byte[] bs = serializeObject(tn);

            log.finer("Sending server transport configuration to: " + conn.getHostName() + ": " + conn.getPort());

            conn.writeBytes(serializeInt(bs.length)); 
			conn.writeBytes(bs);
			
			// Now receive Client's transports.
			
			bs = new byte[SJ_SERIALIZED_INT_LENGTH];
			
			conn.readBytes(bs);				
	
			int len = deserializeInt(bs);
			
			bs = new byte[len];
			conn.readBytes(bs); 
			
			List<String> desiredTransports = (List<String>) deserializeObject(bs); 
            // Currently unused (see clientNegotiation).
		
			// Find out if Client wants to reuse the setup connection; otherwise the Client will connect to a different acceptor thread.
			
			reuse = conn.readByte() == REUSE_SETUP_CONNECTION;
		}
		
		return reuse; 		
	}
	
	protected SJConnection clientNegotiation(SJSessionParameters params, String hostName, int port, List<SJTransport> ss, List<SJTransport> ts, List<String> tn, int boundedBufferSize) throws SJIOException // Synchronized where necessary from calling scope.
	{
		return clientNegotiationAndAuthentication(params, hostName, port, ss, ts, tn, boundedBufferSize, null, null);
	}
	
	// HACK: Nuno's authenticated connections hacked in. Authentication only performed for SJAuthenticatingTransports; skipped for other transports.
	protected SJConnection clientNegotiationAndAuthentication(SJSessionParameters params, String hostName, int port, List<SJTransport> ss, List<SJTransport> ts, List<String> tn, int boundedBufferSize, String user, String pwd) throws SJIOException // Synchronized where necessary from calling scope.
	{
		SJConnection conn = null;
		
		for (SJTransport t : ss) // The negotiation transports.
		{
			try
			{
				if (t instanceof SJBoundedFifoPair) // FIXME: currently hacked. Should there be a SJBoundedBufferTransport? 
				{
					// Although the interface is for reading/writing bytes, here buffer size means SJ Runtime (session level) messages. Because each byte chunk corresponds to one such message. So correct buffer size settings depend on the SJ serialization protocol (similarly to message arrival detection for the SJSelector). Basically, this interface currently combines a byte-level abstract transport with a session-level abstract transport.   
					conn = ((SJBoundedFifoPair) t).connect(t.sessionHostToNegotiationHost(hostName), t.sessionPortToSetupPort(port), boundedBufferSize);
				}
				else if (t instanceof SJAuthenticatingTransport) // HACK.
				{
					if (user == null || pwd == null) // Should be passed via SJCredentials.
					{
						throw new SJIOException("[SJTransportManager_c] Missing authentication details: user=" + user + ", pwd=" + pwd); 
					}
					
					conn = ((SJAuthenticatingTransport) t).connect(t.sessionHostToNegotiationHost(hostName), t.sessionPortToSetupPort(port), user, pwd);
				}
				else 
				{
					conn = t.connect(t.sessionHostToNegotiationHost(hostName), t.sessionPortToSetupPort(port));
				}

                log.finer("Setting up on " + conn.getLocalPort() + " to " + hostName + ':' + t.sessionPortToSetupPort(port) + " using: " + t.getTransportName());

                break;
			}
			catch (SJIOException ioe)
			{
                log.log(Level.WARNING, t.getTransportName() + " setup failed: " + ioe.getMessage(), ioe);
            }		
		}						
		
		if (conn == null)
		{
			throw new SJIOException("[SJTransportManager_c] Setup failed: " + hostName + ':' + port);
		}			
				
		SJCompatibilityMode mode = params.getCompatibilityMode();
		
		if (mode == SJCompatibilityMode.CUSTOM) // Bypass negotiation protocol: go straight to session-layer accept/request protocol. Basically custom mode just tries the negotiation transports one-by-one, and session transports are ignored. Perhaps makes more sense to be the other way round, but can see it is convenient due to the way the negotiation protocol is implemented for now.
		{
			return conn;
		}
		else if (mode != SJCompatibilityMode.SJ)
		{
			throw new SJRuntimeException("[SJTransportManager_c] Unknown compatibility mode: " + mode);
		}
		
		boolean transportAgreed;
		
		String sname = conn.getTransportName();
		
		if (sname.equals(ts.get(0).getTransportName()))  
		{
			conn.writeByte(SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED);
			conn.flush();

            log.finer("SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED: " + sname);

            byte b = conn.readByte();
			
			transportAgreed = b == SJ_SERVER_TRANSPORT_SUPPORTED || b == SJ_SERVER_TRANSPORT_FORCE;
		}
		else 
		{
			if (!tn.contains(sname))
			{
				conn.writeByte(SJ_CLIENT_TRANSPORT_NO_FORCE); // Should be sent if the setup isn't a Client transport. 
				conn.flush();

                log.finer("SJ_CLIENT_TRANSPORT_NO_FORCE: " + sname);

                if (conn.readByte() == SJ_SERVER_TRANSPORT_FORCE) // Negotiation has failed.
				{
					throw new SJIOException("[SJTransportManager_c] Client does not support this transport: " + sname); 
				}
				
				transportAgreed = false;
			}
			else
			{
				conn.writeByte(SJ_CLIENT_TRANSPORT_NEGOTIATION_START);
				conn.flush();

                log.finer("SJ_CLIENT_TRANSPORT_NEGOTIATION_START: " + sname);

                byte b = conn.readByte();

				// FIXME: currently, only comes from SJHTTPProxyServlet, but need to prepare for it more generally.
                transportAgreed = b == SJ_SERVER_TRANSPORT_FORCE;
			}
		}
		
		if (!transportAgreed) // Start main negotiations.
		{
            // Send our transports.

            byte[] bs = serializeObject(tn);

            conn.writeBytes(serializeInt(bs.length));
			conn.writeBytes(bs); // This is currently pointless - Server has the same reply in any case.
			
			// Receive Server's transports.
			
			bs = new byte[SJ_SERIALIZED_INT_LENGTH];		
			conn.readBytes(bs);		
			
			int len = deserializeInt(bs);
			
			bs = new byte[len];		
			conn.readBytes(bs);
			
			Map<String, Integer> servers = (Map<String, Integer>) deserializeObject(bs);

            log.finer("Server at " + hostName + ':' + port + " offers: " + servers);

            for (SJTransport t : ts)
			{
				try
				{
					String name = t.getTransportName();
									
					if (sname.equals(name) && servers.get(name) != null)
					{
						conn.writeByte(REUSE_SETUP_CONNECTION); // Could make this a boolean?
						conn.flush();
						
						transportAgreed = true;
						
						break;
					}
					
					if (servers.containsKey(name))
					{
						int p = servers.get(name);
						
						SJConnection tmp;
						
						if (t instanceof SJBoundedFifoPair) // FIXME: currently hacked. Should there be a SJBoundedBufferTransport? 
						{
							tmp = ((SJBoundedFifoPair) t).connect(t.sessionHostToNegotiationHost(hostName), p < 0 ? -1 * p : p, boundedBufferSize);
						}
						else // The original case. 
						{
							tmp = t.connect(t.sessionHostToNegotiationHost(hostName), p < 0 ? -1 * p : p);
						}
						
						if (p < 0) // Connected to a setup, so bypass the preliminary negotiation phase.
						{
							tmp.writeByte(SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED);
							tmp.flush();
						
							byte b = tmp.readByte();
							
							if (!(b == SJ_SERVER_TRANSPORT_SUPPORTED || b == SJ_SERVER_TRANSPORT_FORCE))
							{
								throw new SJRuntimeException("[SJTransportManager_c] Shouldn't get in here: " + b);
							}
						}
						
						conn.writeByte(CLOSE_SETUP_CONNECTION); // i.e. Not reusing it.
						conn.flush();
						
						conn.disconnect();														
						
						conn = tmp;																				
						
						transportAgreed = true;
						
						break;
					}
				}
				catch (SJIOException ioe)
				{
                    log.finer("Transport connection failed: " + ioe);
                }					
			}
		}
		
		return transportAgreed ? conn : null;
	}
}
