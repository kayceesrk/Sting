/**
 * 
 */
package sessionj.runtime.net;

import java.io.*;

/**
 * @author Raymond
 *
 */
public class SJServerIdentifier implements Serializable
{
	private String hostName; // FIXME: IP addresses are less fragile than host names (e.g. HZHL2 on IC-DoC). However, we may lose some compatibility with dynamically updated DNS systems, e.g. MobileIP?
	private int port;
	
	protected SJServerIdentifier(String hostName, int port) // A session peer hostName and session port. 
	{
		this.hostName = hostName;
		this.port = port;
	}
	
	public String getHostName()
	{
		return hostName;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public String toString()
	{
		return hostName + ":" + port;
	}
}
