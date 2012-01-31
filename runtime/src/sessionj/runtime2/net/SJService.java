package sessionj.runtime2.net;

import java.net.*;

import sessionj.runtime2.*;

public class SJService 
{
	//public static final long serialVersionUID = SJ_VERSION;

	private SJProtocol protocol;
	private SJSessionAddress target;

	protected SJService() // Seems to be needed for SJServiceSocketHack.
	{
		
	}
	
	private SJService(SJProtocol protocol, SJSessionAddress target)
	{
		this.protocol = protocol;
		this.target = target;
	}

	public static SJService create(SJProtocol protocol, String hostName, int port) throws UnknownHostException
	{
		return new SJService(protocol, SJSessionAddress.createFromHostName(hostName, port));
	}

	/*public static SJService create(SJProtocol protocol, String hostName, SJPort port) // Could allow SJPort or otherwise allow service/setup/transport preferences to be recorded.
	{
		...
	}*/

	public SJSocket request() throws SJIOException, SJIncompatibleSessionException
	{
		//return request(SJSessionParameters.DEFAULT_PARAMETERS); // Use default setups and transports.
		
		return null;
	}

	public SJSocket request(SJSessionParameters params) throws SJIOException, SJIncompatibleSessionException
	{
		/*SJRequestingSocket s = new SJRequestingSocket(this, params);

		SJRuntime.connectSocket(s);
		SJRuntime.request(s);

		return s;*/
		
		return null;
	}

	public SJProtocol getProtocol()
	{
		return protocol;
	}

	public SJSessionAddress getTargetAddress()
	{
		return target;
	}

	public String toString()
	{
		return getProtocol().getSessionType() + "@" + getTargetAddress();
	}
}
