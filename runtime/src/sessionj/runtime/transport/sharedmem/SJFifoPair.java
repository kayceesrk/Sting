package sessionj.runtime.transport.sharedmem;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;
import sessionj.runtime.util.SJRuntimeUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class SJFifoPairAcceptor extends AbstractWithTransport implements SJConnectionAcceptor {	
	private static final HashMap<Integer, LinkedList<SJFifoPairConnection>> servers = new HashMap<Integer, LinkedList<SJFifoPairConnection>>();

	private final int port;
	private boolean isClosed = false;

    SJFifoPairAcceptor(int port, SJTransport transport) {
	    super(transport);
		this.port = port;

        servers.put(port, new LinkedList<SJFifoPairConnection>());
	}
	
	public SJFifoPairConnection accept() throws SJIOException
  {
		LinkedList<SJFifoPairConnection> requests = servers.get(port);
		
		SJFifoPairConnection theirConn;
		
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
		
		List<Object> ours = new LinkedList<Object>();
		
		int localPort = SJFifoPair.findFreePort();

		//RAY
		SJFifoPair.bindPort(localPort); // Can the connection establishment after this fail? Would need to free the port.
		//YAR
		
		SJFifoPairConnection ourConn = new SJFifoPairConnection(null, theirConn.getLocalPort(), localPort, ours, getTransport()); // FIXME: need peer hostname.
		//SJFifoPairConnection ourConn = new SJFifoPairConnection(null, theirConn.getLocalPort(), port, ours); // FIXME: need peer hostname. // Reusing server port value for local port, as in TCP. // Problem: hard to tell when need to free port after a connection is closed (don't know if server is using that port still).
		
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
            try {
                SJFifoPair.freePort(port);
            } catch (SJIOException e) {
                SJFifoPair.logger.log(Level.INFO, "Could not free port cleanly", e);
            }

            servers.remove(port);
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
	
	protected static void addRequest(int port, SJFifoPairConnection conn)
	{

        synchronized (servers)
		{
			List<SJFifoPairConnection> foo = servers.get(port);
		
			synchronized (foo)
      {
				foo.add(conn);
				foo.notify();	      
      }
		}
	}
}

class SJFifoPairConnection extends AbstractSJConnection implements SJLocalConnection
{
	private static Object EOF = new Object() {}; 
	
	private final String hostName;	
	private final int port;
	
	private final int localPort;
	
	protected List<Object> ours;
	protected List<Object> theirs;
	
    // package-private for access in accept()
	final boolean[] hasBeenAccepted = { false };

    protected SJFifoPairConnection(String hostName, int port, int localPort, List<Object> ours, SJTransport transport) {
        super(transport);
        this.hostName = hostName;
		this.port = port;
		this.localPort = localPort;
		
		this.ours = ours;
    }
	
	public void disconnect() {		
		if (theirs != null) // FIXME: need an isClosed, e.g. delegation protocol closes early.
		{
			boolean closed = false;			
			
			synchronized (theirs)
			{
				if (!theirs.isEmpty() && theirs.get(theirs.size() - 1) == EOF)
				{
					ours = theirs = null;
					
					closed = true;
				}
			}
			
			if (!closed)
			{		
				synchronized (ours) // FIXME: probably can get bad interleavings with the above synchronization between the two peers. Need a common lock to synchronize on for close.
				{			
					ours.add(ours.size(), EOF);
				}
			}
		}

        try {
            SJFifoPair.freePort(localPort); // This is a problem if we try to reuse server port for accepted connections a la TCP.
        } catch (SJIOException e) {
            SJFifoPair.logger.log(Level.WARNING, "Could not cleanly free port", e);
        }
    }

  public void writeByte(byte b) throws SJIOException
  {    
  	if (theirs == null) // e.g. forwarding protocol closes connections early. Remember: we are at transport level here, don't get confused by session-type level properties, e.g. writes can happen asynchronously (i.e. by both ends at the same time) and we may incorrectly try to use closed connections, etc. 
  	{
  		throw new SJIOException('[' + getTransportName() + "] Connection already closed.");
  	}
  	
  	synchronized(theirs)
  	{
  		if (!theirs.isEmpty() && theirs.get(theirs.size() - 1) == EOF)
  		{
  			throw new SJIOException('[' + getTransportName() + "] Connection closed by peer.");
  		}
  		
  		theirs.add(b);
  		theirs.notifyAll();
  	}   
  }

  public void writeBytes(byte[] bs) throws SJIOException
  {
  	if (theirs == null)  
  	{
  		throw new SJIOException('[' + getTransportName() + "] Connection already closed.");
  	}  	
  	
  	synchronized(theirs)
  	{
  		if (!theirs.isEmpty() && theirs.get(theirs.size() - 1) == EOF)
  		{
  			throw new SJIOException('[' + getTransportName() + "] Connection closed by peer.");
  		}  		
  		
  		theirs.add(bs); // FIXME: should copy-on-send. But should be OK, we're always writing the serialized messages (i.e. already copied and won't be modified)? 
  		theirs.notifyAll();
  	}
  }

  public byte readByte() throws SJIOException
  {
  	if (theirs == null)  
  	{
  		throw new SJIOException('[' + getTransportName() + "] Connection already closed.");
  	}  	
  	
  	synchronized(ours)
  	{
  		try
  		{
	  		while (ours.isEmpty())
	  		{
	  			ours.wait();
	  		}
  		}
  		catch (InterruptedException ie) 
  		{
				throw new SJIOException(ie);
			}
  		
  		Object o = ours.remove(0);
  		
  		if (o instanceof Byte) // Needed?
  		{
  			return (Byte) o;
  		}
  		else
  		{
  			throw new SJIOException('[' + getTransportName() + "] Connection closed by peer.");
  		}
  	}  	  	
  }

  public void readBytes(byte[] bs) throws SJIOException
  {  
  	if (theirs == null) 
  	{
  		throw new SJIOException('[' + getTransportName() + "] Connection already closed.");
  	}  	
  	
  	synchronized(ours)
  	{  	
	  	try
			{
				while (ours.isEmpty())
				{
					ours.wait();		
				}
			}
			catch (InterruptedException ie) 
			{
				throw new SJIOException(ie);
			}  		
		
  		//Object o = ours.remove(0);
			
  		//if (ours instanceof byte[])
  		{
  			byte[] foo = (byte[]) ours.remove(0);
  			
  			if (foo.length != bs.length) // FIXME: need to decide whether we should block until bs can be filled or what.
    		{
    			throw new SJIOException('[' + SJFifoPair.TRANSPORT_NAME + "] Bad buffer size: " + bs.length);
    		}
    		
    		System.arraycopy(foo, 0, bs, 0, bs.length); // FIXME: need to buffer any extra data that doesn't fit into bs. // FIXME: could optimise by returning foo;  			
  		}
  		/*else
  		{
  			throw new SJIOException("[" + getTransportName() + "] Connection closed by peer.");
  		}*/ 		  		
  	}
  }

  public void writeReference(Object o)
  {
  	synchronized (theirs)
    {
		  theirs.add(o);
		  theirs.notifyAll();
    }
  } 
  
  public Object readReference() throws SJIOException
  {
  	synchronized(ours)
  	{
	  	try
			{
				while (ours.isEmpty())
				{
					ours.wait();		
				}
			}
			catch (InterruptedException ie) 
			{
				throw new SJIOException(ie);
			}   		
			
  		return ours.remove(0);
  	}
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
	
    protected List<Object> getOurFifo()
	{
		return ours;
	}
	
	protected void setPeerFifo(List<Object> theirs)
	{
		this.theirs = theirs;
	}	
}

/**
 * @author Raymond
 *
 */
public class SJFifoPair extends AbstractSJTransport
{
    static final Logger logger = SJRuntimeUtils.getLogger(SJFifoPair.class);
    
    private static final File locksDir;
    private static final File dirLock;
    static {
        // FIXME: Still doesn't seem reliable
        String tmpDir = System.getProperty("java.io.tmpdir");
        String separator = System.getProperty("file.separator");
        locksDir = new File(tmpDir + separator + "sessionj-fifopair-ports");
        if (!locksDir.isDirectory()) {
            boolean created = locksDir.mkdir();
            if (!created) logger.log(Level.SEVERE, "Directory " + locksDir + " could not be created, transport will not work reliably");
        }
        dirLock = new File(locksDir, "lock");
        if (!dirLock.isFile()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                dirLock.createNewFile();
                // We only create it if it wasn't there already, so it's ok if the method returns false.
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Lock file " + dirLock + " could not be created, transport will not work", e);
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                deleteDir(locksDir);
            }
        });
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) { // Sometimes finding a null pointer exception here.
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) return false;
            }
        }

        return dir.delete();
    }

    public static final String TRANSPORT_NAME = "sessionj.runtime.transport.sharedmem.SJFifoPair";
	
	private static final int LOWER_PORT_LIMIT = 1024; 
	private static final int PORT_RANGE = 65535 - 1024;
	
	public SJFifoPair() { }

	public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters param) throws SJIOException
	{
		SJFifoPairAcceptor a = new SJFifoPairAcceptor(port, this);
		
		//RAY
		bindPort(port);
		//YAR
		
		return a;
	}
	
	public SJFifoPairConnection connect(String hostName, int port) throws SJIOException
	{
		// FIXME: need to map session-level addresses to transport specific values. 
		
		try
		{
			if (notLocalHost(hostName))
			{
				throw new SJIOException('[' + getTransportName() + "] Connection not valid: " + hostName + ':' + port);
			}
		}
        catch (UnknownHostException e) {
            throw new SJIOException(e);
        } 

    //RAY
		//*
		if (!portInUse(port)) 
		{
			//throw new SJIOException('[' + getTransportName() + "] Port not open: " + port);
		}//*/
		//YAR
		
		int localPort = getFreePort();
		
		//RAY
		bindPort(localPort); // Can the connection establishment after this fail? Would need to free the port.
		//YAR
		
		SJFifoPairConnection ourConn = new SJFifoPairConnection(hostName, port, localPort, new LinkedList<Object>(), this);
		
		SJFifoPairAcceptor.addRequest(port, ourConn);
		
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
				throw new SJIOException('[' + getTransportName() + "] 2: " + ie, ie);
			}
    }	
		
		return ourConn;
	}

    private boolean notLocalHost(String hostName) throws UnknownHostException {
        // FIXME: check properly. We're now using IP addresses rather than host names. 
        return !(hostName.equals("127.0.0.1")
            || hostName.equals(InetAddress.getLocalHost().getHostAddress())
            || hostName.equals("localhost")
            || hostName.equals(InetAddress.getLocalHost().getHostName()));
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
	
	private static synchronized boolean portFree(int port)
	{
				//RAY
        /*FileLock lock = null;
        try {
            lock = lock();
            return portFile(port).isFile();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not lock port directory, assuming port "+port+" is free", e);
            return true;
        } finally {
            close(lock);
        }*/
				return true;
				//YAR
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
		
		throw new SJIOException('[' + TRANSPORT_NAME + "] No free port available.");
	}
	
    // Needs to be synchronized to prevent several threads to try and lock the
    // lock file: Java file locks are at process level, not thread level.
	protected synchronized static void bindPort(int port) throws SJIOException {
        //RAY
				/*File portFile = portFile(port);
        boolean created;
        FileLock lock = null;
        try {
            lock = lock();
            created = portFile.createNewFile();
        } catch (IOException e) {
            throw new SJIOException(e);
        } finally {
            close(lock);
        }
        if (!created) throw new SJIOException("Could not create port file:" + portFile);
        */
		//YAR
	}

    private static void close(FileLock lock) {
        if (lock != null) try {
            lock.release();
            lock.channel().close();
        } catch (IOException ignored) {
            // or rethrow?
        }
    }

    private static FileLock lock() throws IOException {
        // Channel is closed in caller.
        //noinspection ChannelOpenedButNotSafelyClosed
        return new RandomAccessFile(dirLock, "rw").getChannel().lock();
    }

    private static File portFile(int port) {
        return new File(locksDir, Integer.toString(port));
    }

    protected static void  freePort(int port) throws SJIOException {
        File file = portFile(port);
        boolean deleted = file.delete();
        if (!deleted && file.isFile()) throw new SJIOException("Could not delete port file: " + file
            + ", port will still be considered in use");
	}
	
	public String sessionHostToNegotiationHost(String hostName)
	{
		return hostName;
	}
	
	public int sessionPortToSetupPort(int port)
	{
		return port;
	}
}
