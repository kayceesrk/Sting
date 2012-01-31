/**
 * 
 */
package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.net.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJLabel;

/**
 * @author Raymond
 *
 * This class encapsulates the protocols for "backwards-compatibility" with non-SJ peers. 
 * 
 * FIXME: should generally make such options more modular and convenient for the user, e.g. wrt. to disabling session initiation and delegation.
 *
 * FIXME: need to consider situations such as a default-SJ peer connecting to a non-SJ-compatibility peer, etc. 
 *  
 */
public class SJNonSjCompatibilityProtocols implements SJSessionProtocols
{ 
	private static final boolean RUNTIME_MONITORING = true; // Basically a necessity for this session mode.
	
	private final SJSocket s;
	private SJSerializer ser;
    
	private SJStateManager sm; 
    
  public SJNonSjCompatibilityProtocols(SJSocket s, SJSerializer ser)
	{
		this.s = s;
		this.ser = ser;
		
		if (RUNTIME_MONITORING)
		{
			try
			{
				sm = new SJStateManager_c(SJRuntime.getTypeSystem(), s.getInitialRuntimeType());
				
				s.setStateManager(sm);
			}
			catch (SJIOException ioe)
			{
				throw new SJRuntimeException("[SJProtocolsImpl] Shouldn't get in here: ", ioe);
			}
	  }
	}

    //<By MQ> MQTODO: need to support batching here?
    public SJContinuationObject expectContinuation() throws SJIOException
    {
        throw new SJIOException("SJNonCompatibilityProtocols: expectContinuation() is not supported yet.");
    }

    public void setUnflushedBytes(byte[] b)
    {
        //Throw new SJIOException("SJNonCompatibilityProtocols: flush is not supported yet.");
        System.out.println("SJNonCompatibilityProtocols: setUnflushedBytes() is not supported yet.");
    }

    public byte[] getUnflushedBytes()
    {
        //Throw new SJIOException("SJNonCompatibilityProtocols: flush is not supported yet.");
        System.out.println("SJNonCompatibilityProtocols: getUnflushedBytes() is not supported yet.");
	return null;
    }

    public void flush()
    {
	//Throw new SJIOException("SJNonCompatibilityProtocols: flush is not supported yet.");
	System.out.println("SJNonCompatibilityProtocols: flush() is not supported yet.");
    }

    public Object peekObject() throws SJIOException, ClassNotFoundException, SJControlSignal
    {
	throw new SJIOException("SJNonCompatibilityProtocols: peekObject is not supported yet.");
    }

    public void socketGroup(SJSocketGroup sg)
    {
        //throw new SJIOException("SJSecureSessionsProtocols: socketGroup is not supported yet.");
	System.out.println("SJNonCompatibilityProtocols: socketGroup(SJSocketGroup) is not supported yet.");
    }

    public SJSocketGroup socketGroup()
    {
        //throw new SJIOException("SJSecureSessionsProtocols: socketGroup is not supported yet.");
	System.out.println("SJNonCompatibilityProtocols: socketGroup() is not supported yet.");
	return null;
    }
    //</By MQ>
	public void accept() throws SJIOException, SJIncompatibleSessionException
	{
		if (RUNTIME_MONITORING)
		{
			sm.accept();
		}			
	}
	
	public void request() throws SJIOException, SJIncompatibleSessionException
	{
		if (RUNTIME_MONITORING)
		{
			sm.request();
		}		
	}
	
	public void close()
	{		
		if (RUNTIME_MONITORING)
		{
			sm.close();
		}			
		
		if (!ser.isClosed()) 
		{
			ser.close(); // Doesn't close the underlying connection.					
		}
		
		SJRuntime.closeSocket(s); // Maybe need to guard by an isClosed? Forwarding protocol closes sockets early.  	
	}

	public void send(Object o) throws SJIOException
	{
		ser.writeObject(o); // SJUtf8Serializer will write it as a String (via toString), and similarly for other custom serializers.  
		
		if (RUNTIME_MONITORING) // FIXME: provide a common structure for handling the monitor after each typeable action (so that new actions don't forget to update the monitor). Can specify via an interface.
		{
			sm.send(o);
		}			
	}

