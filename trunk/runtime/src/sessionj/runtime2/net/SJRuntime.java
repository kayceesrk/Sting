package sessionj.runtime2.net;

/*import java.io.*;
import java.net.*;*/
import java.util.*;

import polyglot.types.SemanticException;

import sessionj.ExtensionInfo;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJSessionType;

import sessionj.runtime2.*;
//import sessionj.runtime2.service.*;
//import sessionj.runtime2.transport.*;
//import sessionj.runtime2.net.service.SJServiceComponent;
import sessionj.runtime2.service.SJBootstrapService;
import sessionj.runtime2.service.SJServiceComponent;
import sessionj.runtime2.util.*;

public abstract class SJRuntime extends SJComponent_c implements SJComponent
{
	public static final SJRuntime SJ_RUNTIME = new SJRuntime_c();
	//private static final SJTransportManager SJ_TRANSPORT_MANAGER = new SJTransportManager_c();
	
	private	final ExtensionInfo extInfo;
	private final SJTypeSystem sjts;
	private final SJClassResolver sjcr;
	private final SJRuntimeTypeEncoder sjrte;
	
	protected SJRuntime(SJComponentId cid) 
	{
		super(cid);
		
		try
		{
			extInfo = new ExtensionInfo();
			sjts = (SJTypeSystem) extInfo.typeSystem();
			sjcr = new SJClassResolver(sjts, extInfo);
			sjts.initialize(sjcr, extInfo);
			sjrte = new SJRuntimeTypeEncoder(sjts);
		}
		catch (SemanticException se)
		{
			throw new SJRuntimeException(se);
		}
	}
		
	/*abstract public void reset(SJBootstrapService bs);
	abstract public void reset(SJBootstrapService bs, List<Class<?>> services);
	abstract public void reset(List<Class<?>> services);*/
	
	abstract public void acceptSession(SJSocket s) throws SJIOException;
	abstract public void requestSession(SJSocket s) throws SJIOException;	
	abstract public void closeSession(SJSocket s)/* throws SJIOException*/;
	
	/*abstract protected SJServiceComponent getService(SJComponentId cid);
	abstract protected void addService(SJServiceComponent sc);
	abstract protected void addService(SJComponentId cid, SJServiceComponent sc);*/
	
	/*public static SJTransportManager getTransportManager()
	{
		return SJ_TRANSORT_MANAGER;
	}*/
	
	/*public static final SJTypeSystem getTypeSystem()
	{
		return sjts;
	}*/

	public final SJRuntimeTypeEncoder getRuntimeTypeEncoder()
	{
		return sjrte;
	}
	
	//abstract public SJAcceptorThreadGroup getFreshAcceptorThreadGroup(SJSessionParameters params) throws SJIOException; // Used to receive sessions.

