package sessionj.runtime.transport.sharedmem;

import sessionj.ast.sessops.basicops.SJReceive;
import sessionj.ast.sessops.compoundops.SJInbranch;
import sessionj.ast.sessops.compoundops.SJInwhile;
import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;
import sessionj.runtime2.SJProtocol;
import sessionj.types.sesstypes.SJBranchType;
import sessionj.types.sesstypes.SJInbranchType;
import sessionj.types.sesstypes.SJInwhileType;
import sessionj.types.sesstypes.SJMessageCommunicationType;
import sessionj.types.sesstypes.SJReceiveType;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJCompilerUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import polyglot.types.SemanticException;

// All derived from sessionj.runtime.transport.sharedmem.SJFifoPair.

class SJBoundedFifoPairAcceptor extends AbstractWithTransport implements SJConnectionAcceptor {	
	protected static final HashMap<Integer, LinkedList<SJBoundedFifoPairConnection>> servers = new HashMap<Integer, LinkedList<SJBoundedFifoPairConnection>>(); 
	
	protected int port;
	protected boolean isClosed = false;
	
	private int boundedBufferSize;

	SJBoundedFifoPairAcceptor(int port, int boundedBufferSize, SJTransport transport) {
		super(transport);
		this.port = port;

		servers.put(port, new LinkedList<SJBoundedFifoPairConnection>());
		
		this.boundedBufferSize = boundedBufferSize;
	}
	
	public SJBoundedFifoPairConnection accept() throws SJIOException
  {
		LinkedList<SJBoundedFifoPairConnection> requests = servers.get(Integer.valueOf(port));
		
		SJBoundedFifoPairConnection theirConn;
		
		synchronized (requests) // FIXME: requests can sometimes be null at this point (quite rarely). 
    {
			try
			{
				while (requests.isEmpty())
				{
					requests.wait();
				}
			}
			catch (InterruptedException ie)
			{
				throw new SJIOException('[' + getTransportName() + "] 1: " + ie, ie);
			}
			
			theirConn = requests.remove(0);	
    }		
		
		//SJBoundedFifo ours = new SJBoundedFifo(SJBoundedFifoPair.INIT_BUFFER_SIZE);
		SJBoundedFifo ours = new SJBoundedFifo(boundedBufferSize);
		
		int localPort = SJBoundedFifoPair.findFreePort();
		
		SJBoundedFifoPair.bindPort(localPort); // Can the connection establishment after this fail? Would need to free the port.
		
		SJBoundedFifoPairConnection ourConn = new SJBoundedFifoPairConnection(null, theirConn.getLocalPort(), localPort, ours, getTransport()); // FIXME: need peer hostname.
		//SJBoundedFifoPairConnection ourConn = new SJBoundedFifoPairConnection(null, theirConn.getLocalPort(), port, ours); // FIXME: need peer hostname. // Reusing server port value for local port, as in TCP. // Problem: hard to tell when need to free port after a connection is closed (don't know if server is using that port still).
		
		theirConn.setPeerFifo(ours);
		ourConn.setPeerFifo(theirConn.getOurFifo());
				
		boolean[] b = theirConn.hasBeenAccepted;
		
		synchronized (b)
    {			
			b[0] = true;
			b.notifyAll(); 
    }	
		
	  return ourConn;				
  }
	
	public void close()
	{
		isClosed = true;
		
		synchronized (servers)
		{
			SJBoundedFifoPair.freePort(port);
			
			servers.remove(Integer.valueOf(port));
		}			
	}
	
	public boolean interruptToClose()
	{
		return true;
	}
	
	public boolean isClosed()
	{
		return isClosed;
	}

	protected static void addRequest(int port, SJBoundedFifoPairConnection conn)
	{

        synchronized (servers)
		{
			List<SJBoundedFifoPairConnection> foo = servers.get(port);
		
			synchronized (foo)
      {
				foo.add(conn);
				foo.notify();	      
      }
		}
	}
}

class SJBoundedFifoPairConnection extends AbstractSJConnection
{
	private final String hostName;	
	private final int port;
	
	private final int localPort;
	
	private SJBoundedFifo ours;
	private SJBoundedFifo theirs;
	
    // package-private for access in SJBoundedFifoPair#accept()
	final boolean[] hasBeenAccepted = { false };
	
	//private boolean initialised = false;
	
	// "ours" is our input buffer, i.e. we read from it (and the session peer writes to it).
	protected SJBoundedFifoPairConnection(String hostName, int port, int localPort, SJBoundedFifo ours, SJTransport transport) {
        super(transport);
        this.hostName = hostName;
		this.port = port;
		this.localPort = localPort;
		
		this.ours = ours;
    }
	
