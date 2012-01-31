package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.session.SJSerializer;
import sessionj.runtime.session.SJSessionProtocols;
import sessionj.runtime.session.SJStateManager;
import sessionj.runtime.transport.SJConnection;
import sessionj.types.sesstypes.SJSessionType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import sessionj.runtime.session.*; //<By MQ>

abstract public class SJAbstractSocket implements SJSocket
{
	private SJStateManager sm; // A runtime version of SJContext, specialised for a single session.
	
	private SJProtocol protocol;
    private final SJSessionParameters params;
	
	private String hostName;
	private int port;
	
	private String localHostName; // Session-level values.
	private int localPort;
	
	private SJConnection conn;
	
	protected SJSessionProtocols sp; // Hacked access for SJRuntime.
	protected SJSerializer ser;
	
	private boolean isActive = false;
    private final SJSessionType receivedRuntimeType;

    protected SJAbstractSocket(SJProtocol protocol, SJSessionParameters params)
        throws SJIOException
    {
        this(protocol, params, null);
    }

    public SJAbstractSocket(SJProtocol protocol, SJSessionParameters params, SJSessionType receivedRuntimeType) throws SJIOException {
        this.protocol = protocol; // Remainder of initialisation for client sockets performed when init is called.
        this.params = params;

        try
        {
            localHostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            throw new SJIOException(e);
        }

        //this.sm = new SJStateManager_c(SJRuntime.getTypeSystem(), protocol.type()); // Replaced by a setter (called by SJProtocolsImp).
        this.receivedRuntimeType = receivedRuntimeType;
    }

    // FIXME: refactor the initialisation/binding of SJR session and transport components to session sockets. Move decision making more explicitly into the SJR and make more modular. 
	protected void init(SJConnection conn) throws SJIOException // conn can be null (delegation case 2?).
	{
		this.conn = conn;
		
		SJRuntime.initSocket(this);
	}

    //<By MQ>
    public void setUnflushedBytes(byte[] b)
    {
        sp.setUnflushedBytes(b);
    }

    public byte[] getUnflushedBytes()
    {
	return sp.getUnflushedBytes();
    }

    public void socketGroup(SJSocketGroup sg)
    {
	sp.socketGroup(sg);
    }

    public SJSocketGroup socketGroup()
    {
	return sp.socketGroup();
    }

    public void flush() throws SJIOException
    {
	sp.flush();
    }

    public Object peekObject() throws SJIOException, ClassNotFoundException, SJControlSignal
    {
	return sp.peekObject();
    }

    public SJContinuationObject expectContinuation() throws SJIOException
    {
	return sp.expectContinuation();
    }
    //<By MQ>
    public void reconnect(SJConnection conn) throws SJIOException
        {      
		ser.close();
		
		this.conn = conn;
		
		ser = SJRuntime.getSerializer(params, conn); // FIXME: refactor to make cleaner and more modular with socket initialisation routines (now in SJRuntime).
		sp.setSerializer(ser);
	}
	
	protected void accept() throws SJIOException, SJIncompatibleSessionException
	{		
		sp.accept();
	}
	
	protected void request() throws SJIOException, SJIncompatibleSessionException
	{		
		sp.request();
	}

	public void close() // FIXME: not compatible with delegation.
	{
		sp.close(); 
	}

	public void send(Object o) throws SJIOException
	{
		sp.send(o);
	}
	
	public void sendInt(int i) throws SJIOException
	{
		sp.sendInt(i);
	}

	public void sendBoolean(boolean b) throws SJIOException
	{
		sp.sendBoolean(b);
	}
	
	public void sendDouble(double d) throws SJIOException
	{
		sp.sendDouble(d);
	}
	
	public void pass(Object o) throws SJIOException
	{
		sp.pass(o);
	}
	
	public void copy(Object o) throws SJIOException
	{
		sp.copy(o);
	}
	
	public Object receive() throws SJIOException, ClassNotFoundException
	{
		return sp.receive();
	}
	
	public int receiveInt() throws SJIOException
	{
		return sp.receiveInt();
	}

	public boolean receiveBoolean() throws SJIOException
	{
		return sp.receiveBoolean();
	}
	
	public double receiveDouble() throws SJIOException
	{
		return sp.receiveDouble();
	}
	
	public void outlabel(String lab) throws SJIOException
	{
		sp.outlabel(lab);
	}
	
	public String inlabel() throws SJIOException
	{
		return sp.inlabel();
	}
	
	public boolean outsync(boolean b) throws SJIOException
	{
		sp.outsync(b);
		
		return b;
	}
	
	public boolean insync() throws SJIOException
	{
		return sp.insync();
	}

	public boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException 
	{
		return sp.isPeerInterruptibleOut(selfInterrupting);
	}
	
	public boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException 
	{
		return sp.isPeerInterruptingIn(selfInterruptible);
	}
	
	public boolean interruptibleOutsync(boolean condition) throws SJIOException 
	{
		return sp.interruptibleOutsync(condition);
	}
	
