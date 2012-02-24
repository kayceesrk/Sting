/**
 * 
 */
package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.util.SJRuntimeUtils;
import sessionj.runtime.net.*;
import static sessionj.runtime.session.SJMessage.*;
import sessionj.runtime.transport.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJLabel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author Raymond
 *
 * Currently, this class collects together init., delegation and close protocol (with corresponding signal handling) into one class, but can easily be separated.
 * 
 * Currently, there is an instance of this class per session socket. (Later refactor to make it safe for sessions to share protocol objects?)
 *
 * FIXME: translation of typecase needs to add appropriate calls to the SJ Runtime so signify when we enter a typecase-case and handle runtime type monitoring accordingly. Related to this, execution of typecase should support singleton session types. 
 * 
 */
public class SJSessionProtocolsImpl implements SJSessionProtocols
{
    private SJBatchedSerializer batchedSerializer; //<By MQ>
	//private static final boolean RUNTIME_MONITORING = false;  
        private static final boolean RUNTIME_MONITORING = false; // <By MQ> MQTODO: Disabled runtime session checking// FIXME: factor out as a configurable parameter.
	
	private static final byte DELEGATION_START = -1; // Would be more uniform to be a control signal (although slower).
	//private static final byte DELEGATION_ACK = -2;	

    /** Collects messages received after a session has been delegated. */
	private List<SJMessage> lostMessages = new LinkedList<SJMessage>(); // Doesn't need synchronization, shouldn't be adding and removing in different threads.
    private final SJSocket s;
    private SJSerializer ser;
    
    private final SJStateManager sm;
    private static final Logger log = SJRuntimeUtils.getLogger(SJSessionProtocolsImpl.class);

    private SJSocketGroup socketGroup; //<By MQ> hacked access for other sockets in this protocol implementation

    public SJSessionProtocolsImpl(SJSocket s, SJSerializer ser)
	{
		this.s = s;
		this.ser = ser;
		batchedSerializer = new SJBatchedSerializer(ser); //<By MQ>
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
		else
		{
			sm = null;
		}
	}

	public void accept() throws SJIOException, SJIncompatibleSessionException
	{
		dualityCheck(s.getProtocol());
		
		if (RUNTIME_MONITORING)
		{
			sm.accept();
		}			
	}

    //<By MQ>
    public byte[] getUnflushedBytes()
    {
	return batchedSerializer.getUnflushedBytes();
    }

    public void setUnflushedBytes(byte[] b)
    {
	batchedSerializer.setUnflushedBytes(b);
    }

    public void socketGroup(SJSocketGroup sg)
    {
	socketGroup = sg;
    }

    public SJSocketGroup socketGroup()
    {
	return socketGroup;
    }

    public void flush() throws SJIOException
    {
	batchedSerializer.flush();
    }	
    
    public Object peekObject() throws SJIOException, ClassNotFoundException, SJControlSignal
    {
	//The reson for try and catch is that we want to READ the object, not just PEEK it if it is a control signal so that it won't be handled multiple times.
	try
	{
	    return batchedSerializer.peekObject();
	}
	catch(SJControlSignal cs)
	{
	    return batchedSerializer.readObject();
	}
    }

    public SJContinuationObject expectContinuation() throws SJIOException
    {
	boolean continuationReceived = false;
	Object o = null;
	try
	{
	    o = batchedSerializer.readObject();
	}
	catch(SJContinuationObject cs)
	{
	    continuationReceived = true;
	    return cs;
	}
	catch(Exception ex)
	{
	    throw new SJIOException(ex.getMessage());
	}
	if(continuationReceived == false)
	    throw new SJIOException("[SJSessionProtocolsImpl] Did not receive an expected continuation object");
	return null;
    }
    //<By MQ>

	public void request() throws SJIOException, SJIncompatibleSessionException
	{
		dualityCheck(s.getProtocol());
		
		if (RUNTIME_MONITORING)
		{
			sm.request();
		}		
	}
	
