package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;

public class SJPort
{
	private int port;
	
	private final SJSessionParameters params;
	
	public SJPort(int port) throws SJIOException
	{
		SJRuntime.takePort(port); // Takes the session port, but does not guarantee the setups are available. In keeping with specifying e.g. TCP 8888 - could already have been taken. 
		
		this.port = port;
		
		try
		{
			params = new SJSessionParameters();
		}
		catch (SJSessionParametersException spe)
		{
			throw new SJRuntimeException("[SJPort] Shouldn't get in here.", spe);
		}
	}
	
	public SJPort(int port, SJSessionParameters params) throws SJIOException
	{
		SJRuntime.takePort(port); // Takes the session port, but does not guarantee the setups are available. In keeping with specifying e.g. TCP 8888 - could already have been taken. 
		
		this.port = port;
		this.params = params;
	}
	
	public int getValue()
	{
		return port;
	}
	
	public String toString()
	{
		return "SJPort(" + port + ")";
	}
	
	public final boolean equals(Object obj)
	{
		if (obj instanceof SJPort)
		{
			return getValue() == ((SJPort) obj).getValue();
		}
		else
		{
			return false;
		}
	}

	public final int hashCode()
	{
		return Integer.valueOf(getValue()).hashCode();
	}

	public final SJPort clone() throws CloneNotSupportedException // Open ports aren't cloneable.
	{
		 throw new CloneNotSupportedException("[SJPort] SJPorts are not cloneable: " + getValue());
	}
	
	public SJSessionParameters getParameters()
	{
		return params;
	}
}