	public boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException 
	{
		return sp.interruptingInsync(condition, peerInterruptible);
	}

	public boolean recursionEnter(String lab) throws SJIOException
	{
		return sp.recursionEnter(lab);
	}
	
	public boolean recursionExit() throws SJIOException
	{
		return sp.recursionExit();
	}
	
	public boolean recurse(String lab) throws SJIOException
	{
		return sp.recurse(lab);
	}
  
  public void sendChannel(SJService c, String encoded) throws SJIOException
	{
		//sp.sendChannel(c, SJRuntime.getTypeEncoder().decode(c.getProtocol().encoded()));
		sp.sendChannel(c, SJRuntime.decodeType(encoded));
	}
	
	public SJService receiveChannel(String encoded) throws SJIOException
	{
		return sp.receiveChannel(SJRuntime.decodeType(encoded));
	}
	
	public void delegateSession(SJAbstractSocket s, String encoded) throws SJIOException
	{
		sp.delegateSession(s, SJRuntime.decodeType(encoded));
	}
	
	//public SJAbstractSocket receiveSession(String encoded) throws SJIOException
	public SJAbstractSocket receiveSession(String encoded, SJSessionParameters params) throws SJIOException
	{
		return sp.receiveSession(SJRuntime.decodeType(encoded), params);
	}
	
	public boolean isActive()
	{
		return isActive;
	}

	protected void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}
		
	//protected SJConnection getConnection()
	public SJConnection getConnection()
	{
		return conn;
	}
	
	//protected SJSerializer getSerializer()
	public SJSerializer getSerializer()
	{
		return ser;
	}
	
	public SJSessionProtocols getSJSessionProtocols() // Access modifier too open.
	{
		return sp;
	}

	public SJProtocol getProtocol()
	{
		return protocol;
	}
	
	public String getHostName()
	{
		return hostName;
	}
	
	public void setHostName(String hostName) // Access by users disallowed by compiler.
	{
		this.hostName = hostName;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public String getLocalHostName()
	{
		return localHostName;
	}	
	
	public int getLocalPort()
	{
		return localPort;
	}
	
	protected void setLocalHostName(String localHostName)
	{
		this.localHostName = localHostName;
	}
	
	protected void setLocalPort(int localPort)
	{
		this.localPort = localPort;
	}
	
	public SJSessionParameters getParameters()
	{
		return params;		
	}
	
	// Hacks for bounded-buffer communication.

	/*public void sendBB(Object o) throws SJIOException
	{
		sp.sendBB(o);
	}
	
	public void passBB(Object o) throws SJIOException
	{
		sp.passBB(o);
	}
	
	public Object receiveBB() throws SJIOException, ClassNotFoundException
	{
		return sp.receiveBB();
	}
	
	public void outlabelBB(String lab) throws SJIOException
	{
		sp.outlabelBB(lab);
	}
	
	public String inlabelBB() throws SJIOException
	{
		return sp.inlabelBB();
	}*/
	
	/*public boolean recurseBB(String lab) throws SJIOException
	{
		return sp.recurseBB(lab);
	}*/

  /**
   * For zero-copy delegation: receiver needs to keep its own static type
   */
  public void updateStaticAndRuntimeTypes(SJSessionType staticType, SJSessionType runtimeType) throws SJIOException {
            protocol = new SJProtocol(SJRuntime.encode(staticType));  //<By MQ> commented so that all sockets use the same protocol
  }

  public abstract boolean isOriginalRequestor();
  
  public SJStateManager getStateManager()
  {
  	return sm;
  }
  
  public void setStateManager(SJStateManager sm)
  {
  	this.sm = sm;
  }
  
  public SJSessionType currentSessionType()
  {
  	return getStateManager().currentState(); // FIXME: state manager needs to use proper unrolling of loop types.
  }
    
  public SJSessionType remainingSessionType() // Returns null if the runtime context is empty (session completed, or due to errors/bugs).
  {
  	return getStateManager().expectedType(); // FIXME: state manager needs to use proper unrolling of loop types.
  }

    public boolean typeStartsWithOutput() throws SJIOException {
        return protocol.type().child().startsWithOutput(); 
    }

    public SJSessionType getInitialRuntimeType() throws SJIOException {
        return receivedRuntimeType == null ? protocol.type() : receivedRuntimeType;
    }

	public boolean supportsBlocking() {
		return conn.supportsBlocking();
	}

	public boolean arrived() {
		return conn.arrived();
	}

	public TransportSelector transportSelector() {
		return conn.getTransport().transportSelector();
	}

	@Override
    public String toString() {
        return getClass().getSimpleName() + '{' +
            "protocol=" + protocol +
            ", conn=" + conn +
            ", params=" + params +
            ", hostName='" + hostName + '\'' +
            ", port=" + port +
            ", localHostName='" + localHostName + '\'' +
            ", localPort=" + localPort +
            ", isActive=" + isActive +
            '}';
    }
}