	public void close()
    {
		try
		{
			if (ser.getConnection() != null && !ser.isClosed()) // FIXME: need a isClosed. null in delegation case 2, session-receiver.
			{			   
                log.finer("About to write SJFIN on: " + ser);
		batchedSerializer.writeControlSignal(new SJFIN()); // <By MQ> // FIXME: currently fails for delegated sessions. 
			
				SJControlSignal cs = null;						
				
				if (lostMessages == null || lostMessages.isEmpty())
				{				
					if (ser.getConnection() != null) // FIXME: need a isClosed.
					{
                        TransportSelector sel;
                        //noinspection EmptyFinallyBlock
                        try
						{
                            log.finer("About to read control signal from: " + ser);
                            // FIXME: HACK to support non-blocking transports.
                            if (!s.supportsBlocking()) {
                                log.finer("Starting close protocol hack for non-blocking transport on:" + s);
	                            //noinspection StatementWithEmptyBody
	                            while (!s.arrived()); // spin-wait
	                            log.finer("Finished spin-wait in close protocol on: " + s);
                            }
			    batchedSerializer.readControlSignal(); //<By MQ>
                            log.fine("Control signal read.");
						}
						catch (Exception ioe) // We are prematurely closing. 
						{
                            log.log(Level.FINE, "Could not read close signal", ioe);
							// E.g. could be a non-forwarded message due to failed delegation.
						} finally {
                            // Not calling sel.close() to avoid closing the socket for this connection
                        }
					}
				}
				else
				{
					SJMessage m = lostMessages.get(lostMessages.size() - 1);
					
					byte t = m.getType();
					
					if (t != SJ_CONTROL)
					{
						throw new SJRuntimeException("[SJSessionProtocolsImpl] Unexpected message flag: " + t);
					}
					
					cs = (SJControlSignal) m.getContent();
				}
				
				/*if (!(cs instanceof SJFIN || cs instanceof SJDelegationSignal)) // FIXME: need to ACK delegation, and also distinguish good and bad FIN, to avoid confusion at other party.
				{
					throw new SJRuntimeException("[SJSessionProtocolsImpl] Unexpected control signal: " + cs); // No point, we are about to close anyway - maybe after failure, so close protocol may not have been performed properly. In any case, need to close up.
				}*/
			}
		}
		catch (SJIOException ioe)
		{
			//ioe.printStackTrace();
		}
		finally
		{
			if (RUNTIME_MONITORING)
			{
				sm.close();
			}			
			
			//if (ser != null) // Delegation case 2: no connection created between passive party and session acceptor.
			{
				if (!ser.isClosed()) 
				{
				    batchedSerializer.close(); //<By MQ> // Doesn't close the underlying connection.					
				}
			}
			
            log.fine("Closing socket: " + s);
			SJRuntime.closeSocket(s); // Maybe need to guard by an isClosed? Forwarding protocol closes sockets early.  
		}	
	}

	public void send(Object o) throws SJIOException
	{
	    batchedSerializer.writeObject(o); //ser.writeObject(o); //<By MQ>
		
		if (RUNTIME_MONITORING) // FIXME: provide a common structure for handling the monitor after each typeable action (so that new actions don't forget to update the monitor). Can specify via an interface.
		{
			sm.send(o);
		}			
	}

	public void sendByte(byte b) throws SJIOException
	{
		/*if (isLocal)
		{
			
		}
		else*/
		{
		    batchedSerializer.writeByte(b); //<By MQ>
		}
		
		if (RUNTIME_MONITORING)
		{
			sm.sendByte(b);
		}				
	}
	
	public void sendInt(int i) throws SJIOException
	{
		/*if (isLocal)
		{
			// Could do a zero-copy int send using Integer wrapper, but is that actually faster than encoding as 4 bytes?
		}
		else*/
		{
		    batchedSerializer.writeInt(i);//<By MQ>
		}
		
		if (RUNTIME_MONITORING)
		{
			sm.sendInt(i);
		}			
	}

	public void sendBoolean(boolean b) throws SJIOException
	{
		/*if (isLocal)
		{
		
		}
		else*/
		{
		    batchedSerializer.writeBoolean(b); //<By MQ>
		}
		
		if (RUNTIME_MONITORING)
		{
			sm.sendBoolean(b);
		}			
	}
	
	public void sendDouble(double d) throws SJIOException
	{
		/*if (isLocal)
		{
			
		}
		else*/
		{
		    batchedSerializer.writeDouble(d); //<By MQ>
		}
		
		if (RUNTIME_MONITORING)
		{
			sm.sendDouble(d);
		}			
	}
	
