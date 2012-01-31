package sessionj.runtime2.net;

//import java.io.Serializable;
import java.util.*;

//import sessionj.runtime2.transport.*;

public class SJSessionParameters //implements Serializable
{
	//public static final SJSessionParameters DEFAULT_PARAMETERS = new SJSessionParameters();
	
	private List<SJComponentId> services;
	private List<SJComponentId> setups;
	private List<SJComponentId> transports;

	public SJSessionParameters()
	{
		// TODO.
	}

	public SJSessionParameters(List<SJComponentId> services, List<SJComponentId> setups, List<SJComponentId> transports)
	{
		this.services = new LinkedList<SJComponentId>(services); // Relying on implicit iterator ordering.
		this.setups = new LinkedList<SJComponentId>(setups); 
		this.transports = new LinkedList<SJComponentId>(transports);
	}

	public List<SJComponentId> getServices()
	{
		return new LinkedList<SJComponentId>(services);
	}
	
	public List<SJComponentId> getSetups()
	{
		return new LinkedList<SJComponentId>(setups);
	}

	public List<SJComponentId> getTransports()
	{
		return new LinkedList<SJComponentId>(transports);
	}

	public String toString()
	{
		return "SJSessionParameters(services=" + getServices().toString() + ", setups=" + getSetups().toString() + ", transports=" + getTransports().toString() + ")";
	}
}
