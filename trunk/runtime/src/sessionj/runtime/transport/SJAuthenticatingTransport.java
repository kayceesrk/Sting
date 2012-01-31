package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

public interface SJAuthenticatingTransport extends SJTransport
{
	public SJConnection connect(String hostName, int port, String user, String pwd) throws SJIOException;
}
