package sessionj.runtime.net;

import polyglot.types.SemanticException;
import sessionj.ExtensionInfo;
import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.session.*;
import sessionj.runtime.session.security.SJSecureSessionProtocols;
import sessionj.runtime.transport.*;
import sessionj.runtime.util.SJClassResolver;
import sessionj.runtime.util.SJRuntimeTypeEncoder;
import sessionj.runtime.util.SJRuntimeUtils;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJSessionType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
public class SJRuntime
{
	private	static final ExtensionInfo extInfo;
	private static final SJTypeSystem sjts;
	private static final SJClassResolver sjcr;
	private static final SJRuntimeTypeEncoder sjte;	
	
	private static final String LOCAL_HOST_NAME;

    private static SJTransportManager sjtm;

	private static final int LOWER_PORT_LIMIT = 1024;
	private static final int UPPER_PORT_LIMIT = 65535;
  
    private static final Set<Integer> portsInUse = new HashSet<Integer>();
    private static final Logger log = SJRuntimeUtils.getLogger(SJRuntime.class);

    static
	{
		try
		{
			extInfo = new ExtensionInfo();
			sjts = (SJTypeSystem) extInfo.typeSystem();
			sjcr = new SJClassResolver(sjts, extInfo);
			sjts.initialize(sjcr, extInfo);
			sjte = new SJRuntimeTypeEncoder(sjts);
		}
		catch (SemanticException se)
		{
			throw new SJRuntimeException(se);
		}
		
		try
		{
			//LOCAL_HOST_NAME = InetAddress.getLocalHost().getHostName();
            // // FIXME: problems for e.g. HZHL2 on IC-DoC.
            // But we've starting using host names, so use them for now.
			LOCAL_HOST_NAME = InetAddress.getLocalHost().getHostAddress();
            // FIXME: we're actually using IP addresses now, not host names.
		}
		catch (UnknownHostException uhe)
		{
			throw new SJRuntimeException(uhe);
		}
	}	

	static { // Should be merged with the above static initialiser.
    try {
        sjtm = new SJTransportManager_c();
    } catch (SJIOException e) {
        e.printStackTrace();
        System.exit(-1);
    }
	}
  
	private SJRuntime() { }
	
	private static final Map<SJPort, SJAcceptorThreadGroup> reservedPorts = new HashMap<SJPort, SJAcceptorThreadGroup>();
    // FIXME: need to free up unclaimed ports.
	
	//private static final Map<Integer, DatagramSocket> servers = new HashMap<Integer, DatagramSocket>();
	
	/*public static SJAcceptorThreadGroup getFreshAcceptorThreadGroup() throws SJIOException
	{
		return getFreshAcceptorThreadGroup(SJSessionParameters.DEFAULT_PARAMETERS);
	}*/
	
	//public static SJServerSocket getFreshServer(SJProtocol p) throws SJIOException
	public static SJAcceptorThreadGroup getFreshAcceptorThreadGroup(SJSessionParameters params) throws SJIOException // Used to receive sessions.
	{
		int attempts = 100;
		
		synchronized (portsInUse)
		{
			for (int i = 0; i < attempts; i++)
			{
				int port = takeFreePort(); // Makes a record for portsInUse.
				
				try
				{								
					return sjtm.openAcceptorGroup(port, params);
				}
				catch (SJSetupException se)
				{
					freePort(port);
				}							
			}			
		}
		
		throw new SJIOException("[SJRuntime] Could not get a fresh server socket.");
	}
	
	public static SJPort reserveFreeSJPort() throws SJIOException
	{
		return reserveFreeSJPort(SJSessionParameters.DEFAULT_PARAMETERS);
	}
	
	public static SJPort reserveFreeSJPort(SJSessionParameters params) throws SJIOException
    // Unlike takeFreeSJPort, this actually reserves the setups (guarantees port is open).
	{
		SJPort p = null;
		
		//SJAcceptorThreadGroup atg = getFreshAcceptorThreadGroup();
        // Doesn't work because getFreshAcceptorThreadGroup uses takeFreePort, which registers the port as in use,
        // and then when we get a conflict when we create the SJPort. 			

        SJAcceptorThreadGroup atg = null;
		
		int attempts = 100; // Following adapted from takeFreePort and getFreshAcceptorThreadGroup.
		
		Random rand = new Random();
		
		synchronized (reservedPorts) // Necessary? 
		{
			synchronized (portsInUse)
			{				
				for (int i = 0; i < attempts && atg == null; i++)
				{
					int port = rand.nextInt(UPPER_PORT_LIMIT - LOWER_PORT_LIMIT) + LOWER_PORT_LIMIT;
					
					try
					{								
						atg = sjtm.openAcceptorGroup(port, params); 
						
						p = new SJPort(port);
					}
					catch (SJSetupException se)
					{
						//freePort(port);
					}							
				}
				
				if (atg == null)
				{
					throw new SJIOException("[SJRuntime] Could not get a fresh server socket.");		
				}
			}					
			
			reservedPorts.put(p, atg); // To reserve the port.
		}
		
		return p; // A "ticket" to the reserved port. When this port is used, can "claim" the AcceptorThreadGroup for use.
	}
	
	/*public static void openFreshServerSocket(SJProtocol p) throws SJIOException
	{
		// This might be useful in some cases.
	}*/
	
	public static void openServerSocket(SJServerSocket ss) throws SJIOException
	{		
		SJPort sjPort = ss.getLocalSJPort();
		
		if (sjPort == null || !reservedPorts.containsKey(sjPort)) // Need synchronization?
		{
			int port = ss.getLocalPort();

			synchronized (portsInUse)
			{
				if (portsInUse.contains(port))
				{
					throw new SJIOException("[SJRuntime] Port already in use: " + port);
				}
				
				ss.setAcceptorGroup(sjtm.openAcceptorGroup(port, ss.getParameters()));		
				
				portsInUse.add(port);
			}
		}
		else
		{
			synchronized (reservedPorts) // Synchronization necessary? // It shouldn't be possible for a manually created SJPort to clash with a reserved one (synchronized on portsInUse). So only the reserver can claim the reserved port. 
			{
				ss.setAcceptorGroup(reservedPorts.remove(sjPort));
			}
		}
	}
	