	public void pass(Object o) throws SJIOException
	{
		    if (ser.zeroCopySupported()) {
			batchedSerializer.writeReference(o); //<By MQ>
        } else {
			batchedSerializer.writeObject(o); //<By MQ> //ser.writeObject(o);
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
	
	/*public void copyInt(int i) throws SJIOException
	{
		sendInt(i);
	}
	
	public void copyDouble(double d) throws SJIOException
	{
		sendDouble(d);
	}*/
	
    public Object receive() throws SJIOException, ClassNotFoundException
	{
		Object o = null;
		
		while (true)
		{
			try
			{
				if (lostMessages.isEmpty())				
				{
				    o = batchedSerializer.readObject(); //o = ser.readObject(); //<By MQ>
				}
				else 
				{
					SJMessage m = lostMessages.remove(0);
					
					byte t = m.getType();
					
					if (t == SJ_OBJECT || t == SJ_REFERENCE)
					{
						o = m.getContent();
					}
					else handleMessage(m, t);
				}
				
				if (RUNTIME_MONITORING)
				{
					sm.receive(o);
				}	
				
				return o; // FIXME: refactor all receive methods to put the return at the end of the top-level (easier to read). 
			}
			catch (SJControlSignal cs)
			{
				handleControlSignal(cs);
			}
		}
	}

    public byte receiveByte(boolean monitoring) throws SJIOException // Need such variants because sometimes want to handle inductive delegation cases but not affect e.g. monitoring. Such variants probably needed for other routines as well. // Would be good to make it more general than just for delegation and monitoring. More general meaning allowing for other affected services (that may be added later) as well.
	{
		byte b = -1; 
		
		while (true)
		{
			try
			{
				if (lostMessages.isEmpty())				
				{				
					b = ser.readByte();
				}
				else
				{
					SJMessage m = lostMessages.remove(0);
					
					byte t = m.getType();
					
					if (t == SJ_BYTE)
					{
						b = m.getByteValue();
					}
					else handleMessage(m, t);
                }

				if (RUNTIME_MONITORING && monitoring)
				{
					sm.receiveByte(b);
				}					
				
				return b; 
			}
			catch (SJControlSignal cs)
			{
				handleControlSignal(cs);
			}
		}
	}
	
    public byte receiveByte() throws SJIOException 
	{
		return receiveByte(true);
	}	
	
    public int receiveInt(boolean monitoring) throws SJIOException
	{

        while (true)
		{
			try
			{
                int i;
                if (lostMessages.isEmpty())
				{
					i = ser.readInt();					
				}
				else
				{
					SJMessage m = lostMessages.remove(0);
					
					byte t = m.getType();
					
					if (t == SJ_INT)
					{
						i = m.getIntValue();
					}
					else return handleMessage(m, t);
                }

				if (RUNTIME_MONITORING && monitoring)
				{
					sm.receiveInt(i);
				}	                
                
				return i;
			}
			catch (SJControlSignal cs)
			{
				handleControlSignal(cs);
			}
		}
	}

    public int receiveInt() throws SJIOException
	{
		return receiveInt(true);
	}
	
    public boolean receiveBoolean() throws SJIOException
	{
		boolean b = false;
		
		while (true)
		{
			try
			{
				if (lostMessages.isEmpty())				
				{				
					b = ser.readBoolean();
				}
                else
                {
                    SJMessage m = lostMessages.remove(0);

                    byte t = m.getType();

                    if (t == SJ_BOOLEAN)
                    {
                        b = m.getBooleanValue();
                    }
                    else if (t == SJ_CONTROL)
                    {
                        handleControlSignal((SJControlSignal) m.getContent());
                    }
                    else
                    {
                        throw new SJIOException("[SJSessionProtocolsImpl] Unexpected lost message type: " + t);
                    }
                }
                
				if (RUNTIME_MONITORING)
				{
					sm.receiveBoolean(b);
				}	                
                
        return b;
			}
			catch (SJControlSignal cs)
			{
				handleControlSignal(cs);
			}
		}
	}
	
    public double receiveDouble() throws SJIOException 
	{

        while (true)
		{
			try
			{
                double d = 0;
                if (lostMessages.isEmpty())
				{
					d = ser.readDouble();					
				}
				else
				{
					SJMessage m = lostMessages.remove(0);
					
					byte t = m.getType();
					
					if (t == SJ_DOUBLE)
					{
						d = m.getIntValue();
					}
					else handleMessage(m, t);
                }

				if (RUNTIME_MONITORING)
				{
					sm.receiveDouble(d);
				}	                
                
				return d;
			}
			catch (SJControlSignal cs)
			{
				handleControlSignal(cs);
			}
		}
	}
	
  private static int handleMessage(SJMessage m, byte t) throws SJControlSignal, SJIOException 
  {
    if (t == SJ_CONTROL)
    {
      throw (SJControlSignal) m.getContent();
    }
    else
    {
      throw new SJIOException("[SJSessionProtocolsImpl] Unexpected lost message type: " + t);
    }
  }	
	
	public void outlabel(String lab) throws SJIOException 
	{
        if (ser.zeroCopySupported()) {
            batchedSerializer.writeReference(lab); //<By MQ>
        } else {
            batchedSerializer.writeObject(lab); //<By MQ>
        }
        
		if (RUNTIME_MONITORING)
		{
			sm.outbranch(new SJLabel(lab));
		}		        
	}
	
    public String inlabel() throws SJIOException 
	{
        try
		{
			while (true)
			{
				try
				{
                    String lab = null;
                    if (lostMessages.isEmpty())
					{
					    batchedSerializer.flush(); //<By MQ>
						lab = (String) batchedSerializer.readObject();
					}
					else
					{
						SJMessage m = lostMessages.remove(0);
						
						byte t = m.getType();
						
						if (t == SJ_OBJECT || t == SJ_REFERENCE)
						{
							lab = (String) m.getContent();
						}
						else handleMessage(m, t);
                    }
					
      		if (RUNTIME_MONITORING)
      		{
      			sm.inbranch(new SJLabel(lab));
      		}	          
                    
					return lab;
				}
				catch (SJControlSignal cs)
				{
					handleControlSignal(cs);
				}			
			}						
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new SJRuntimeException(cnfe);
		}		
	}
	
	public void outsync(boolean b) throws SJIOException // FIXME: make support for zerocopy? (And other primitives?)
	{
	    batchedSerializer.writeBoolean(b); //<By MQ>
		if (RUNTIME_MONITORING)
		{
			sm.outwhile(b);
		}			
	}
	
    public boolean insync() throws SJIOException 
	{
	    batchedSerializer.flush(); //<By MQ> so that the loop controller won't be trapped in receive() for ever!
        while (true)
		{
			try
			{
                boolean b = false;
                if (lostMessages.isEmpty())
				{			
					b = ser.readBoolean();
				}		
				else
				{
					SJMessage m = lostMessages.remove(0);
					
					byte t = m.getType();
					
					if (t == SJ_BOOLEAN)
					{
						b = m.getBooleanValue();
					}
					else handleMessage(m, t);
                }
				
    		if (RUNTIME_MONITORING)
    		{
    			sm.inwhile(b);
    		}	                
                
				return b;
			}
			catch (SJControlSignal cs)
			{
				handleControlSignal(cs);
			}						
		}
	}

	public boolean interruptibleOutsync(boolean condition) throws SJIOException { // FIXME: need to add SJStateManager monitor calls to these routines.
	    batchedSerializer.writeBoolean(condition); //<By MQ>
	    //batchedSerializer.flush();   //<By MQ>
	    return condition && readBooleanWrapCS();
	}
	
	public boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException {
	    boolean peerContinues = readBooleanWrapCS();
	    if (peerContinues && peerInterruptible) batchedSerializer.writeBoolean(condition); //<By MQ>
	    if (!condition && !peerInterruptible && peerContinues) 
	        throw new SJOutsyncInterruptedException
	                ("Insync attempted to interrupt, but outsync peer does not support interruption");
	    return peerContinues && condition;
	}
	
	public boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException {
	    batchedSerializer.writeBoolean(selfInterrupting); //<By MQ> // Any chance this will be a lost message? (should be handled by forwarding)
	    //batchedSerializer.flush();    //<By MQ>
	    return readBooleanHandleCS();
	}
	
	private boolean readBooleanWrapCS() throws SJIOException {
	    try {
	        return ser.readBoolean();
	    } catch (SJControlSignal signal) {
	        throw wrapControlSignal(signal);
	    }
	}
	
    private boolean readBooleanHandleCS() throws SJIOException  {
	    while (true) {
	        try {
	            return ser.readBoolean();
	        } catch (SJControlSignal controlSignal) {
	            handleControlSignal(controlSignal);
	        }
	    }
	}
	
	public boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException {
		//RAY
	    /*boolean isPeerInt = readBooleanHandleCS();  
	    ser.writeBoolean(selfInterruptible);
	    return isPeerInt;*/
	    batchedSerializer.writeBoolean(selfInterruptible);  //<By MQ>
	    batchedSerializer.flush();  //<By MQ>
		return readBooleanHandleCS();		
		//YAR
	}

	public boolean recurse(String lab) throws SJIOException 
	{
		if (RUNTIME_MONITORING)
		{
			sm.recurse(new SJLabel(lab));
		}	  		
		
		return true;
	}
	
	//public boolean recursionEnter() throws SJIOException 
	public boolean recursionEnter(String lab) throws SJIOException 
	{
		if (RUNTIME_MONITORING)
		{
			sm.recursion(new SJLabel(lab));
		}		
		
		return false;
	}
	
	public boolean recursionExit() throws SJIOException // Recursion-exit currently doesn't do anything (wrt. to runtime type monitoring). // This hook is now not generated at all to support delegation from within recursion scopes.
	{
		return false;
	}    
    
	public void sendChannel(SJService c, SJSessionType st) throws SJIOException // Can get encoded from c.
	{
		// Maybe faster to use a custom protocol, such as !<host>.!<port>.!<encoded>.
		
		//if (ser.zeroCopySupported())
        pass(c);
        // OK to alias SJServices across threads, typing ensures they are na-final (they are also thread-safe).
        /*else
          {
              send(c);
          }*/
        
		if (RUNTIME_MONITORING)
		{
			sm.sendChannel(SJRuntime.decodeSessionType(c.getProtocol().encoded()));
		}	        
	}
	
	public SJService receiveChannel(SJSessionType st) throws SJIOException
	{
		try
		{
			SJService c = (SJService) receive(); 
			
			if (RUNTIME_MONITORING)
			{
				sm.receiveChannel(SJRuntime.decodeSessionType(c.getProtocol().encoded()));
			}					
			
			return c;
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new SJIOException(cnfe);
		}		
	}
	
	public void delegateSession(SJAbstractSocket s, SJSessionType st) throws SJIOException
	{				
		//if (ser.getConnection() instanceof SJLocalConnection) // Attempting to do a purely noalias transfer of session socket object.
		if (ser.zeroCopySupported()) // This is matched by zeroCopyReceiveSession. 
		{
		    batchedSerializer.writeReference(s); //<By MQ> // Or use pass (like sendChannel). // Maybe some way to better structure this aspect of the routine? Or maybe this decision should be made here.
		    batchedSerializer.writeReference(st); //<By MQ>
    } 
		else
		{
			standardDelegateSession(s, st);
		}
		
		if (RUNTIME_MONITORING)
		{
			//sm.sendSession(s.getStateManager().currentState());
			
			sm.sendSession(st);
			s.getStateManager().close();
		}				
	}

    //public SJAbstractSocket receiveSession(SJSessionType st) throws SJIOException
	public SJAbstractSocket receiveSession(SJSessionType st, SJSessionParameters params) throws SJIOException
	{		
		SJAbstractSocket as;
		
		if (ser.zeroCopySupported())
		{
			as = zeroCopyReceiveSession(st);
		}
    else
    {
    	as = standardReceiveSession(st, params);
    }
		
		if (RUNTIME_MONITORING)
		{
			//sm.receiveSession(as.getStateManager().currentState());
			sm.receiveSession(st);
			
			as.getStateManager().open(); // The socket has been initialised and the protocols object created, but the state manager still needs initialising (must push the top-level monitoring context element).
		}			
		
		return as;
	}

    private SJAbstractSocket standardReceiveSession(SJSessionType st, SJSessionParameters params) throws SJIOException {
        checkDelegationFlagPresent();
        
        boolean becomeOrigRequester;
        boolean simultaneousDelegation;
        SJSessionType runtimeSt = null;

        List<SJMessage> controlMsgs = null;

        SJConnection conn = null;
        SJTransportManager sjtm = null;
        SJAcceptorThreadGroup atg = null;

        try {
            becomeOrigRequester = readOrigRequester();
            runtimeSt = readRuntimeType();

            //int port = SJRuntime.findFreePort();
            //int port = SJRuntime.takeFreePort();

            sjtm = SJRuntime.getTransportManager();

            /*SJAcceptorThreadGroup atg = sjtm.openAcceptorGroup(port);
            FIXME: It's not just the session-port that we have to check is free - we need to make sure
            that the *setup* transports can be opened for that session port (e.g. some other non-SJ
            process may have opened a TCP port there already).*/

            //SJAcceptorThreadGroup atg = SJRuntime.getFreshAcceptorThreadGroup(SJSessionParameters.DEFAULT_PARAMETERS);
            // FIXME: need to decide about session parameters.
            //SJAcceptorThreadGroup atg = SJRuntime.getFreshAcceptorThreadGroup(s.getParameters());
            atg = SJRuntime.getFreshAcceptorThreadGroup(params);

            batchedSerializer.writeInt(atg.getPort()); //<By MQ>

            controlMsgs = new LinkedList<SJMessage>();

            simultaneousDelegation = bufferLostMessagesUntilACKOrFIN(ser, controlMsgs); // Includes final control message.

            SJMessage m = controlMsgs.get(controlMsgs.size() - 1);

            if (simultaneousDelegation && becomeOrigRequester) {
                //throw new RuntimeException("foo");
                // A test case for failure in the middle of delegation. Not resolved yet, e.g. try delegation case 4.

                SJDelegationSignal ds = (SJDelegationSignal) controlMsgs.get(controlMsgs.size() - 1).getContent();

                //conn = sjtm.openConnection(ds.getHostName(), ds.getPort(), s.getParameters());
                // FIXME: is this the right thing to do about parameters?
                conn = sjtm.openConnection(ds.getHostName(), ds.getPort(), SJSessionParameters.DEFAULT_PARAMETERS);
                // We're using SJSessionParameters
            } else if (!(m.getType() == SJ_CONTROL && m.getContent() instanceof SJFIN)) {
                conn = atg.nextConnection(); // FIXME: if the passive peer has no compatible setups, we will hang.

                //conn = ss.accept().getConnection();

                //System.out.println("[SJSessionProtocolsImpl] Accepted connection from: " + conn.getHostName() + ":" + conn.getPort());
            }

            controlMsgs.remove(controlMsgs.size() - 1); // Remove the final control message (SJDelegationACK). // RAY: also SJDelegationSignal.
        } catch (SJControlSignal cs) {
            throw new SJIOException(cs);
        } catch (ClassNotFoundException cnfe) {
            throw new SJIOException(cnfe);
        } finally {
            if (sjtm != null && atg != null) {
                sjtm.closeAcceptorGroup(atg.getPort());

                SJRuntime.freePort(atg.getPort()); // Gives the session port the Runtime bound for us.

                //ss.close();
            }
        }

        SJAbstractSocket receivedSock = createSocket(becomeOrigRequester, st, runtimeSt);
        return initReceivedSocket(becomeOrigRequester, controlMsgs, conn, simultaneousDelegation, receivedSock);
    }

private void checkDelegationFlagPresent() throws SJIOException 
    {
    	//byte flag = ser.readByte(); // Will avoid unwanted monitoring actions, but won't handle delegation (case 3).
      byte flag = receiveByte(false); // Handles delegation by peer? // This isn't a "monitored input".    

        if (flag != DELEGATION_START) // Maybe replace by a proper control signal. Then would need to manually handle SJDelegationSignal.
        {
            throw new SJIOException("[SJSessionProtocolsImpl] Unexpected flag: " + flag);
        }
    }

    private void standardDelegateSession(SJAbstractSocket delegated, SJSessionType st) throws SJIOException {
        SJSessionProtocols sp = delegated.getSJSessionProtocols();

        if (!(sp instanceof SJSessionProtocolsImpl))
        {
            throw new SJIOException("[SJSessionProtocolsImpl] Incompatible session peer for delegation: " + sp);
        }

        batchedSerializer.writeByte(DELEGATION_START); //<By MQ>
        batchedSerializer.writeBoolean(delegated.isOriginalRequestor()); //<By MQ> // Original requestor.
        
        //ser.writeObject(delegated.getRuntimeType()); // Ray: incorrect, standardReceiveSession implicitly expects a String here...
        //ser.writeObject(SJRuntime.encode(delegated.currentSessionType())); 
        batchedSerializer.writeObject(SJRuntime.encode(st)); //<By MQ> //... but would it be faster to just serialize the SJSessionType?
        
        // We could also pass on the active type of the session being delegated (the session type performed up to this point). 
        
        int port = receiveInt(false); // Should handle delegation case 3 (the two preceding delegation protocol messages are forwarded by peer).

        SJSerializer delegatedPeerSer = sp.getSerializer();

        delegatedPeerSer.writeControlSignal(new SJDelegationSignal(localHostName(), port)); 

        forwardUntilACKOrFINInclusive(delegatedPeerSer, ser);

        // Maybe need to close sp (and corresponding socket).
        delegated.getSerializer().close(); // to avoid triggering the close protocol in the next call
        SJRuntime.closeSocket(delegated);
    }

    private String localHostName() {
        String hostName = ser.getConnection().getHostName();

        if (isLocalhost(hostName))
        {
            try
            {
                //hostName = InetAddress.getLocalHost().getHostName();
                hostName = InetAddress.getLocalHost().getHostAddress(); // Main Runtime routines are now using IP addresses rather than host names.
            }
            catch (UnknownHostException uhe)
            {
                throw new SJRuntimeException(uhe);
            }
        }
        return hostName;
    }

    private boolean isLocalhost(String hostName) {
        return hostName.equals("localhost") || hostName.equals("127.0.0.1");
    }

    private SJAbstractSocket initReceivedSocket
        (boolean origReq, List<SJMessage> controlMsgs, SJConnection conn, boolean simdel, SJAbstractSocket sock)
        throws SJIOException
    {

        SJRuntime.bindSocket(sock, conn); // Calls SJAbstractSocket.init (which creates the serializer and protocols components).

        ((SJSessionProtocolsImpl) sock.getSJSessionProtocols()).lostMessages = controlMsgs;

        if (conn != null) // Delegation case 2.
        {
            try // Duals the operations at the end of reconnectToDelegationTarget. But not sure if this should be done here.
            {
                //sock.setHostName((String) sock.getSerializer().readObject());
                // Could instead get this information from conn (currently using IP addresses).
                sock.setHostName(InetAddress.getByName(conn.getHostName()).getHostAddress());

                // RAY
                if (simdel && origReq) {
                    sock.getSerializer().writeInt(sock.getLocalPort());
                    // Mirrors the actions of reconnectToDelegationTarget.
                } else
                //YAR
                {
                    sock.setPort(sock.getSerializer().readInt());
                // This could also be given by the delegator, passive party's session port shouldn't change - except for delegation case 4?
                }
            }
            catch (SJControlSignal cs) {
                throw new RuntimeException("[SJSessionProtocolsImpl] Shouldn't get in here", cs);
            }
            catch (UnknownHostException uhe) {
                throw new RuntimeException("[SJSessionProtocolsImpl] Shouldn't get in here", uhe);
            }
        }

        return sock;
    }

    private SJAbstractSocket createSocket(boolean origReq, SJSessionType declaredType, SJSessionType runtimeType) throws SJIOException {
        SJProtocol declaredProto = new SJProtocol(SJRuntime.encode(declaredType));
        SJAbstractSocket receivedSocket;
        if (origReq) {
            receivedSocket = new SJRequestingSocket(declaredProto, SJSessionParameters.DEFAULT_PARAMETERS, runtimeType);
        // FIXME: default parameters for now, but maybe receive session should also have some transport configuration options.
        } else {
            receivedSocket = new SJAcceptingSocket(declaredProto, SJSessionParameters.DEFAULT_PARAMETERS, runtimeType);
        }
        return receivedSocket;
    }

    private boolean readOrigRequester() throws SJIOException, SJControlSignal {
        boolean origReq;
        if (lostMessages.isEmpty()) {
            origReq = ser.readBoolean();
        } else {
            SJMessage m = lostMessages.remove(0);

            byte t = m.getType();

            if (t != SJ_BOOLEAN) {
                throw new SJIOException("[SJSessionProtocolsImpl] Expected boolean, not: " + t);
            }

            origReq = m.getBooleanValue();
        }
        return origReq;
    }

   private SJSessionType readRuntimeType() throws SJIOException, SJControlSignal, ClassNotFoundException {
        String runtimeTypeString;
        if (lostMessages.isEmpty()) {
            runtimeTypeString = (String) ser.readObject();
        } else {
            SJMessage m = lostMessages.remove(0);

            byte t = m.getType();

            if (t != SJ_OBJECT) {
                throw new SJIOException("[SJSessionProtocolsImpl] Expected object, not: " + t);
            }

            runtimeTypeString = (String) m.getContent();
        }
        return SJRuntime.decodeSessionType(runtimeTypeString);
   }

    private SJAbstractSocket zeroCopyReceiveSession(SJSessionType staticType) throws SJIOException {
        try {
            SJAbstractSocket s = (SJAbstractSocket) ser.readReference(); // Can use ordinary receive (like receiveChannel).
            SJSessionType runtimeType = (SJSessionType) ser.readReference(); // For zero-copy, fastest to just send a reference to the current session type.
            s.updateStaticAndRuntimeTypes(staticType, runtimeType);
            return s;
        }
        catch (SJControlSignal cs) // May need revision to handle certain delegation cases.
        {
            throw new SJIOException(cs);
        }
    }

    private void handleControlSignal(SJControlSignal cs) throws SJIOException
	{
		if (cs instanceof SJDelegationSignal)
		{			
			reconnectToDelegationTarget((SJDelegationSignal) cs);						
		}
		//<By MQ> chaining handling
		else if(cs instanceof SJChainedObject)
		{
		    //System.out.println("SJChainedObject");
		    socketGroup.handleSJChainedObject((SJChainedObject)cs);
		}
		else if(cs instanceof SJCloseObject)
		{
		    //System.out.println("SJCloseObject");
		    socketGroup.handleSJCloseObject((SJCloseObject)cs);
		}
		else if(cs instanceof SJContinuationNotification)
		{
		    //System.out.println("SJContinuationNotification");
		    socketGroup.handleSJContinuationNotification((SJContinuationNotification)cs);
		}
		else if(cs instanceof SJContinuationObject)
		{
		    //System.out.println("SJContinuationObject");
		    socketGroup.handleSJContinuationObject((SJContinuationObject)cs);
		}
		//</By MQ>
		else throw wrapControlSignal(cs);
	}

    private SJIOException wrapControlSignal(SJControlSignal signal) {
        if (signal instanceof SJFIN)
		{
			// No need for the closing protocol here, close will be done in finally.
             return new SJIOException("[SJSessionProtocolsImpl] Session prematurely terminated by peer: " + signal);		
		}
		else
		{
			return new SJIOException("[SJSessionProtocolsImpl] Unsupported control signal: " + signal);			
		}
    }
	
  //FIXME: need to check compatibility of session-layer protocol components. Also should have some way to work out if we're connected to a non-SJ or a non-SJ-compatibility peer.
	private void dualityCheck(SJProtocol proto) throws SJIOException, SJIncompatibleSessionException
	{
        String encoded = proto.encoded();
        log.finest("About to send our session type on: " + ser);
	batchedSerializer.writeObject(encoded); //<By MQ>
	batchedSerializer.flush(); //<By MQ>
		
		SJSessionType ours = proto.type().getCanonicalForm();
		
		try
		{
            log.finest("About to receive peer session type from: " + ser);
            String encodedType = (String) ser.readObject();
                        
            /*
            //Disabled for benchmarking.            
            SJSessionType theirs = SJRuntime.decodeSessionType(encodedType).getCanonicalForm();			
            
			if (!ours.isDualtype(theirs))
			{
				//ser.close(); // The session socket variable will still be null because of this
                // exception, so close won't be called in the finally block - manually close here to
                // flush (call the close protocol instead?)
				//SJRuntime.getTransportManager().closeConnection(ser.getConnection());
                // FIXME: this isn't nice. Also need to unbind session socket...
				
				s.close();
				
				throw new SJIncompatibleSessionException("[SJSessionProtocolsImpl] Our session type (" + ours + ") incompatible with theirs: " + theirs);
			}
			/*/
			
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new SJRuntimeException(cnfe);
		}
		catch (SJControlSignal cs)
		{
			throw new SJRuntimeException("[SJSessionProtocolsImpl] Unexpected control signal", cs);
		}
	}
	
	private void reconnectToDelegationTarget(SJDelegationSignal ds) throws SJIOException
	{
		//System.out.println("[SJSessionProtocolsImpl] Received SJDelegationSignal: " + ds);
		
	    batchedSerializer.writeControlSignal(new SJDelegationACK()); //<By MQ>
		
		SJRuntime.reconnectSocket(s, ds.getHostName(), ds.getPort()); // Maybe could send a FIN to close the old socket.
		
		//System.out.println("[SJSessionProtocolsImpl] Reconnected to: " + ds.getHostName() + ":" + ds.getPort());		
		
		//ser.writeObject(s.getLocalHostName());
        // FIXME: breaks e.g. PLDI benchmark 3. // Similar to the SJRuntime accept/request exchange.
        // But should this be done here? Maybe reuse the accept/request exchange?

		batchedSerializer.writeInt(s.getLocalPort()); //<By MQ>
	}
	
	private static void forwardUntilACKOrFINInclusive(SJSerializer s1, SJSerializer s2) throws SJIOException 
	{
		SJMessage m;
		byte type;
		
		while (true)
		{
			try
			{
				m = s1.nextMessage();			
				
				switch (type = m.getType())
				{
					case SJ_CONTROL:
                        SJControlSignal cs = (SJControlSignal) m.getContent();

                        /*if (cs instanceof SJDelegationSignal) // Double delegation.
{
throw new SJRuntimeException("[SJSessionProtocolsImpl] Simultaneous delegation not yet supported: " + cs);
}*/

                        s2.writeControlSignal(cs);

                        if (cs instanceof SJDelegationACK || cs instanceof SJFIN)
                        {
                            //System.out.println("[SJSessionProtocolsImpl] forwarded: " + cs);

                            return;
                        }

                        // RAY
                        else if (cs instanceof SJDelegationSignal)
                        {
                            return;
                        }
                        // YAR

                        break;
                    case SJ_OBJECT:
                        s2.writeObject(m.getContent());

                        break;
                    case SJ_REFERENCE:
                        s2.writeReference(m.getContent());

                        break;
                    case SJ_BYTE:
                        s2.writeByte(m.getByteValue());

                        break;
                    case SJ_INT:
                        s2.writeInt(m.getIntValue());

                        break;
                    case SJ_BOOLEAN:
                        s2.writeBoolean(m.getBooleanValue());

                        break;
                    default:
                        throw new SJIOException("[SJSessionProtocolsImpl] Unexpected message type: " + type);
                }
				
				//System.out.println("[SJSessionProtocolsImpl] forwarded: " + m);
			}			
			catch (ClassNotFoundException cnfe) // If we manually transmit the serialized form, no need to worry about this here.
			{
				throw new SJIOException(cnfe);
			}
		}		
	}
	
	private static boolean bufferLostMessagesUntilACKOrFIN(SJSerializer ser, List<SJMessage> lostMessages) throws SJIOException // Inclusive of final control message.
	{
		boolean simdel = false;
		
		try
		{
			while (true)
			{
				SJMessage m = ser.nextMessage();
				
				lostMessages.add(m);
				
				byte t = m.getType(); 
				
				if (t == SJ_CONTROL)					
				{
					if (m.getContent() instanceof SJDelegationACK)						
					{
						break;
					}

					// RAY
					else if (m.getContent() instanceof SJDelegationSignal)
					{
						simdel = true;

						break;
					}
					// YAR
				}
				
				//lostMessages.add(m); // receiveSession doesn't want lostMessages to be empty.
				
				//System.out.println("[SJSessionProtocolsImpl] Buffered lost message: " + m);
				
				if (t == SJ_CONTROL && m.getContent() instanceof SJFIN)
				{							
					break;
				}				
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new SJIOException(cnfe);
		}
		
		return simdel;
	}
	
	// Hacks for bounded-buffer communication.

	/*public void sendBB(Object o) throws SJIOException
	{
		ser.writeObject(o);
	}
	
	public void passBB(Object o) throws SJIOException
	{
		if (!zeroCopySupported)
		{
			ser.writeObject(o);
		}
		else
		{
			ser.writeReference(o);			
		}
	}*/
	
	/*public boolean recurseBB(String lab) throws SJIOException
	{
		if (boundedBufferSupported)
		{
			((SJBoundedBufferConnection) s.getConnection()).recurseBB(lab);
			
			return true;
		}
		else
		{
			// recurse(lab); // This routine does not exist yet.
			
			return true;
		}		
	}*/

  public SJSerializer getSerializer()
  {
      return ser;
  }

  public void setSerializer(SJSerializer ser)
  {
      this.ser = ser;
  } 
}
