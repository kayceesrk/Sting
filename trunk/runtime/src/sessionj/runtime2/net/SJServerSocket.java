/**
 *
 */
package sessionj.runtime2.net;

import java.util.LinkedList;
import java.util.List;

import sessionj.runtime2.*;
import sessionj.runtime.transport.SJAcceptorThreadGroup;
import sessionj.runtime.transport.SJConnection;

/**
 * @author Raymond
 *
 */
abstract public class SJServerSocket
{
	private SJProtocol protocol;
	private SJServerPort port; 

	protected boolean isOpen = false;

	private SJServerSocket(SJProtocol protocol, SJServerPort port) throws SJIOException
	{
		this.protocol = protocol;
		this.port = port;
	}

	public static SJServerSocket create(SJProtocol protocol, int port) throws SJIOException
	{
		//return create(protocol, port, SJSessionParameters.DEFAULT_PARAMETERS);
		
		return null;
	}

	public static SJServerSocket create(SJProtocol protocol, int port, SJSessionParameters params) throws SJIOException
	{
		/*SJServerSocket ss = new SJServerSocketImpl(protocol, port, params);

		//ss.init();

		return ss;*/
		
		return null;
	}

	public static SJServerSocket create(SJProtocol protocol, SJServerPort port) throws SJIOException
	{
		/*SJServerSocket ss = new SJServerSocketImpl(protocol, port); 

		//ss.init(); 

		return ss;*/
		
		return null;
	}

	abstract public SJSocket accept() throws SJIOException, SJIncompatibleSessionException;
	abstract public void close();

	/*abstract protected void init() throws SJIOException;

	abstract protected SJAcceptorThreadGroup getAcceptorGroup();
	abstract protected void setAcceptorGroup(SJAcceptorThreadGroup acceptors);*/

	public SJProtocol getProtocol()
	{
		return protocol;
	}

	public int getLocalPort()
	{
		return port.getValue();
	}

	public boolean isClosed()
	{
		return !isOpen;
	}
	
	public SJServerPort getServerPort()
	{
		return port; 
	}

	public SJSessionParameters getSessionParameters()
	{
		return getServerPort().getParameters();
	}
	
	/*public SJServerSocketCloser getCloser()
	{
		return new SJServerSocketCloser(this);
	}*/
}
