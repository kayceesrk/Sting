package sessionj.runtime.session;

import java.util.Stack;

import sessionj.types.sesstypes.*;
import sessionj.util.*;

import sessionj.runtime.*;
import sessionj.runtime.session.contexts.*;


/**
 *
 */
public interface SJStateManager // Analogous to SJContext. But only tracks the session types for a single session, as each session has an individual underlying socket, with its own socket state object. // We currently record the type of the session completed so far. An alternative would be to track the concrete operation history: basically, loops unrolled. This might make the lost message resending algorithm simpler. 
{
	public SJRuntimeContextElement currentContext();
	
	public SJSessionType protocolType();
	//private SJSessionType activeType();
	//private SJSessionType implementedType();
	
	public boolean inIterationContext(); // Not sure, but I think this is only used for managing the sent message cache (delimiting "element boundaries" for the resending-delegation protocol).
	
	public SJSessionType currentState(); // i.e. the session type performed so far.	
	//private SJSessionType currentState(int i);

	public SJSessionType expectedType(); 

	public SJSessionType accept() throws SJIOException;
	public SJSessionType request() throws SJIOException;
	//private SJSessionType begin() throws SJIOException
	
	public void open(); // To receive delegated sessions.

	public SJSessionType send(Object obj) throws SJIOException; // Also need a sendSession for type tracking.
	public SJSessionType sendByte(byte b) throws SJIOException;
	public SJSessionType sendInt(int v) throws SJIOException;
	public SJSessionType sendBoolean(boolean v) throws SJIOException;
	public SJSessionType sendDouble(double d) throws SJIOException;
	public SJSessionType sendSession(SJSessionType sjtype) throws SJIOException;
	public SJSessionType sendChannel(SJSessionType sjtype) throws SJIOException;
	
	public SJSessionType receive(Object obj) throws SJIOException;
	public SJSessionType receiveByte(byte b) throws SJIOException;
	public SJSessionType receiveInt(int v) throws SJIOException;	
	public SJSessionType receiveBoolean(boolean v) throws SJIOException;
	public SJSessionType receiveDouble(double d) throws SJIOException;
	public SJSessionType receiveSession(SJSessionType sjtype) throws SJIOException;	
	public SJSessionType receiveChannel(SJSessionType sjtype) throws SJIOException;
		
	public void outbranch(SJLabel lab) throws SJIOException;
	public void inbranch(SJLabel lab) throws SJIOException;
	
	public void outwhile(boolean bool) throws SJIOException; // Could return performed type on false.
	public void inwhile(boolean bool) throws SJIOException;
	
	public void recursion(SJLabel lab) throws SJIOException; // Recursion is "local" (so is checked by compiler), no dynamic check needed (no point to check own actions).
	public SJSessionType recurse(SJLabel lab) throws SJIOException; // Recursion is "local" (so is checked by compiler), no dynamic check needed (no point to check own actions).

	public void close();
	public void reset();

}