	public void sendByte(byte b) throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation: " + b);
	}
	
	public void sendInt(int i) throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation: " + i);
	}

	public void sendBoolean(boolean b) throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation: " + b);
	}
	
	public void sendDouble(double d) throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation: " + d);
	}
	
	public void pass(Object o) throws SJIOException
	{
    if (ser.zeroCopySupported()) // FIXME: currently fixed to false by SJUtf8Serializer. 
    {
    	ser.writeReference(o);
    }
    else 
    {
    	ser.writeObject(o);
    }
        
		if (RUNTIME_MONITORING)
		{
			sm.send(o);
		}	        
	}
	
	public void copy(Object o) throws SJIOException
	{
		send(o); // Already taken care of type monitoring.		
	}
	
	public Object receive() throws SJIOException, ClassNotFoundException
	{			
		Object o;
		
		try
		{
			o = ser.readObject();	
		}
		catch (SJControlSignal cs)
		{
			throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] Shouldn't get in here.", cs);
		}
		
		if (RUNTIME_MONITORING)
		{
			sm.receive(o);
		}	
		
		return o; 
	}
	
	public byte receiveByte() throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation.");
	}	
	
	public int receiveInt() throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation.");
	}
	
	public boolean receiveBoolean() throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation.");
	}
	
	public double receiveDouble() throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Unsupported operation.");
	}

	//FIXME: branch labels shouldn't need to be Strings. In general, we can use any object (can specify/infer a base label type as part of the branch type) and just use equals for comparison as we do already for String. For e.g. UTF-8 mode, we use the message formatter to convert the arbitrary object to a String.  
	public void outlabel(String lab) throws SJIOException 
	{
    if (ser.zeroCopySupported()) //FIXME: as noted in pass, currently fixed to false by SJUtf8Serializer.
    {
      ser.writeReference(lab);
    } 
    else 
    {
      ser.writeObject(lab);
    }
        
		if (RUNTIME_MONITORING)
		{
			sm.outbranch(new SJLabel(lab));
		}		        
	}
	
	public String inlabel() throws SJIOException 
	{
		Object o;
		
		try
		{
			o = ser.readObject();	
		}
		catch (ClassNotFoundException cs)
		{
			throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] Shouldn't get in here.", cs);
		}
		catch (SJControlSignal cs)
		{
			throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] Shouldn't get in here.", cs);
		}
		
		if (!(o instanceof String)) // Should generalise for custom message formatters to use arbitrary objects as labels.
		{
			throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] Shouldn't get in here: " + o);
		}
		
		String lab = o.toString();
		
		if (RUNTIME_MONITORING)
		{
			sm.inbranch(new SJLabel(lab));
		}	          
	            
		return lab;
	}
	
	public void outsync(boolean b) throws SJIOException // FIXME: make support for zerocopy? (And other primitives?)
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Iteration not supported: " + b);			
	}
	
	public boolean insync() throws SJIOException
	{        	
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Iteration not supported.");
	}

	public boolean interruptibleOutsync(boolean condition) throws SJIOException // FIXME: need to add SJStateManager monitor calls to these routines. 
	{ 
		throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] TODO.");
	}
	
	public boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException 
	{
		throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] TODO.");
	}
	
	public boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException
	{
		throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] TODO.");
	} 
	
	public boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException 
	{        
		throw new SJRuntimeException("[SJNonSjCompatibilityProtocols] TODO.");
	}

	public boolean recurse(String lab) throws SJIOException
	{
		if (RUNTIME_MONITORING)
		{
			sm.recurse(new SJLabel(lab));
		}	  		
		
		return true;
	}

	public boolean recursionEnter(String lab) throws SJIOException
	{
		if (RUNTIME_MONITORING)
		{
			sm.recursion(new SJLabel(lab));
		}		
		
		return false;
	}

	public boolean recursionExit() throws SJIOException
	{
		return false;
	} 	
	
  public void sendChannel(SJService c, SJSessionType st) throws SJIOException // Can get encoded from c.
	{
  	throw new SJIOException("[SJNonSjCompatibilityProtocols] SJService passing not supported: " + c);   
	}
	
	public SJService receiveChannel(SJSessionType st) throws SJIOException
	{
		throw new SJIOException("[SJNonSjCompatibilityProtocols] SJService passing not supported: " + st);
	}
	
	public void delegateSession(SJAbstractSocket s, SJSessionType st) throws SJIOException
	{				
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Delegation not supported: " + s);
	}

	public SJAbstractSocket receiveSession(SJSessionType st, SJSessionParameters params) throws SJIOException
	{		
		throw new SJIOException("[SJNonSjCompatibilityProtocols] Delegation not supported: " + st);
	}

  public SJSerializer getSerializer()
  {
  	return ser;
  }

  public void setSerializer(SJSerializer ser)
  {
		this.ser = ser;
  }
}
