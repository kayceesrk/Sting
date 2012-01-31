package sessionj.runtime2.service;

import java.io.Serializable;
import java.util.*;

import sessionj.runtime.transport.*;
import sessionj.runtime2.transport.SJTransport;
import sessionj.runtime2.transport.SJTransportParameters;

/**
 * 
 * @author Raymond
 *
 * FIXME: the default is mostly a performance hack. It can't be used to e.g. see which transports were actually used (the ones that were the defaults at the time). 
 * 
 */
public class SJSessionParameters //implements Serializable
{
	private SJTransportParameters transports;

	public SJSessionParameters(List<SJTransport> setups, List<SJTransport> transports)
	{
		this.transports = new SJTransportParameters(setups, transports);
	}
	
	public SJTransportParameters getTransportParameters() 
	{
		return transports;
	}
	
	public String toString()
	{
		return "SJSessionParameters(" + getTransportParameters() + ")";
	}
}