	/*public void initBoundedBuffers(int size)
	{
		if (ours == null || theirs == null)
		{
			throw new SJRuntimeException("[" + getTransportName() + "] Shouldn't get in here: " + size);
		}
		
		synchronized (this)
		{
			if (!initialised)
			{
				ours = new SJBoundedFifo(size);
				theirs = new SJBoundedFifo(size); // Wrong, need to link up the buffers between the two parties again.
			}
		}
	}*/
	
	public void disconnect() {		
		if (theirs != null) // FIXME: need an isClosed, e.g. delegation protocol closes early.
		{
			boolean closed = false;			
			
			if (!theirs.isClosed())
			{
				ours = theirs = null;
				
				closed = true;
			}
			
			if (!closed)
			{		
				ours.close();
			}
		}
		
		SJBoundedFifoPair.freePort(localPort); // This is a problem if we try to reuse server port for accepted connections a la TCP. 
	}

  public void writeByte(byte b) throws SJIOException
  {    
  	if (theirs == null) // e.g. forwarding protocol closes connections early. Remember: we are at transport level here, don't get confused by session-type level properties, e.g. writes can happen asynchronously (i.e. by both ends at the same time) and we may incorrectly try to use closed connections, etc. 
  	{
  		throw new SJIOException('[' + getTransportName() + "] Connection already closed.");
  	}
  	
		if (theirs.isClosed())
		{
			throw new SJIOException('[' + getTransportName() + "] Connection closed by peer.");
		}
		
		theirs.writeByte(b);   
  }

  public void writeBytes(byte[] bs) throws SJIOException
  {
  	if (theirs == null)  
  	{
  		throw new SJIOException('[' + getTransportName() + "] Connection already closed.");
  	}  	
  	
 		if (theirs.isClosed())
		{
			throw new SJIOException('[' + getTransportName() + "] Connection closed by peer.");
		}  		
  		
 		theirs.writeBytes(bs); // FIXME: should copy-on-send. But should be OK, we're always writing the serialized messages (i.e. already copied and won't be modified)? 
  }

  public byte readByte() throws SJIOException
  {
  	if (theirs == null)  
  	{
  		throw new SJIOException("[" + getTransportName() + "] Connection already closed.");
  	}  	
  	
		return ours.readByte();  	  	
  }

  public void readBytes(byte[] bs) throws SJIOException
  {  
  	if (theirs == null) 
  	{
  		throw new SJIOException("[" + getTransportName() + "] Connection already closed.");
  	}  	
  	  	
		byte[] foo = ours.readBytes();
		
		if (foo.length != bs.length) // FIXME: need to decide whether we should block until bs can be filled or what.
		{
			throw new SJIOException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] Received " + foo.length + " bytes, bad buffer size: " + bs.length);
		}
		
		System.arraycopy(foo, 0, bs, 0, bs.length); // FIXME: need to buffer any extra data that doesn't fit into bs. // FIXME: could optimise by returning foo;  			
	}

  public void writeReference(Object o) throws SJIOException
  {
 		theirs.writeObject(o);
  } 
  
  public Object readReference() throws SJIOException
  {
  	return ours.readObject();
  } 
  
	public void flush() throws SJIOException
  {
	  
  }
	
	public String getHostName()
	{
		return hostName;
	}
	
	public int getPort()
	{
		return port;
	}

	public int getLocalPort()
	{
		return localPort;
	}
	
    protected SJBoundedFifo getOurFifo()
	{
		return ours;
	}
	
	protected void setPeerFifo(SJBoundedFifo theirs)
	{
		this.theirs = theirs;
	}
	
	public void recurseBB(String lab) throws SJIOException
	{
		/*System.out.println("1: " + localPort);
		
		theirs.resetWrite();
		
		System.out.println("2: ");
		
		ours.resetRead();
		
		System.out.println("3: ");*/
	}
}

/**
 * @author Raymond
 *
 */
public class SJBoundedFifoPair extends AbstractSJTransport
{
	public static final String TRANSPORT_NAME = "sessionj.runtime.transport.sharedmem.SJBoundedFifoPair";
	
	public static final int UNBOUNDED_BUFFER_SIZE = -1; // Factor out more generally? Make a SJBoundedBufferTransport?
	
	//protected static final int INIT_BUFFER_SIZE = 10000; // For session initialisation protocol, before actual size bound parameters are passed.
	
	private static final int LOWER_PORT_LIMIT = 1024; 
	private static final int PORT_RANGE = 65535 - 1024;
	
	private static final Collection<Integer> portsInUse = new HashSet<Integer>(); 
	
	public SJBoundedFifoPair() { }

