package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

public abstract class AbstractSJAuthenticatingTransport extends AbstractSJTransport
{
	public SJConnection connect(String hostName, int port) throws SJIOException
	{
		throw new SJIOException("[AbstractSJAuthenticatingTransport] Missing authentication details.");
	}
}
