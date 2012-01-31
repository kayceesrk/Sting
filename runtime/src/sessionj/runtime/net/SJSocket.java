package sessionj.runtime.net;

import sessionj.runtime.*;
import sessionj.runtime.session.SJStateManager;
import sessionj.runtime.transport.SJConnection;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.runtime.session.*; //<By MQ>

public interface SJSocket extends SJChannel, SJSelectableChannel
{
    //<By MQ>
    void flush() throws SJIOException;
    Object peekObject() throws SJIOException, ClassNotFoundException, SJControlSignal;
    void socketGroup(SJSocketGroup sg);
    SJSocketGroup socketGroup();
    SJContinuationObject expectContinuation() throws SJIOException;
    byte[] getUnflushedBytes();
    public void setUnflushedBytes(byte[] b);
    //</By MQ>

   	void close();
	
	// Sending.
    void send(Object o) throws SJIOException;
	void sendInt(int i) throws SJIOException;
	void sendBoolean(boolean b) throws SJIOException;
	void sendDouble(double d) throws SJIOException;
	
	void pass(Object o) throws SJIOException;
	
	void copy(Object o) throws SJIOException;
	
	// Receiving.
	Object receive() throws SJIOException, ClassNotFoundException;
	int receiveInt() throws SJIOException;
	boolean receiveBoolean() throws SJIOException;
	double receiveDouble() throws SJIOException;
	
	// Session handling.
	void outlabel(String lab) throws SJIOException;
	String inlabel() throws SJIOException;
	boolean outsync(boolean condition) throws SJIOException;
	boolean insync() throws SJIOException;
	
	boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException;
	boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException;
	boolean interruptibleOutsync(boolean condition) throws SJIOException;
	boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException;

	boolean recursionEnter(String lab) throws SJIOException;
	boolean recursionExit() throws SJIOException;
	boolean recurse(String lab) throws SJIOException;	
	
	// Higher-order.
	void sendChannel(SJService c, String encoded) throws SJIOException;
	SJService receiveChannel(String encoded) throws SJIOException;
	
	void delegateSession(SJAbstractSocket s, String encoded) throws SJIOException;
    SJAbstractSocket receiveSession(String encoded, SJSessionParameters params) throws SJIOException;
	
  // for the following "non session typed" methods on SJSocket, need to add as "exceptions" to SJSessionOperationParser  
    
	SJProtocol getProtocol();
	
	String getHostName();
	int getPort();
	
	String getLocalHostName();
	int getLocalPort();
	
	SJSessionParameters getParameters();
	
	//Hacks for bounded-buffer communication.
	
	//public boolean recurseBB(String lab) throws SJIOException;

    SJConnection getConnection();

    void reconnect(SJConnection connection) throws SJIOException;

    void setHostName(String hostAddress);

    void setPort(int port);

    SJStateManager getStateManager();
    void setStateManager(SJStateManager sm);
  
    SJSessionType currentSessionType(); // Session actions performed so far (modulo loop types).
    SJSessionType remainingSessionType();

    boolean typeStartsWithOutput() throws SJIOException;

    SJSessionType getInitialRuntimeType() throws SJIOException;

	boolean supportsBlocking();

	boolean arrived();

	TransportSelector transportSelector();
}
