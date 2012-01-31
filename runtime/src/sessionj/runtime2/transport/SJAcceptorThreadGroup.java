/**
 * 
 */
package sessionj.runtime2.transport;

import java.util.*;

import sessionj.runtime.*;

/**
 * @author Raymond
 *
 */
public class SJAcceptorThreadGroup extends ThreadGroup
{
	private SJTransportManager sjtm;
	
	private int port; // The session port.
	
	//private Map<String, Integer> transports = new HashMap<String, Integer>();
	
	private List<SJConnection> pending = new LinkedList<SJConnection>();
	
	private boolean isClosed = false;
	
	public SJAcceptorThreadGroup(SJTransportManager sjtm, int port, String name)
	{
		super(name);
		
		this.sjtm = sjtm;
		this.port = port;
	}
	
	public void close()
	{
		isClosed = true;
	}
	
	protected void appendToConnectionQueue(SJConnection c)
	{

	}
	
	public SJConnection takeFirstConnection() throws SJIOException
	{
		return null;
	}
	
	public int getSessionPort()
	{
		return port;
	}
	
	/*protected void addTransport(String name, int port)
	{
		transports.put(name, new Integer(port));
	}
	
	protected Map<String, Integer> getTransports()
	{
		return transports;
	}*/
	
	public boolean isClosed()
	{
		return isClosed;
	}
}
