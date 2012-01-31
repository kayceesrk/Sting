/**
 *
 */
package sessionj.runtime2.net;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sessionj.runtime2.SJRuntimeException;

import static sessionj.runtime2.SJRuntimeConstants.*;

public class SJSessionAddress implements Serializable
{
	public static final long serialVersionUID = SJ_RUNTIME_VERSION;
	
	public static final int MIN_SERVER_PORT_VALUE = 1025; 
	public static final int MIN_CLIENT_PORT_VALUE = 49152;  
	public static final int MAX_SESSION_PORT_VALUE = 65535;
	
	private static final String LOCAL_ADDRESS;
	//private static final String LOCAL_NAME; // Cannot look this up reliably, e.g. HZHL2 on IC-DoC (we don't want "HZHL2"). 
	
	private String address; // Session addresses are currently directed mapped to IPv4 addresses.
	private String name = null; // Session host name doesn't have a proper meaning - it's just convenience given we are currently using IPv4 addresses.
	
	private int port; // Abstract session port value.

	static
	{
		try
		{
			LOCAL_ADDRESS = InetAddress.getLocalHost().getHostAddress();
			//LOCAL_NAME = InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException uhe)
		{
			throw new SJRuntimeException(uhe);
		}				
	}
	
	private SJSessionAddress(String address, int port) 
	{
		this.address = address;
		this.port = port;
	}

	public static SJSessionAddress createFromIP4Address(String address, int port) // No validation.
	{
		if (address.equals(LOCAL_HOST_ADDRESS))
		{
			address = LOCAL_ADDRESS;
		}
		
		return new SJSessionAddress(address, port);
	}
	
	public static SJSessionAddress createFromHostName(String name, int port) throws UnknownHostException // IP addresses also accepted.
	{
		String address;
		
		if (name.equals(LOCAL_HOST_NAME) || name.equals(LOCAL_HOST_ADDRESS)) 
		{
			address = LOCAL_ADDRESS;
		}
		else
		{
			address = InetAddress.getByName(name).getHostAddress();	
		}
		
		SJSessionAddress sa = new SJSessionAddress(address, port);
		
		if (!name.equals(LOCAL_HOST_NAME))
		{
			sa.name = name;
		}
		
		return sa;
	}
	
	public String getHostAddress()
	{
		return address;
	}
	
	public String getHostName()
	{
		return name; // Will be null if not explicitly supplied - "reverse" address lookup doesn't work reliabily.
	}

	public int getPort()
	{
		return port;
	}

	public String toString()
	{
		String m = getHostAddress();
		
		if (getHostName() != null)
		{
			m += "(" + getHostName() + ")";
		}
		
		m += ":" + getPort();
		
		return m;
	}
	
	public static String getLocalHostAddress()
	{
		return LOCAL_ADDRESS;
	}
}