	/*abstract public void takePort(int port) throws SJIOException; // FIXME: a bit confusing with takeFreePort and takeFreshSessionPort. // Other routines in this class should be modified to use this operation.
	
	abstract protected int findFreePort() throws SJIOException;
	abstract protected int takeFreePort() throws SJIOException; // FIXME: what we need is a routine that finds the port and opens the server immediately to reduce the chance that another process will steal e.g. the TCP port. // Should use something like getFreshAcceptorThreadGroup to ensure we have a fresh port, return the port value, and cache the AcceptorThreadGroup somewhere until the port is actually used.	
	
	//abstract public SJServerPort getFreeSJPort() throws SJIOException; // Rename to getFreeSJPort, to better distinguish from reserveFreeSJPort.
	abstract public SJServerPort reserveFreeSJPort() throws SJIOException;
	abstract public SJServerPort reserveFreeSJPort(SJSessionParameters params) throws SJIOException; // Unlike takeFreeSJPort, this actually reserves the setups (guarantees port is open).

	abstract public void freePort(int port);

	//abstract public SJSerializer getSerializer(SJConnection conn) throws SJIOException;
	
	abstract public SJSessionType decodeSessionType(String encoded) throws SJIOException;
	
	abstract public void openServerSocket(SJServerSocket ss) throws SJIOException;
	abstract public void closeServerSocket(SJServerSocket ss);
	
	//abstract public void bindSocket(SJSocket s, SJConnection conn) throws SJIOException; // Currently, conn can be null (delegation case 2 reconnection).
	abstract public void connectSocket(SJSocket s) throws SJIOException;
	abstract public void reconnectSocket(SJSocket s, String hostName, int port) throws SJIOException;
	abstract public void closeSocket(SJSocket s);
	
	// Initiation service.
	abstract protected void accept(SJSocket s) throws SJIOException, SJIncompatibleSessionException;
	abstract protected void request(SJSocket s) throws SJIOException, SJIncompatibleSessionException;
	
	// Close service.
	abstract public void close(SJSocket s);	
	
	// Send service.
	abstract public void send(SJSocket s, Object obj) throws SJIOException;
	abstract public void send(SJSocket s, int i) throws SJIOException;
	abstract public void send(SJSocket s, boolean b) throws SJIOException;	
	abstract public void send(SJSocket s, double d) throws SJIOException;
	
	// Pass service.
	abstract public void pass(SJSocket s, Object obj) throws SJIOException;
	
	// Copy service.
	abstract public void copy(SJSocket s, Object obj) throws SJIOException;
	
	// Receive service.
	abstract public Object receive(SJSocket s) throws SJIOException, ClassNotFoundException; // Remove array in a compiler pass.
	/*abstract public int receiveInt(SJSocket s) throws SJIOException;	
	abstract public boolean receiveBoolean(SJSocket s) throws SJIOException;
	abstract public double receiveDouble(SJSocket s) throws SJIOException;
	
	// Service passing service.
	abstract public void copy(SJSocket s, Object obj, String encoded) throws SJIOException; // Shared-channel passing.
	abstract public SJService receiveChannel(SJSocket s, String encoded) throws SJIOException; // Needs a different name to session-receive - arguments are the same. // No ClassNotFoundException means runtime errors (from malicious peers) due to receiving unexpected and unknown object classes must be converted to IO errors. // Actually, no ClassNotFoundException here doesn't seem to make much difference, the base type checker uses the "ordinary" receive. // Actually, no ClassNotFoundException here breaks the code generation javac pass...
	
	// Session delegation service.
	abstract public void pass(SJSocket s, Object obj, String encoded) throws SJIOException; // Session delegation. Probably better to rename to something more obvious here as well.
	abstract public SJSocket receive(SJSocket s, String encoded) throws SJIOException; // Session-receive.
	abstract public SJSocket receive(SJSocket s, String encoded, SJSessionParameters params) throws SJIOException; // Session-receive.
		
	// Loop synchronization service.
	abstract public boolean insync(SJSocket s) throws SJIOException;
	abstract public boolean outsync(SJSocket s, boolean cond) throws SJIOException;
	
	// Label communication service.
	abstract public String inlabel(SJSocket s) throws SJIOException;
	abstract public void outlabel(SJSocket s, String lab) throws SJIOException;
	
	// Local recursion services.
	abstract public boolean recurse(SJSocket s) throws SJIOException; // Session-level recurse to a label is translated to a boolean value.
	abstract public boolean recursionEnter(SJSocket s);
	abstract public void recursionExit(SJSocket s);
	
	abstract public void close(SJSocket[] sockets);
	
	abstract public void send(SJSocket[] sockets, Object obj) throws SJIOException;
	abstract public void send(SJSocket[] sockets, int i) throws SJIOException;
	abstract public void send(SJSocket[] sockets, boolean b) throws SJIOException;
	abstract public void send(SJSocket[] sockets, double d) throws SJIOException;
	
	abstract public void pass(SJSocket[] sockets, Object obj) throws SJIOException;	
	abstract public void pass(SJSocket[] sockets, Object obj, String encoded) throws SJIOException;
	
	abstract public void copy(SJSocket[] sockets, Object obj) throws SJIOException;	
	abstract public void copy(SJSocket[] sockets, Object obj, String encoded) throws SJIOException;	
	
	abstract public Object receive(SJSocket[] sockets) throws SJIOException, ClassNotFoundException; // Remove array in a compiler pass.
	abstract public int receiveInt(SJSocket[] sockets) throws SJIOException;
	abstract public boolean receiveBoolean(SJSocket[] sockets) throws SJIOException;
	abstract public double receiveDouble(SJSocket[] sockets) throws SJIOException;
	abstract public SJService receiveChannel(SJSocket[] sockets, String encoded) throws SJIOException; // Needs a different name to session-receive - arguments are the same.
	abstract public SJSocket receive(SJSocket[] sockets, String encoded) throws SJIOException;
	abstract public SJSocket receive(SJSocket[] sockets, String encoded, SJSessionParameters params) throws SJIOException;
	
	abstract public boolean recurse(SJSocket[] sockets) throws SJIOException;
	abstract public boolean insync(SJSocket[] sockets) throws SJIOException;
	abstract public boolean outsync(SJSocket[] sockets, boolean cond) throws SJIOException;
	abstract public String inlabel(SJSocket[] sockets) throws SJIOException;
	abstract public void outlabel(SJSocket[] sockets, String lab) throws SJIOException;
	abstract public boolean recursionEnter(SJSocket[] sockets);
	abstract public void recursionExit(SJSocket[] sockets);
	
	abstract public Object receive(SJSocket s, int timeout) throws SJIOException; // FIXME: only half done so far. Need to do for remaining ops.: accept, compound ops., etc. Currently relies on implicit close to terminate the SJRuntimeReceiveTimeout thread that is still blocked on the receive operation - need to check this works for all transports (check that an exception from early close is propagated up properly). Also, need more work on making SJTimeout a terminal exception - don't do FIN protocol (on our side at least).
	abstract public int receiveInt(SJSocket s, int timeout) throws SJIOException;
	abstract public boolean receiveBoolean(SJSocket s, int timeout) throws SJIOException;
	abstract public double receiveDouble(SJSocket s, int timeout) throws SJIOException;
	abstract public SJService receiveChannel(SJSocket s, String encoded, int timeout) throws SJIOException; // Session-receive.
	abstract public SJSocket receive(SJSocket s, String encoded, int timeout) throws SJIOException; // Session-receive.
	abstract public SJSocket receive(SJSocket s, String encoded, SJSessionParameters params, int timeout) throws SJIOException; // Session-receive.
	
	abstract public Object receive(SJSocket[] sockets, int timeout) throws SJIOException;
	abstract public int receiveInt(SJSocket[] sockets, int timeout) throws SJIOException;	
	abstract public boolean receiveBoolean(SJSocket[] sockets, int timeout) throws SJIOException;
	abstract public double receiveDouble(SJSocket[] sockets, int timeout) throws SJIOException;
	abstract public SJService receiveChannel(SJSocket[] sockets, String encoded, int timeout) throws SJIOException;
	abstract public SJSocket receive(SJSocket[] sockets, String encoded, int timeout) throws SJIOException;
	abstract public SJSocket receive(SJSocket[] sockets, String encoded, SJSessionParameters params, int timeout) throws SJIOException;

	abstract public Object receive(SJSocket[] sockets, SJSessionParameters params) throws SJIOException; // Dummy compiler targets for session-receive (pre-translation).
	abstract public Object receive(SJSocket s, SJSessionParameters params) throws SJIOException;	
	abstract public Object receive(SJSocket[] sockets, SJSessionParameters params, int timeout) throws SJIOException; 
	abstract public Object receive(SJSocket s, SJSessionParameters params, int timeout) throws SJIOException;*/
}
