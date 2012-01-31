package sessionj.runtime2.transport;

import java.util.*;

/**
 * 
 * @author Raymond
 *
 */
public class SJTransportParameters
{
	private List<SJTransport> setups;// = new LinkedList<SJTransport>(); 
	private List<SJTransport> transports;// = new LinkedList<SJTransport>();
		
	public SJTransportParameters(List<SJTransport> setups, List<SJTransport> transports)
	{
		/*Collections.copy(this.setups, setups);
		Collections.copy(this.transports, transports);*/
		
		this.setups = new LinkedList<SJTransport>(setups); // Relying on implicit iterator ordering.
		this.transports = new LinkedList<SJTransport>(transports);		
	}
	
	public List<SJTransport> getSetups() 
	{
		return new LinkedList<SJTransport>(setups);
	}
	
	public List<SJTransport> getTransports() 
	{
		return new LinkedList<SJTransport>(transports);
	}
	
	public String toString()
	{
		return "SJTransportParameters(" + getSetups().toString() + ", " + getTransports().toString() + ")";
	}
}