	public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) throws SJIOException
	{
		return openAcceptor(port, UNBOUNDED_BUFFER_SIZE);
	}
	
	public SJConnectionAcceptor openAcceptor(int port, int boundedBufferSize) throws SJIOException
	{
		if (boundedBufferSize == UNBOUNDED_BUFFER_SIZE)
		{
			throw new SJIOException("[" + getTransportName() + "] No maximum bound on buffer specified: " + boundedBufferSize);
			//throw new SJIOException("[" + getTransportName() + "] Maximum bound on buffer out of range: " + boundedBufferSize);
		}		
		
		SJConnectionAcceptor a = new SJBoundedFifoPairAcceptor(port, boundedBufferSize, this);
		
		bindPort(port);
		
		return a;
	}
	
	/*public SJBoundedFifoPairConnection connect(SJServerIdentifier si) throws SJIOException
	{
		return connect(si.getHostName(), si.getPort());
	}*/
	
	public SJBoundedFifoPairConnection connect(String hostName, int port) throws SJIOException
	{
		return connect(hostName, port, UNBOUNDED_BUFFER_SIZE);	
	}

    public SJBoundedFifoPairConnection connect(String hostName, int port, int boundedBufferSize) throws SJIOException
	{
		// FIXME: need to map session-level addresses to transport specific values.
		
		if (boundedBufferSize == UNBOUNDED_BUFFER_SIZE)
		{
			throw new SJIOException("[" + getTransportName() + "] No maximum bound on buffer specified: " + boundedBufferSize);
			//throw new SJIOException("[" + getTransportName() + "] Maximum bound on buffer out of range: " + boundedBufferSize);
		}
		
		try
		{
			if (!(hostName.equals("127.0.0.1") || hostName.equals(InetAddress.getLocalHost().getHostAddress()) || hostName.equals("localhost") || hostName.equals(InetAddress.getLocalHost().getHostName()))) // FIXME: check properly. We're now using IP addresses rather than host names. // Factor out constants.
			{
				throw new SJIOException("[" + getTransportName() + "] Connection not valid: " + hostName + ":" + port);
			}
		}
		catch (IOException ioe) // SJIOException is not an IOException.
		{
			throw new SJIOException(ioe);
		}
		
		if (!portInUse(port)) 
		{
			throw new SJIOException("[" + getTransportName() + "] Port not open: " + port);
		}
		
		int localPort = getFreePort();
		
		bindPort(localPort); // Can the connection establishment after this fail? Would need to free the port.
		
		//SJBoundedFifoPairConnection ourConn = new SJBoundedFifoPairConnection(hostName, port, localPort, new SJBoundedFifo(INIT_BUFFER_SIZE));
		SJBoundedFifoPairConnection ourConn = new SJBoundedFifoPairConnection(hostName, port, localPort, new SJBoundedFifo(boundedBufferSize), this);
		
		SJBoundedFifoPairAcceptor.addRequest(port, ourConn);
		
		boolean[] b = ourConn.hasBeenAccepted; // FIXME: Object will do (only need a lock for synchronisation).
		
		synchronized (b)		
		{
			try
			{
				while (!b[0])
				{				
					b.wait();	
				}
			}
			catch (InterruptedException ie) 
			{
				throw new SJIOException("[" + getTransportName() + "] 2: " + ie);
			}
    }	
				
		return ourConn;
	}

	public boolean portInUse(int port)
	{
		return !portFree(port);
	}
	
	public int getFreePort() throws SJIOException
	{
		return findFreePort();
	}
	
	public String getTransportName()
	{
		return TRANSPORT_NAME;
	}
	
	public static boolean portFree(int port)
	{
		synchronized (portsInUse)
		{
			return !portsInUse.contains(port);
		}
	}
	
	protected static int findFreePort() throws SJIOException
	{
		int start = new Random().nextInt(PORT_RANGE);
		int seed = start + 1;
		
		for (int port = seed % PORT_RANGE; port != start; port = seed++ % PORT_RANGE)  
		{
			if (portFree(port + LOWER_PORT_LIMIT))
			{
				return port + LOWER_PORT_LIMIT;
			}
		}
		
		throw new SJIOException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] No free port available.");
		//throw new SJIOException("[SJ(Bounded)FifoPair] No free port available.");
	}
	
	protected static void bindPort(int port) 
	{
		synchronized (portsInUse)
    {
	    portsInUse.add(port);
    }
	}
	
	protected static void freePort(int port)
	{		
		synchronized (portsInUse)
    {
	    portsInUse.remove(port);
    }
	}
	
	public String sessionHostToNegotiationHost(String hostName)
	{
		return hostName;
	}
	
	public int sessionPortToSetupPort(int port)
	{
		return port;
	}
	
	public static final int upperBufferBound(SJProtocol p)
	{
		return upperBufferBound(p.getSessionType());
	}
	
	public static final int dualUpperBufferBound(SJProtocol p)
	{
		try
		{
			return upperBufferBound(SJCompilerUtils.dualSessionType(p.getSessionType()));
		}
		catch (SemanticException se)
		{
			throw new SJRuntimeException("[SJBoundedFifoPair] Could not determine a dual type to: " + p.toString(), se);
		}
	}		
	
	// Determines the upper bound for the input buffer size (i.e. from the receiver side of the session type).
	// FIXME: implicitly tied to the current SJ serialization protocol, and other protocols like initiation and close.
	private static final int upperBufferBound(SJSessionType st)
	{
		int bound = 0;
		int count = 0;
		
		while (st != null)
		{
			if (isInputNode(st))
			{
				count++;
			}
			else
			{
				if (count > bound)
				{
					bound = count;
				}
				
				count = 0;
			}
			
			if (st instanceof SJMessageCommunicationType)
			{
				st = st.child();
			}
			else if (st instanceof SJBranchType)
			{
				// TODO.
				
				throw new SJRuntimeException("[SJBoundedFifoPair] TODO.");
			}
			else
			{
				throw new SJRuntimeException("[SJBoundedFifoPair] TODO.");
			}
		}
		
		return bound;
	}
	
	private static final boolean isInputNode(SJSessionType st)
	{
		return (st instanceof SJReceiveType) || (st instanceof SJInbranchType) || (st instanceof SJInwhileType);
	}
}

