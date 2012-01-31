package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.SJConnection;
import sessionj.runtime.transport.SJTransport;
import sessionj.runtime.transport.SJTransportManager;
import sessionj.runtime.transport.sharedmem.SJBoundedFifoPair;
import sessionj.runtime.transport.tcp.InputState;
import sessionj.runtime.transport.tcp.WaitInitialInputIfNeeded;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Raymond
 *
 * FIXME: the default is mostly a performance hack. It can't be used to e.g. see which transports were actually used (the ones that were the defaults at the time). // RAY: the defaults is no longer a hack now, but it may be slower since we have to create at least a new list of (pointers to) transport components every time. 
 * 
 * Can either store the transport classes here and load the components later at session init., or can load the transport components eagerly here and keep pointers to the components. (Currently neither, SJTransportUtils is doing the loading for us for the latter option; this needs to be refactored.)
 * 
 */
public class SJSessionParameters 
{
	public static /*final*/ SJSessionParameters DEFAULT_PARAMETERS; // The very initial defaults, i.e. the default transports when the SJR is first initialised. Transports can be added to the SJR after initialisation, but this defaults value remains unchanged.    
	
	static 
	{
		try
		{
			DEFAULT_PARAMETERS = new SJSessionParameters(); // Can we gain performance by moving exception raising parts out of here so can be declared final again?
		}
		catch (SJSessionParametersException spe)
		{
			throw new SJRuntimeException("[SJSessionParameters] Shouldn't get in here.", spe);
		}
	}
	
	// These are given by the user.
	private List<Class<? extends SJTransport>> negotiationTransportClasses;  
	private List<Class<? extends SJTransport>> sessionTransportClasses;
	
	// These are filled by the constructors of this class and used by the transport manager.
	private List<SJTransport> negotiationTransports;
	private List<SJTransport> sessionTransports;

	private int boundedBufferSize = SJBoundedFifoPair.UNBOUNDED_BUFFER_SIZE;
  
	//private static final Logger logger = Logger.getLogger(SJSessionParameters.class.getName());
	
	private SJCompatibilityMode mode; // The default mode. Uses SJStreamSerializer where possible, SJManualSerialier otherwise. 
	
	//private SJCustomMessageFormatter cmf;
	private Class<? extends SJCustomMessageFormatter> cmf;
    private SJConnection conn = null;

	// HACK: SJSessionParameters are supposed to be user-configurable parameters.
  // But now using as a convenient place to store pseudo compiler-generated optimisation
  // information for now. Would be better to make a dedicated object for storing such information.
  // But that could be slow. // Factor out constant more generally?
	public SJSessionParameters() throws SJSessionParametersException
	{
		//this(defaultNegClasses(), defaultSessionClasses());
		this(SJCompatibilityMode.SJ, defaultNegClasses(), defaultSessionClasses(), null);
	}
  
	// This is the "main" constructor: others are variants based on this. 
	public SJSessionParameters(SJCompatibilityMode mode, List<Class<? extends SJTransport>> negotiationTransportClasses, List<Class<? extends SJTransport>> sessionTransportClasses, Class<? extends SJCustomMessageFormatter> cmf) throws SJSessionParametersException
	{
		this.mode = mode;

		this.negotiationTransportClasses = negotiationTransportClasses;
		this.sessionTransportClasses = sessionTransportClasses;
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		List<SJTransport> nts; 
		List<SJTransport> sts; 		

		try
		{
			nts = sjtm.loadNegotiationTransports(negotiationTransportClasses);
			sts = sjtm.loadSessionTransports(sessionTransportClasses);
		}
		catch (SJIOException ioe)
		{
			throw new SJSessionParametersException(ioe);
		}
		
    this.negotiationTransports = Collections.unmodifiableList(nts); // The TransportPreferenceList already makes a new list (although not unmodifiable).
    this.sessionTransports = Collections.unmodifiableList(sts);
		/*this.negotiationTransports = nts;  
    this.sessionTransports = sts;*/
    
		this.cmf = cmf;
		
		if (!SJRuntime.checkSessionParameters(this)) // Maybe should check more "lazily" at session initiation. Might be a bit convenient from an exception handling point of view. 
		{
			//throw new... // SJRuntime.checkSessionParameters is already raising appropriate exceptions.
		}
	}
 
	public SJSessionParameters(List<Class<? extends SJTransport>> negotiationTransportClasses, List<Class<? extends SJTransport>> sessionTransportClasses) throws SJSessionParametersException
	{
		this(SJCompatibilityMode.SJ, negotiationTransportClasses, sessionTransportClasses, null); // SJ is the default mode. Uses SJStreamSerializer where possible, SJManualSerialier otherwise.
	}