	public static void closeServerSocket(SJServerSocket ss) 
	{
		int port = ss.getLocalPort();

        synchronized (portsInUse)
		{
			portsInUse.remove(port);
		}
		
		sjtm.closeAcceptorGroup(port);
	}
	
	public static void bindSocket(SJAbstractSocket s, SJConnection conn) throws SJIOException // Currently, conn can be null (delegation case 2 reconnection).
	{		
		s.init(conn);  
		
		s.setLocalHostName(LOCAL_HOST_NAME);
		s.setLocalPort(takeFreePort());
	}
	
	// The secure compatibility mode needs an authenticating version of this routine. FIXME: move authentication decision into SJTransportManager openConnection routine (based on transport type), then no changes needed here. 
	public static void connectSocket(SJRequestingSocket s) throws SJIOException
	{		
		SJServerIdentifier si = s.getServerIdentifier();
		String targetHostAddress;
		
		try
		{
			targetHostAddress = InetAddress.getByName(si.getHostName()).getHostAddress();
		}
		catch (UnknownHostException uhe)
		{
			throw new SJIOException(uhe);
		}
		
		s.init(sjtm.openConnection(targetHostAddress, si.getPort(), s.getParameters()));

		s.setHostName(targetHostAddress);
        // Host names are more fragile than IP addresses (e.g. HZHL2 on IC-DoC).
		s.setPort(si.getPort());
		
		s.setLocalHostName(LOCAL_HOST_NAME);
        // Actually, initialised to IP address (not host name), as we are doing for the target host "name".
		s.setLocalPort(takeFreePort());
	}

    public static void reconnectSocket(String p, SJSocket s, String hostName, int port) throws SJIOException
	{		
	    if(s instanceof SJSocketGroup)
	    {
		closeSocket(s);		
		((SJSocketGroup)s).reconnect(p, sjtm.openConnection(hostName, port, ((SJSocketGroup)s).getParameters(p)));  		
		setHostNameAndPort(p, s, hostName, port);
	    }
	    else
	    {
		closeSocket(s);		
		s.reconnect(sjtm.openConnection(hostName, port, s.getParameters()));  		
		setHostNameAndPort(p, s, hostName, port);
	    }
	}
    public static void reconnectSocket(SJSocket s, String hostName, int port) throws SJIOException
	{
		closeSocket(s);		
		s.reconnect(sjtm.openConnection(hostName, port, s.getParameters()));  		
		setHostNameAndPort("No_valid_socket_MQ", s, hostName, port);
	}
	
