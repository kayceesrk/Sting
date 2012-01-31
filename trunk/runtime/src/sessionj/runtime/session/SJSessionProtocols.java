/**
 * 
 */
package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.*;
import sessionj.types.sesstypes.SJSessionType;

/**
 * @author Raymond
 *
 * Currently the same as SJSocket, but doesn't need to be.
 *
 */
public interface SJSessionProtocols
{
    Object peekObject() throws SJIOException, ClassNotFoundException, SJControlSignal; //<By MQ>
    void socketGroup(SJSocketGroup sg);  //<By MQ>
    SJSocketGroup socketGroup(); //<By MQ>
    SJContinuationObject expectContinuation() throws SJIOException; //<By MQ>
    byte[] getUnflushedBytes(); //<By MQ>
    void setUnflushedBytes(byte[] b); //<By MQ>

	void accept() throws SJIOException, SJIncompatibleSessionException;
	void request() throws SJIOException, SJIncompatibleSessionException;
	void close();

	// Sending.
    void send(Object o) throws SJIOException;
	void sendByte(byte b) throws SJIOException;
	void sendInt(int i) throws SJIOException;
	void sendBoolean(boolean b) throws SJIOException;
	void sendDouble(double d) throws SJIOException;
	
	void pass(Object o) throws SJIOException;
	
	void copy(Object o) throws SJIOException;
    void flush() throws SJIOException; //<By MQ>
	/*abstract public void copyInt(int i) throws SJIOException;
	abstract public void copyDouble(double d) throws SJIOException;*/
	
	// Receiving.
    Object receive() throws SJIOException, ClassNotFoundException;
    byte receiveByte() throws SJIOException;
	int receiveInt() throws SJIOException; 
    boolean receiveBoolean() throws SJIOException; 
    double receiveDouble() throws SJIOException; 
	
	// Session handling.
    void outlabel(String lab) throws SJIOException;
    String inlabel() throws SJIOException; 
	void outsync(boolean condition) throws SJIOException; // Differs from SJSocket.
    boolean insync() throws SJIOException; 
  boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException;
  boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException;
  boolean interruptibleOutsync(boolean condition) throws SJIOException;
  boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException;

	boolean recursionEnter(String lab) throws SJIOException;
	boolean recursionExit() throws SJIOException;
	boolean recurse(String lab) throws SJIOException;	   
    
	// Higher-order.
  void sendChannel(SJService c, SJSessionType st) throws SJIOException;
	SJService receiveChannel(SJSessionType st) throws SJIOException;
	
	void delegateSession(SJAbstractSocket s, SJSessionType st) throws SJIOException;
	//abstract public SJAbstractSocket receiveSession(SJSessionType st) throws SJIOException;
  SJAbstractSocket receiveSession(SJSessionType st, SJSessionParameters params) throws SJIOException;

  SJSerializer getSerializer();
	void setSerializer(SJSerializer ser);

	/*protected boolean zeroCopySupported()
	{
		return zeroCopySupported;
	}*/
	
	//Hacks for bounded-buffer communication.	
	//abstract public boolean recurseBB(String lab) throws SJIOException;
}