	// FIXME: should be generalised to support custom "deserializers" for other wire formats. Well, in principle, the programmer should add a custom SJSerializer. But this interface may be easier to use than a full serializer implemetation.
	//public SJSessionParameters(SJCompatibilityMode mode, SJCustomMessageFormatter cmf) throws SJSessionParametersException
	public SJSessionParameters(SJCompatibilityMode mode, Class<? extends SJCustomMessageFormatter> cmf) throws SJSessionParametersException
	{
		this(mode, defaultNegClasses(), defaultSessionClasses(), cmf); 
	}		
	
	public SJSessionParameters(SJCompatibilityMode mode, List<Class<? extends SJTransport>> negotiationTransportClasses, List<Class<? extends SJTransport>> sessionTransportClasses) throws SJSessionParametersException
	{
		this(mode, negotiationTransportClasses, sessionTransportClasses, null);
	}

	public SJSessionParameters(SJCompatibilityMode mode) throws SJSessionParametersException
	{
		this(mode, defaultNegClasses(), defaultSessionClasses()); 
	}
	
	public SJSessionParameters(List<Class<? extends SJTransport>> negotiationTransportClasses, List<Class<? extends SJTransport>> sessionTransportClasses, int boundedBufferSize) throws SJSessionParametersException
	{
		this(negotiationTransportClasses, sessionTransportClasses); //FIXME: "bounded-buffers" should be a mode (shouldn't be SJ default).
		
		this.boundedBufferSize = boundedBufferSize;
	}	
	
	public SJSessionParameters(int boundedBufferSize) throws SJSessionParametersException
	{
		this();
		
		this.boundedBufferSize = boundedBufferSize;
	}	
	
	public List<Class<? extends SJTransport>> getNegotiationTransportClassess()
	{
		return negotiationTransportClasses;
	}
	
	public List<Class<? extends SJTransport>> getSessionTransportsClasses()
	{
		return sessionTransportClasses;
	}	
	
	public List<SJTransport> getNegotiationTransports()
	{
		// already unmodifiableList.
		//noinspection ReturnOfCollectionOrArrayField
		return negotiationTransports;
	}
	
	public List<SJTransport> getSessionTransports()
	{
		// already unmodifiableList.
		//noinspection ReturnOfCollectionOrArrayField
		return sessionTransports;
	}
	
	@Override
	public String toString() {
		return "SJSessionParameters{" +
			"negotiationTransportClasses=" + negotiationTransportClasses +
			", sessionTransportClasses=" + sessionTransportClasses +
			", negotiationTransports=" + negotiationTransports +
			", sessionTransports=" + sessionTransports +
			", boundedBufferSize=" + boundedBufferSize +
			", mode=" + mode +
			", cmf=" + cmf +
			", conn=" + conn +
			'}';
	}

	public int getBoundedBufferSize()
    {
        return boundedBufferSize;
    }

    public SJCompatibilityMode getCompatibilityMode()
    {
        return mode;
    }

    protected Class<? extends SJCustomMessageFormatter> getCustomMessageFormatter()
    {
        return cmf;
    }

    public SJCustomMessageFormatter createCustomMessageFormatter() throws SJIOException
    {
        try
        {
            return cmf.newInstance();
        }
        catch (IllegalAccessException iae)
        {
            throw new SJIOException(iae);
        }
        catch (InstantiationException ie)
        {
            throw new SJIOException(ie);
        }
    }

    private static List<Class<? extends SJTransport>> defaultSessionClasses()
    {
        return SJRuntime.getTransportManager().defaultSessionTransportClasses();
    }

    private static List<Class<? extends SJTransport>> defaultNegClasses()
    {
        return SJRuntime.getTransportManager().defaultNegotiationTransportClasses();
    }

    public SJDeserializer createDeserializer() throws SJIOException {
        if (cmf == null) return new SJManualDeserializer();
        else return new CustomMessageFormatterFactory(createCustomMessageFormatter());
    }

    public SJAcceptProtocol getAcceptProtocol() {
        if (cmf == null) return new SJAcceptProtocolImpl();
        // The custom mode has no accept protocol messages to wait for.
        else return new SJAcceptProtocol() {
            public InputState initialAcceptState(SJServerSocket serverSocket) throws SJIOException, SJIncompatibleSessionException {
                return new WaitInitialInputIfNeeded(serverSocket.accept());
            }
        };
    }

	// HACK: The following 2 methods serve to avoid a race condition in SJServerSocket.nextConnection().
	// When we receive several connections at the same time, we need a way to pair
	// the SJServerSockets and the actual underlying SJConnections.
	// See also DefaultSJProtocolAcceptState
    public void setExpectedConnectionHack(SJConnection conn) {
        this.conn = conn;
    }
    
    public boolean validConnection(SJConnection conn) {
        return this.conn == null || conn.equals(this.conn);
    }
}
