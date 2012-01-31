package sessionj.runtime2.net;

import sessionj.runtime.SJIOException;

public class SJServerPort
{
	private int port;

	private SJSessionParameters params;

	protected SJServerPort(int port, SJSessionParameters params) throws SJIOException
	{
		this.port = port;		
		this.params = params;
	}
	
	public int getValue()
	{
		return port;
	}

	public SJSessionParameters getParameters()
	{
		return params;
	}
	
	public String toString()
	{
		return "SJServerPort(" + port + ")";
	}

	public final boolean equals(Object obj)
	{
		if (obj instanceof SJServerPort)
		{
			return getValue() == ((SJServerPort) obj).getValue();
		}
		else
		{
			return false;
		}
	}

	public final int hashCode()
	{
		return new Integer(getValue()).hashCode();
	}

	public final SJServerPort clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("[SJServerPort] SJPorts are not cloneable: " + getValue());
	}
}