// This is a FIFO that uses "wrapped-around" (modular arithmetic wrt. buffer capacity) indexing for the read/write indices.
class SJBoundedFifo
{
	private Object[] buffer; 	
	private int size;
	
	private int write = 0;
	private int read = 0;
	
	/*private boolean resetWrite = false; // Disastrously wrong due to asynchronous (i.e. one party does it earlier) recurse boundary.
	private boolean resetRead = false;*/
	
	private int avail = 0;
	
	//private Object lock = new Object();
	
	private static final Object EOF = new Object();
	
	public SJBoundedFifo(int size)
	{
		this.buffer = new Object[size + 1]; // Extra space for an EOF.
		this.size = size;
	}

	public synchronized void writeByte(byte b) throws SJIOException
	{
        writeObject(b);
	}
	
	public synchronized void writeBytes(byte[] bs) throws SJIOException
	{
        writeObject(bs);
	}
	
	public synchronized byte readByte() throws SJIOException
	{		
		Object o = readObject();
		
		if (o instanceof Byte)
		{
			return (Byte) o;
		}
		else
		{
			throw new SJIOException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] Connection closed by peer.");
		}
	}
	
	public synchronized byte[] readBytes() throws SJIOException
	{			
		Object o = readObject();
		
		if (o instanceof byte[])
		{
			return (byte[]) o;
		}
		else
		{
			throw new SJIOException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] Connection closed by peer.");
		} 
	}
		
	public synchronized void writeObject(Object o) throws SJIOException
	{
		/*if (resetWrite) // Shouldn't break error handling via EOF signal? isClosed uses [write - 1].
		{
			write = 0;
			
			resetWrite = false;
		}*/
		
		/*if (write >= size)
		{
			throw new SJIOException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] SJBoundedFifo write overflow: " + write);
		}*/
		
		buffer[write++] = o;
		
		if (write >= size)
		{
			write = 0;
		}
		
		avail++;

        notifyAll();
	}
		
	public synchronized Object readObject() throws SJIOException
	{
		/*if (resetRead) 
		{
			read = 0;
			
			resetRead = false;
		}*/
		
		/*if (read >= size)
		{
			throw new SJIOException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] SJBoundedFifo read overflow: " + read);
		}*/
		
		try
		{
			//while (write <= read) // Not sure about threading situation for early close - so use while for safety. // write should never actually be less than read.
			while (avail <= 0)
			{				
				this.wait();
			}			
		}
		catch (InterruptedException ie)
		{
			throw new SJRuntimeException("[" + SJBoundedFifoPair.TRANSPORT_NAME + "] " + ie);
		}
		
		//return buffer[read++];
		
		Object o = buffer[read++];
		
		if (read >= size)
		{
			read = 0;
		}
		
		avail--;
		
		return o;
	}
	
	public synchronized void close() 
	{
		try
		{		
			this.writeObject(EOF);
		}
		catch (SJIOException ioe)
		{
			
		}
	}
	
	public synchronized boolean isClosed()
	{
		return (write > 0) ? buffer[write - 1] == EOF : false;
	}
	
	/*public synchronized void resetWrite()
	{
		//write = 0;
		
		resetWrite = false;
		
		//notifyAll();
	}
	
	public synchronized void resetRead()
	{
		//read = 0;
		
		resetRead = false;
		
		//notifyAll();
	}*/
}