	// Duplicated from the above, but uses the authenticating client negotiation.
	// FIXME: perhaps we should check the compatibility mode, i.e. if SECURE, to determine whether or not to do authentication. On the other hand, we already use the compatibility mode to create the appropriate session protocols object, which encapsulates these decisions.
    public static void reconnectAndAuthenticateSocket(String p, SJSocket s, String hostName, int port, String user, String pwd) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
	    {
		closeSocket(s);		
		((SJSocketGroup)s).reconnect(p, sjtm.openAuthenticatedConnection(hostName, port, ((SJSocketGroup)s).getParameters(p), user, pwd));		
		setHostNameAndPort(p, s, hostName, port);
	    }
	    else
	    {
		closeSocket(s);		
		s.reconnect(sjtm.openAuthenticatedConnection(hostName, port, s.getParameters(), user, pwd));		
		setHostNameAndPort(p, s, hostName, port);
	    }

	}	
    public static void reconnectAndAuthenticateSocket(SJSocket s, String hostName, int port, String user, String pwd) throws SJIOException
	{
	    
		closeSocket(s);		
		s.reconnect(sjtm.openAuthenticatedConnection(hostName, port, s.getParameters(), user, pwd));		
		setHostNameAndPort("No_valid_socket_MQ", s, hostName, port);
	}	
	
    private static void setHostNameAndPort(String p, SJSocket s, String hostName, int port) throws SJIOException
	{
		try
		{
		    if(s instanceof SJSocketGroup)
		    {
			((SJSocketGroup)s).setHostName(p, InetAddress.getByName(hostName).getHostAddress());
			// Host names are more fragile than IP addresses (e.g. HZHL2 on IC-DoC).
			((SJSocketGroup)s).setPort(p, port);		
		    }
		    else
		    {
			s.setHostName(InetAddress.getByName(hostName).getHostAddress());
			// Host names are more fragile than IP addresses (e.g. HZHL2 on IC-DoC).
			s.setPort(port);		
		    }
		}
		catch (UnknownHostException uhe)
		{
			throw new SJIOException(uhe);
		}
	}
	
	public static void closeSocket(SJSocket s)
	{
		SJConnection conn = s.getConnection();
				
		if (conn != null) // FIXME: need a isClosed.
		{
			freePort(s.getLocalPort());
			
			sjtm.closeConnection(conn);
		}
	}
	
    public static void accept(String p, SJAbstractSocket s) throws SJIOException, SJIncompatibleSessionException
	{					
        log.finer("accept on socket: " + s);
		if (s.getParameters().getCompatibilityMode() == SJCompatibilityMode.SJ) // FIXME: maybe move into SJSessionParametersImpl? Since we don't do it for custom compatibility modes.
		{
			/*try
			{
				s.setHostName((String) s.receive()); // Will be whatever requestor has set its LOCAL_HOST_NAME to. // Maybe host name can be gotten from the underlying connection. Session port value needs to be sent though.
				s.setPort(s.receiveInt());
			}
			catch (ClassNotFoundException cnfe)
			{
				throw new SJRuntimeException(cnfe);
			}*/			
			
			try // Cannot use send/receive because it will interfere with the type monitoring.
			{
				SJSerializer ser = s.getSerializer();
				
                log.finest("About to read host name (object)");
				s.setHostName((String) ser.readObject()); // Will be whatever requestor has set its LOCAL_HOST_NAME to. // Maybe host name can be gotten from the underlying connection. Session port value needs to be sent though.
                log.finest("About to read port (int)");
				s.setPort(ser.readInt());
			}
			catch (ClassNotFoundException cnfe)
			{
				throw new SJRuntimeException(cnfe);
			}
			catch (SJControlSignal cs)
			{
				throw new SJRuntimeException("[SJRuntime] Shouldn't get in here: ", cs);
			}
		}
		else //if (s.getParameters().getCompatibilityMode() == SJCompatibilityMode.CUSTOM)
		{
			// FIXME: what should we set the remote host name and port values to be? Maybe we can get it from the underlying concrete transport connection, e.g. TCP, but some transports won't support it.
		}
		
		s.accept();
	}
	
	protected static void request(SJAbstractSocket s) throws SJIOException, SJIncompatibleSessionException
	{	
		if (s.getParameters().getCompatibilityMode() == SJCompatibilityMode.SJ) // FIXME: as for accept, maybe move into SJSessionParametersImpl? Since we don't do it for custom compatibility modes.
		{
			/*s.send(s.getLocalHostName());		
			s.sendInt(s.getLocalPort());*/
			
			SJSerializer ser = s.getSerializer(); // Cannot use send/receive because it will interfere with the type monitoring.
			
			ser.writeObject(s.getLocalHostName()); 		
			ser.writeInt(s.getLocalPort());
		}
		else //if (s.getParameters().getCompatibilityMode() == SJCompatibilityMode.CUSTOM)
		{
			// FIXME: what should we set the remote host name and port values to be? Maybe we can get it from the underlying concrete transport connection, e.g. TCP, but some transports won't support it.
		}
		
		s.request();
	}

    private static int i = 1;
    private static final ExecutorService executor = Executors.newFixedThreadPool(5, new ThreadFactory() {
        public Thread newThread(Runnable runnable) {
            Thread t = Executors.defaultThreadFactory().newThread(runnable);
            t.setName("SessionCloser" + i++);
            t.setDaemon(true);
            return t;
        }
    });
    public static void close(final SJSocket s) throws SJIOException //<By MQ>
    {
        // Need to allow arbitrary interleaving of close() calls, as there
        // is a handshake with the other party in the close protocol.
        if (s != null) {
	    s.flush();
	    s.close();
            /*Runnable closer = new Runnable() {
                public void run() {		    
		    s.close();
                }
            };
            if (log.isLoggable(Level.FINER)) 
                log.finer("Submitting close for: " + s);
		executor.submit(closer);*/
        }
	}	
	
        public static void close(SJSocket... sockets) throws SJIOException //<By MQ>
	{
		for (SJSocket s : sockets)
		{
            close(s);
		}
	}

	// SJSocket doesn't have to be in the last position, but perhaps uniform with the other variant
    public static void send(Object obj, String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		((SJSocketGroup)s).send(p, obj);
	    else
		s.send(obj);
	}

	// a send call with a single SJSocket argument will invoke the above method, not this one
    public static void send(Object obj, String p, SJSocket... sockets) throws SJIOException 
	{
		for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).send(p, obj);
		    else
			s.send(obj);
		}
	}

    public static void send(int i, String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
                ((SJSocketGroup)s).sendInt(p, i);
            else
                s.sendInt(i);
	}

    public static void send(int i, String p, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).sendInt(p, i);
		    else
			s.sendInt(i);
		}
	}

    public static void send(boolean b, String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
                ((SJSocketGroup)s).sendBoolean(p, b);
            else
                s.sendBoolean(b);
	}

    public static void send(boolean b, String p, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).sendBoolean(p, b);
		    else
			s.sendBoolean(b);
		}
	}
	
    public static void send(double d, String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
                ((SJSocketGroup)s).sendDouble(p, d);
            else
                s.sendDouble(d);		
	}

    public static void send(double d, String p, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).sendDouble(p, d);
		    else
			s.sendDouble(d);
		}
	}
	
    public static void pass(Object obj, String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		((SJSocketGroup)s).pass(p, obj);
	    else
		s.pass(obj);            
	}

    public static void pass(Object obj, String p, SJSocket... sockets) throws SJIOException
	{
	    for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).pass(p, obj);
		    else
			s.pass(obj);
		}
	}
	
    public static void copy(Object obj, String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
                ((SJSocketGroup)s).copy(p, obj);
            else
                s.copy(obj);
	}

    public static void copy(Object obj, String p, SJSocket... sockets) throws SJIOException
    {
	for (SJSocket s : sockets)
	    {
		if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).copy(p, obj);
		else
		    s.copy(obj);
	    }
	}
	
	/*public static void copy(SJSocket s, int i) throws SJIOException	// Are these ever needed? 
	{
		s.copyInt(i);
	}
	
	public static void copy(SJSocket[] sockets, int i) throws SJIOException	
	{
		for (SJSocket s : sockets)
		{
			s.copyInt(i);
		}
	}
	
	public static void copy(SJSocket s, double d) throws SJIOException	// Primitives can also be declared na-final... 
	{
		s.copyDouble(d);
	}
	
	public static void copy(SJSocket[] sockets, double d) throws SJIOException	
	{
		for (SJSocket s : sockets)
		{
			s.copyDouble(d);
		}
	}*/

    private static boolean isValidResult(Object res, Class<?> expectedClass) {
        if (expectedClass == null) return !(res instanceof Throwable);
        else return res.getClass().isInstance(expectedClass);
    }

    // Currently not "optimised" to use e.g. setSoTimeout. 
    private static Object timedReceive(int timeout, SJSocket s, int typeCode, String typeName, Class<?> expectedClass, Object[] args) throws SJIOException {
        Object[] res = new Object[1];

        new SJRuntimeReceiveTimeout(Thread.currentThread(), s, res, typeCode, args).start();

        try
		{
			Thread.sleep(timeout);
        }
        catch (InterruptedException ie)
        {
            // res is passed as a parameter to the SJRuntimeReceiveTimeout constructor,
            // hence is properly shared even though it's a local variable
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (res)
            {
                if (isValidResult(res[0], expectedClass))
                {
                    return res[0];
                }
                else if (res[0] instanceof SJIOException)
                {
                    throw (SJIOException) res[0];
                }
                else if (res[0] instanceof RuntimeException)
				{
					throw (RuntimeException) res[0];
				}
                else
                {
                    throw new RuntimeException("Unexpected type for received message: " + res[0]);
                }
            }
        }
        // We only want to close it if there was a timeout and we're aborting the session.
        ((SJAbstractSocket) s).getSerializer().close();
        // Is this a good idea? (Want to bypass FIN protocol).
        // FIXME: could try to send our FIN somehow, but maybe not possible (if it is though, should factor out a terminal early-close routine).

        throw new SJTimeoutException("[SJRuntime] receive" + typeName + " timed out: " + timeout);
    }
 //<By MQ> Changed all receive methods to support multi-party
    public static Object receive(String p, SJSocket s) throws SJIOException, ClassNotFoundException // Remove array in a compiler pass.
	{
	    s.flush(); //<By MQ>
	    if(s instanceof SJSocketGroup)
		return ((SJSocketGroup) s).receive(p);
	    else
		return s.receive();
	}
	
	//RAY
	// Copied from cvs version. Need dummy target with sockets array to satisfy initial base typing pass before SJUnicastOptimiser is run (assuming that pass is still needed to convert the singleton array).  
    public static Object receive(String p, SJSocket[] sockets) throws SJIOException, ClassNotFoundException // Remove array in a compiler pass.
	{
		throw new SJRuntimeException("[SJRuntime] Shouldn't get into here: " + sockets);
	}
	//YAR
	
    // FIXME: only half done so far. Need to do for remaining ops.: accept, compound ops., etc.
    // Currently relies on implicit close to terminate the SJRuntimeReceiveTimeout thread that is still blocked on the receive operation -
    // need to check this works for all transports (check that an exception from early close is propagated up properly).
    // Also, need more work on making SJTimeout a terminal exception - don't do FIN protocol (on our side at least).
        public static Object receive(String p, int timeout, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		return timedReceive(timeout, ((SJSocketGroup)s).getSocket(p), SJRuntimeReceiveTimeout.OBJECT, "Object", Object.class, null);
	    else
		return timedReceive(timeout, s, SJRuntimeReceiveTimeout.OBJECT, "Object", Object.class, null);
	}
	
        public static Object receive(String p, int timeout, SJSocket... sockets) throws SJIOException
	{
        // TODO multi-socket
	    return receive(p, timeout, sockets[0]);
	}
	
	public static int receiveInt(String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		return ((SJSocketGroup) s).receiveInt(p);
	    else
		return s.receiveInt();
        // TODO multi-socket
	}
	
        public static int receiveInt(String p, SJSocket... sockets) throws SJIOException
	{
	    if(sockets[0] instanceof SJSocketGroup)
		return ((SJSocketGroup) sockets[0]).receiveInt(p);
	    else
		return sockets[0].receiveInt();
        // TODO multi-socket
	}
	
        public static int receiveInt(String p, int timeout, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		return (Integer) timedReceive(timeout, ((SJSocketGroup) s).getSocket(p), SJRuntimeReceiveTimeout.INT, "Int", Integer.class, null);
	    else
		return (Integer) timedReceive(timeout, s, SJRuntimeReceiveTimeout.INT, "Int", Integer.class, null);
    }
	
        public static int receiveInt(String p, int timeout, SJSocket... sockets) throws SJIOException
	{
	    return receiveInt(p, timeout, sockets[0]);
        // TODO mulit-socket        
	}
	
    public static boolean receiveBoolean(String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
                return ((SJSocketGroup) s).receiveBoolean(p);
            else
                return s.receiveBoolean();
	}
	
    public static boolean receiveBoolean(String p, SJSocket... sockets) throws SJIOException
	{
	    if(sockets[0] instanceof SJSocketGroup)
                return ((SJSocketGroup) sockets[0]).receiveBoolean(p);
            else
                return sockets[0].receiveBoolean();
        // TODO multi-socket
	}
	
    public static boolean receiveBoolean(String p, int timeout, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		return (Boolean) timedReceive(timeout, ((SJSocketGroup)s).getSocket(p), SJRuntimeReceiveTimeout.BOOLEAN, "Boolean", Boolean.class, null);
	    else
		return (Boolean) timedReceive(timeout, s, SJRuntimeReceiveTimeout.BOOLEAN, "Boolean", Boolean.class, null);
	}
	
    public static boolean receiveBoolean(String p, int timeout, SJSocket... sockets) throws SJIOException
	{
	    return receiveBoolean(p, timeout, sockets[0]);
	    // TODO multi-socket        
	}

    public static double receiveDouble(String p, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		return ((SJSocketGroup)s).receiveDouble(p);
	    else
		return s.receiveDouble();
	}

    public static double receiveDouble(String p, SJSocket... sockets) throws SJIOException
	{
	    if(sockets[0] instanceof SJSocketGroup)
                return ((SJSocketGroup)sockets[0]).receiveDouble(p);
            else
                return sockets[0].receiveDouble();
        // TODO multi-socket                
	}

    public static double receiveDouble(String p, int timeout, SJSocket s) throws SJIOException
	{
	    if(s instanceof SJSocketGroup)
		return (Double) timedReceive(timeout, ((SJSocketGroup)s).getSocket(p), SJRuntimeReceiveTimeout.DOUBLE, "Double", Double.class, null);
	    else
		return (Double) timedReceive(timeout, s, SJRuntimeReceiveTimeout.DOUBLE, "Double", Double.class, null);
	}

    public static double receiveDouble(String p, int timeout, SJSocket... sockets) throws SJIOException
	{
	    return receiveDouble(p, timeout, sockets[0]);
        // TODO multi-socket
	}
	
    public static boolean recurse(String p, String lab, SJSocket s) throws SJIOException
	{
    // Session-level recurse to a label is translated to a boolean value.
	    if(s instanceof SJSocketGroup)
		return ((SJSocketGroup)s).recurse(p, lab);
	    else
		return s.recurse(lab);
	}
   
    public static boolean recurse(String p, String lab, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).recurse(p, lab);
		    else
			s.recurse(lab);
		}				
		
		return true; // A bit hacky.
	}

	/*public static void spawn(SJSocket[] sockets, SJThread t) throws SJIOException
	{
		t.spawn(...);
	}*/

    public static boolean outsync(boolean cond, SJSocket s) throws SJIOException
	{
	    return s.outsync(cond);
	}

    public static boolean outsync(boolean cond, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
			s.outsync(cond);
		}		
		return cond;
	}

    public static boolean insync(String p, SJSocket s) throws SJIOException 
    {
	if(s instanceof SJSocketGroup)
	    return ((SJSocketGroup)s).insync(p);
	else
	    return s.insync();
    }

    //RAY
    public static LoopCondition negotiateOutsync(final boolean selfInterruptible, final SJSocket s) throws SJIOException {
	boolean interrupting = s.isPeerInterruptingIn(selfInterruptible);
      if (interrupting)
              return new LoopCondition() {
                  public boolean call(boolean arg) throws SJIOException {
                      return interruptibleOutsync(arg, s);
                  }
              };
      else
          /*return new LoopCondition() {
              public boolean call(boolean arg) throws SJIOException {
                  return outsync(arg, s);
              }
          };*/
      	return null;
  }    
    //YAR
    
    public static LoopCondition negotiateOutsync(final boolean selfInterruptible, final SJSocket[] sockets) throws SJIOException {
        boolean interrupting = checkAllAgree(new SJSocketTest() {
                public boolean call(SJSocket s) throws SJIOException {
                    boolean b = s.isPeerInterruptingIn(selfInterruptible);
                    return b;
                }
            }, sockets,
           "Multi-party outwhile: all peers need to be either interrupting or non-interrupting");
        if (interrupting)
                return new LoopCondition() {
                    public boolean call(boolean arg) throws SJIOException {
                        return interruptibleOutsync(arg, sockets);
                    }
                };
        else
            return new LoopCondition() {
                public boolean call(boolean arg) throws SJIOException {
                    return outsync(arg, sockets);
                }
            };
    }

    private static boolean interruptibleOutsync(boolean condition, SJSocket s) throws SJIOException {
      s.interruptibleOutsync(condition);
      return condition;
  }
    
    private static boolean interruptibleOutsync(boolean condition, SJSocket[] sockets) throws SJIOException {
        for (SJSocket s : sockets) {
            s.interruptibleOutsync(condition);
        }
        return condition;
    }

    public static void negotiateNormalInwhile(String p, SJSocket s) throws SJIOException {	
	s.flush();
	if(s instanceof SJSocketGroup)
	    ((SJSocketGroup)s).isPeerInterruptibleOut(p, false);
	else
	    s.isPeerInterruptibleOut(false);
	s.flush(); //<By MQ>
  }
    
    public static void negotiateNormalInwhile(String p, SJSocket[] sockets) throws SJIOException {
        for (SJSocket s : sockets) 
        {
	    if(s instanceof SJSocketGroup)
		((SJSocketGroup)s).isPeerInterruptibleOut(p, false);
	    else
		s.isPeerInterruptibleOut(false);
        }
    }

    public static boolean negotiateInterruptingInwhile(SJSocket[] sockets) throws SJIOException {
        return checkAllAgree(new SJSocketTest() {
                public boolean call(SJSocket s) throws SJIOException {
                    return s.isPeerInterruptibleOut(true);
                }
            }, sockets,
            "Multi-party inwhile: all peers need to either all support interruption or all reject it"
        );
    }

    public static boolean interruptingInsync(final boolean condition, final boolean peersInterruptible, SJSocket[] sockets) throws SJIOException {
        //Semantics: require all sockets to terminate at the same time, otherwise fail on all sockets.
        return checkAllAgree(new SJSocketTest() {
                public boolean call(SJSocket s) throws SJIOException {
                    return s.interruptingInsync(condition, peersInterruptible);
                }
            }, sockets,
            "Multi-party inwhile: some of the sockets signalled end of transmission but not all"
        );
    }

    private interface SJSocketTest {
        boolean call(SJSocket s) throws SJIOException;
    }

	public static boolean insync(SJSocket... sockets) throws SJIOException {
		//Semantics: require all sockets to terminate at the same time, otherwise fail on all sockets.
        return checkAllAgree(new SJSocketTest() {
                public boolean call(SJSocket s) throws SJIOException {
                    return s.insync();
                }
            }, sockets,
            "Multi-party inwhile: some of the sockets signalled end of transmission but not all"
        );
    }

    private static boolean checkAllAgree(final SJSocketTest test, SJSocket[] sockets, String message) throws SJIOException {
    	//RAY
    	// Order which the values arrive doesn't matter (and we need them all before progressing): we can just do one by one in _any_ order (that means we can pick an arbitrary order, we don't have to actually need to handle "any order")  
    	// Seems to heavy weight to make a new Executor object every time we do a loop (e.g. two nested loops, end up making a lot of these for the inner loop)
    	// FIXME: no, the isPeerInterruptible is a synchronous exchange, so we will block if users do not write sockets in the right order
        /*ExecutorService es = Executors.newFixedThreadPool(sockets.length);

        List<Future<Boolean>> values = new LinkedList<Future<Boolean>>();
        for (final SJSocket s : sockets) {
            values.add(es.submit(new Callable<Boolean>() {
                public Boolean call() throws SJIOException {
                    return test.call(s);
                }
            }));
        }*/
    	//boolean[] values = new boolean[sockets.length];
    	boolean first = test.call(sockets[0]);
    //for (int i = 0; i < values.length; i++)
    	for (int i = 1; i < sockets.length; i++)
    	{
    		//values[i] = test.call(sockets[i]);
    		if (first != test.call(sockets[i]))
    		{
    			throw new SJIOException(message);
    		}
    	}
    	//YAR
        
    	/*
        boolean fold;
        try {
        	//RAY
            /*fold = values.get(0).get();
            for (int i=1; i<values.size(); ++i) {
                if (values.get(i).get() ^ fold) throw new SJIOException(message);
            }*
        	fold = values[0];
        	for (int i = 1; i < values.length; i++)
        	{
        		if (values[i] ^ fold) throw new SJIOException(message); // This is "late detection"; we could have raised the error earlier as soon as we read one value that doesn't match the previous (then we don't need to loop twice)
        	}
        	//YAR
        /*} catch (InterruptedException e) {
            throw new SJIOException(e);
        } catch (ExecutionException e) {
            throw new SJIOException(e);*
        } finally {
        	//RAY
            //es.shutdown();
        	//YAR
        }

        return fold;*/
    	return first;
    }

    public static void outlabel(String lab, SJSocket s) throws SJIOException
    // FIXME: this should be automatically eligible for reference passing, need to check how it is
    // currently performed - labels cannot be user modified, and are immutable Strings anyway.
	{
		s.outlabel(lab);
	}

	public static void outlabel(String lab, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
			s.outlabel(lab);
		}			
	}

    public static String inlabel(String p, SJSocket s) throws SJIOException
	{	
	    if(s instanceof SJSocketGroup)
                return ((SJSocketGroup) s).inlabel(p);
            else
                return s.inlabel();
	}

    public static String inlabel(String p, SJSocket... sockets) throws SJIOException
	{
	    if(sockets[0] instanceof SJSocketGroup)
                return ((SJSocketGroup) sockets[0]).inlabel(p);
            else
                return sockets[0].inlabel();
        // TODO multi-socket
	}

	//public static boolean recursionEnter(SJSocket s) throws SJIOException
	public static boolean recursionEnter(String lab, SJSocket s) throws SJIOException
	{
		return s.recursionEnter(lab);
	}

	//public static boolean recursionEnter(SJSocket... sockets) throws SJIOException
	public static boolean recursionEnter(String lab, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
			s.recursionEnter(lab); // Should return false.
		}
		
		return false;
	}

	public static boolean recursionExit(SJSocket s) throws SJIOException
	{
		return s.recursionExit(); 
	}
 
	public static boolean recursionExit(SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
			s.recursionExit(); // Should return false.
		}
		
		return false;		
	}
	
	/*public static void sendChannel(SJSocket[] sockets, SJService c) throws SJIOException // Channel objects should be immutable, so can be passed. // Remove array in a compiler pass
	{
		sockets[0].sendChannel(c);
	}*/

    public static void copy(String p, Object obj, String encoded, SJSocket s) throws SJIOException
    // Shared-channel passing.
	{
	    if(s instanceof SJSocketGroup)
		((SJSocketGroup)s).sendChannel(p, (SJService) obj, encoded);
	    else
		s.sendChannel((SJService) obj, encoded);
        // Could just extract the session type from the SJService.
	}

    public static void copy(String p, Object obj, String encoded, SJSocket... sockets) throws SJIOException
	{
		for (SJSocket s : sockets)
		{
		    if(s instanceof SJSocketGroup)
			((SJSocketGroup)s).sendChannel(p, (SJService) obj, encoded);
		    else
			s.sendChannel((SJService) obj, encoded);
		}
	}

    public static SJService receiveChannel(String p, String encoded, SJSocket s) throws SJIOException, ClassNotFoundException
    // Needs a different name to session-receive - arguments are the same.
    // No ClassNotFoundException means runtime errors (from malicious peers) due to receiving unexpected
    // and unknown object classes must be converted to IO errors.
    // Actually, no ClassNotFoundException here doesn't seem to make much difference, the base type checker uses the "ordinary" receive.
    // Actually, no ClassNotFoundException here breaks the code generation javac pass...
	{
	    if(s instanceof SJSocketGroup)
		return ((SJSocketGroup)s).receiveChannel(encoded);
	    else
		return s.receiveChannel(encoded);
	}
	
    public static SJService receiveChannel(String p, String encoded, SJSocket... sockets) throws SJIOException, ClassNotFoundException
    // Needs a different name to session-receive - arguments are the same.
	{
	    if(sockets[0] instanceof SJSocketGroup)
                return ((SJSocketGroup)sockets[0]).receiveChannel(encoded);
            else
                return sockets[0].receiveChannel(encoded);
        // TODO multi-socket
	}
	
    public static SJService receiveChannel(String p, String encoded, int timeout, SJSocket s) throws SJIOException, ClassNotFoundException
    // Session-receive.
	{
	    if(s instanceof SJSocketGroup)
		return (SJService) timedReceive(timeout, ((SJSocketGroup)s).getSocket(p), SJRuntimeReceiveTimeout.CHANNEL, "Channel", null, new Object[] { encoded });
	    else
		return (SJService) timedReceive(timeout, s, SJRuntimeReceiveTimeout.CHANNEL, "Channel", null, new Object[] { encoded });
	}
	
    public static SJService receiveChannel(String p, String encoded, int timeout, SJSocket... sockets) throws SJIOException, ClassNotFoundException
	{
	    return receiveChannel(p, encoded, timeout, sockets[0]);
        // TODO multi-session
	}

    public static void pass(String p, Object obj, String encoded, SJSocket s) throws SJIOException
    // Session delegation. Probably better to rename to something more obvious here as well.
	{
	    if(s instanceof SJSocketGroup)
		((SJSocketGroup)s).delegateSession(p, (SJAbstractSocket) obj, encoded);
	    else
		s.delegateSession((SJAbstractSocket) obj, encoded);
	}

    public static void pass(String p, Object obj, String encoded, SJSocket... sockets) throws SJIOException
	{
	    if(sockets[0] instanceof SJSocketGroup)
                ((SJSocketGroup)sockets[0]).delegateSession(p, (SJAbstractSocket) obj, encoded);
            else
                sockets[0].delegateSession((SJAbstractSocket) obj, encoded);
        // TODO multi-session        
	}

    public static SJAbstractSocket receive(String p, String encoded, SJSocket s) throws SJIOException, ClassNotFoundException
    // Session-receive.
	{
	    return receive(p, encoded, SJSessionParameters.DEFAULT_PARAMETERS, s);
	}
	
    public static SJAbstractSocket receive(String p, String encoded, SJSocket... sockets) throws SJIOException, ClassNotFoundException
	{
	    return receive(p, encoded, SJSessionParameters.DEFAULT_PARAMETERS, sockets[0]);
	}
	
    public static SJAbstractSocket receive(String p, String encoded, SJSessionParameters params, SJSocket s) throws SJIOException, ClassNotFoundException // Session-receive.
	{
	    if(s instanceof SJSocketGroup)
		return ((SJSocketGroup)s).receiveSession(p, encoded, params);
	    else
		return s.receiveSession(encoded, params);
	}

    public static SJAbstractSocket receive(String p, String encoded, SJSessionParameters params, SJSocket... sockets) throws SJIOException, ClassNotFoundException
	{
	    if(sockets[0] instanceof SJSocketGroup)
		return ((SJSocketGroup)sockets[0]).receiveSession(p, encoded, params);
	    else
		return sockets[0].receiveSession(encoded, params);
	}

    public static SJAbstractSocket receive(String p, String encoded, int timeout, SJSocket s) throws SJIOException, ClassNotFoundException // Session-receive.
	{
	    return receive(p, encoded, SJSessionParameters.DEFAULT_PARAMETERS, timeout, s);
	}

    public static SJAbstractSocket receive(String p, String encoded, int timeout, SJSocket... sockets) throws SJIOException, ClassNotFoundException
	{
	    return receive(p, encoded, SJSessionParameters.DEFAULT_PARAMETERS, timeout, sockets[0]);
	}

    public static SJAbstractSocket receive(String p, String encoded, SJSessionParameters params, int timeout, SJSocket s) throws SJIOException, ClassNotFoundException // Session-receive.
	{
	    if(s instanceof SJSocketGroup)
		return (SJAbstractSocket) timedReceive(timeout, ((SJSocketGroup)s).getSocket(p), SJRuntimeReceiveTimeout.SESSION, "Session", null, new Object[] {encoded, params});
	    else
		return (SJAbstractSocket) timedReceive(timeout, s, SJRuntimeReceiveTimeout.SESSION, "Session", null, new Object[] {encoded, params});
	}
	
    public static SJAbstractSocket receive(String p, String encoded, SJSessionParameters params, int timeout, SJSocket... sockets) throws SJIOException, ClassNotFoundException
	{
	    return receive(p, encoded, params, timeout, sockets[0]);
	}
	
    public static Object receive(String p, SJSessionParameters params, SJSocket... sockets) throws SJIOException // Dummy compiler targets for session-receive (pre-translation).
	{
		throw new SJRuntimeException("[SJRuntime] Shouldn't get in here.");
	}

    public static Object receive(String p, SJSessionParameters params, SJSocket s) throws SJIOException
	{
		throw new SJRuntimeException("[SJRuntime] Shouldn't get in here.");
	}

    public static Object receive(String p, SJSessionParameters params, int timeout, SJSocket... sockets) throws SJIOException // Dummy compiler targets for session-receive (pre-translation).
	{
		throw new SJRuntimeException("[SJRuntime] Shouldn't get in here.");
	}

    public static Object receive(String p, SJSessionParameters params, int timeout, SJSocket s) throws SJIOException
	{
		throw new SJRuntimeException("[SJRuntime] Shouldn't get in here.");
	}

	public static SJTransportManager getTransportManager()
	{
		return sjtm;
	}	
	
	public static SJSerializer getSerializer(SJSessionParameters params, SJConnection conn) throws SJIOException
	{
		switch (params.getCompatibilityMode())
		{
			case SJ:
			{
				if (conn instanceof SJStreamConnection)			
				{
					return new SJStreamSerializer(conn);
				}
				else //if (conn != null) // FIXME: Delegation case 2? 
				{
					return new SJManualSerializer(conn);
				}
			}
			case CUSTOM:
			{
				// We could bind conn to the message formatter here, but currently the serializer must do it manually.
				
				return new SJCustomSerializer(params.createCustomMessageFormatter(), conn); // Works for both (or rather handles both cases of) "ordinary" and "stream" connections.  
			}
			default:
			{
				throw new SJRuntimeException("[SJRuntime] Unsupported compatability mode: " + params.getCompatibilityMode());
			}
		}
			
		//return null; // Delegation case 2: no connection created between passive party and session acceptor. 
	}

    public static SJSelector selectorFor(SJProtocol proto) {
        return new SJSelectorImpl();
    }
	
	public static int findFreePort() throws SJIOException
	{
		Random rand = new Random();
		
		int retries = 100; // Factor out constant.
		
		synchronized (portsInUse)
		{		
			while (retries-- > 0)
			{
				int port = rand.nextInt(UPPER_PORT_LIMIT - LOWER_PORT_LIMIT) + LOWER_PORT_LIMIT;
				
				if (!portsInUse.contains(port))
				{
					return port;
				}
			}
		}
		
		throw new SJIOException("[SJRuntime] No free port found.");
	}	
	
	public static int takeFreePort() throws SJIOException
    // FIXME: what we need is a routine that finds the port and opens the server immediately
    // to reduce the chance that another process will steal e.g. the TCP port.
    // Should use something like getFreshAcceptorThreadGroup to ensure we have a fresh port,
    // return the port value, and cache the AcceptorThreadGroup somewhere until the port is actually used.
	{
		Random rand = new Random();
		
		int retries = 100;
		
		synchronized (portsInUse)
		{		
			while (retries-- > 0)
			{
				int port = rand.nextInt(UPPER_PORT_LIMIT - LOWER_PORT_LIMIT) + LOWER_PORT_LIMIT;

                if (!portsInUse.contains(port))
				{
					portsInUse.add(port);
					
					return port;
				}
			}
		}
		
		throw new SJIOException("[SJRuntime] No free port found.");
	}	
	
	public static SJPort takeFreeSJPort() throws SJIOException
    // Rename to getFreeSJPort, to better distinguish from reserveFreeSJPort.
	{
		Random rand = new Random();
		
		int retries = 100;
		
		synchronized (portsInUse)
		{		
			while (retries-- > 0)
			{
				int port = rand.nextInt(UPPER_PORT_LIMIT - LOWER_PORT_LIMIT) + LOWER_PORT_LIMIT;

                if (!portsInUse.contains(port))
				{
					return new SJPort(port); // Uses take port to record for portsInUse.
				}
			}
		}
		
		throw new SJIOException("[SJRuntime] No free port found.");
	}
	
	public static void takePort(int port) throws SJIOException
    // FIXME: a bit confusing with takeFreePort and takeFreshSessionPort.
    // Other routines in this class should be modified to use this operation.
	{
		synchronized (portsInUse)
		{
			if (portsInUse.contains(port))
			{
				throw new SJIOException("[SJRuntime] Port already taken: " + port);
			}
			
			portsInUse.add(port);
		}
	}
	
	public static void freePort(int port)
	{
		synchronized (portsInUse)
		{
			portsInUse.remove(port);
		}
	}
	
	public static SJSessionType decodeSessionType(String encoded) throws SJIOException
	{
		synchronized (sjte)
		{
			return sjte.decode(encoded);
		}
	}
	
	// Hacks for bounded-buffer communication.
	
	/*public static void initBoundedBuffer(SJSocket s, int size) throws SJIOException
	{
		SJConnection conn = ((SJAbstractSocket) s).getConnection();
		
		if (!(conn instanceof SJBoundedBufferConnection))
		{
			throw new SJIOException("[SJRuntime] SJBoundedBufferConnection not found: " + conn.getTransportName());
		}
		
		((SJBoundedBufferConnection) conn).initBoundedBuffer(size);
	}*/

	/*public static void sendBB(SJSocket s, Object obj) throws SJIOException
	{
		s.sendBB(obj);
	}
	
	public static void passBB(SJSocket s, Object obj) throws SJIOException
	{
		s.passBB(obj);
	}
	
	public static Object receiveBB(SJSocket s) throws SJIOException, ClassNotFoundException 
	{
		return s.receiveBB();
	}
	
	public static void outlabelBB(SJSocket s, String lab) throws SJIOException // FIXME: this should be automatically eligible for reference passing, need to check how it is currently performed - labels cannot be user modified, and are immutable Strings anyway.
	{
		s.outlabelBB(lab);			
	}
	
	public static String inlabelBB(SJSocket s) throws SJIOException
	{	
		return s.inlabelBB();
	}*/
	
	/*public static boolean recurseBB(SJSocket s, String lab) throws SJIOException // Session-level recurse to a label is translated to a boolean value.
	{
		// return true;
		
		return s.recurseBB(lab);	// If we adopt this in the future, the standard recurse routine should be modified accordingly.	
	}*/	

	public static SJTypeSystem getTypeSystem()
	{
		return sjts;
	}
	
	public static SJSessionType decodeType(String encoded) { 
		synchronized (sjte) // FIXME: temporary work around, examine properly later.
		{
            try {
                return sjte.decode(encoded);
            } catch (SJIOException e) {
                throw new SJRuntimeException(e);
            }
        }
	}

  public static String encode(SJSessionType st) throws SJIOException {
  	synchronized (sjte) // FIXME: temporary work around, examine properly later.
		{
      return sjte.encode(st);
		}
  }
  
  public static void initSocket(SJAbstractSocket s) throws SJIOException
  {
		SJConnection conn = s.getConnection();
		SJSessionParameters params = s.getParameters(); 
		
		SJSerializer ser = SJRuntime.getSerializer(params, conn); // Switches on the compatibility mode to get the appropriate serializer.   	
		
		s.ser = ser; // FIXME: should have options for more user configurability.
		
		switch (params.getCompatibilityMode()) // Now need to get the appropriate session-layer protocol component(s).
		{
			case SJ:
			{
				s.sp = new SJSessionProtocolsImpl(s, ser); // FIXME: should have options for more user configurability. 	
				break;
			}				
			case CUSTOM:
			{
				s.sp = new SJNonSjCompatibilityProtocols(s, ser);
				// Doesn't support e.g. session initiation validation and session delegation. 
				// // FIXME: but there should also be better user control over these options. 
				break;
			}			
			case SECURE:
			{
				s.sp = new SJSecureSessionProtocols(s, ser);
				break;
			}
			default:
			{
				throw new SJRuntimeException("[SJRuntime] Unsupported session compatibility mode: " + params.getCompatibilityMode());
			}
		}

		assert s.ser != null;
		assert s.sp != null;
  }
  
  // This is currently called by SJSessionParameters (at creation time). Anywhere else where it would be better to call it from? E.g. if the user can still configure the SJ Runtime directly (not using a SJSessionParameters), we should check it there as well. Maybe we should check these conditions at session initiation.    
	public static boolean checkSessionParameters(SJSessionParameters params) throws SJSessionParametersException
	{
		SJCompatibilityMode mode = params.getCompatibilityMode();
		//List<SJTransport> negotiationTransports = params.getNegotiationTransports();
		//List<SJTransport> sessionTransports = params.getSessionTransports();		
		
		//FIXME: check that the transports are compatible with the mode. Should probably implement this in terms of some methods in the transport interface.
		
		if (mode == SJCompatibilityMode.CUSTOM)
		{
			if (params.getCustomMessageFormatter() == null)
			{
				throw new SJSessionParametersException("[SJRuntime] Custom compatibility mode requires a SJCustomMessageFormatter.");
			}
		}
		else if (mode == SJCompatibilityMode.SECURE)
		{
			for (SJTransport t : params.getSessionTransports()) // Should also check negotiation transports?
			{
				if (!(t instanceof SJAuthenticatingTransport))
				{
					throw new SJSessionParametersException("[SJRuntime] Secure compatibility mode only valid for SJAuthenticatingTransports.");
				}
			}
		}
		
		return true;
	} 
}

