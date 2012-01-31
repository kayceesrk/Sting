/**
 * 
 */
package sessionj.types.contexts;

import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.sesstypes.SJUnknownType;
import sessionj.types.typeobjects.SJNamedInstance;

import java.util.HashMap;
import java.util.Set;

import polyglot.types.SemanticException;

/**
 * @author Raymond
 *
 */
public class SJContextElement_c implements SJContextElement
{
	private HashMap<String, SJNamedInstance> channels = new HashMap<String, SJNamedInstance>();
	private HashMap<String, SJNamedInstance> sockets = new HashMap<String, SJNamedInstance>(); // To check if a session-try can create a session scope for the socket. // Actually, not used for that any more. 
	private HashMap<String, SJNamedInstance> servers = new HashMap<String, SJNamedInstance>(); 
	private HashMap<String, SJNamedInstance> selectors = new HashMap<String, SJNamedInstance>();
	
	private HashMap<String, SJSessionType> services = new HashMap<String, SJSessionType>(); // Basically, servicesInScope. // Could be refined to server types. // Services are server sockets that have been opened (are listening)?
	private HashMap<String, SJSessionType> sessions = new HashMap<String, SJSessionType>();
	private HashMap<String, SJSessionType> active = new HashMap<String, SJSessionType>();
	private HashMap<String, SJSessionType> implemented = new HashMap<String, SJSessionType>();
	private HashMap<String, SJSessionType> selectorsInScope = new HashMap<String, SJSessionType>(); // Could be refined to SJSetTypes. Actually, we don't strictly need to record the types here since it is the same as for "selectors", but it is convenient to do so. 

    public SJContextElement_c()
	{
		
	}
	
	public SJContextElement_c(SJContextElement ce)
	{
		if (ce != null) // Should be factored outside of this class (SJContext_c)?
		{
			SJContextElement_c foo = (SJContextElement_c) ce;
			
			// SJNamedInstance is currently immutable, OK to alias.
			this.channels.putAll(foo.channels);
			this.sockets.putAll(foo.sockets);
			this.servers.putAll(foo.servers);
			this.selectors.putAll(foo.selectors);
			this.services.putAll(foo.services);
			this.sessions.putAll(foo.sessions);
			this.active.putAll(foo.active);
			this.selectorsInScope.putAll(foo.selectorsInScope);
			//this.implemented.putAll(foo.implemented); // Would this ever be needed?
		}
	}
	
	public SJNamedInstance getChannel(String sjname)
	{
		return channels.get(sjname); 
	}
		
	public SJNamedInstance getSocket(String sjname)
	{
		return sockets.get(sjname); 
	}
	
	public SJNamedInstance getServer(String sjname)
	{
		return servers.get(sjname); 
	}
	
	public SJNamedInstance getSelector(String sjname)
	{
		return selectors.get(sjname); 
	}	
	
	public void setChannel(SJNamedInstance ni)
	{
		channels.put(ni.sjname(), ni);
	}
	
	public void setSocket(SJNamedInstance ni)
	{
		sockets.put(ni.sjname(), ni);
	}

	public void setServer(SJNamedInstance ni)
	{
		servers.put(ni.sjname(), ni);
	}
	
	public void setSelector(SJNamedInstance ni)
	{
		selectors.put(ni.sjname(), ni);
	}
	
	public SJSessionType getService(String sjname)
	{
		return services.get(sjname);
	}
	
	public SJSessionType getSession(String sjname)
	{
		return sessions.get(sjname);
	}
	
	public SJSessionType getActive(String sjname)
	{
        return active.get(sjname);
	}
	
	public SJSessionType getImplemented(String sjname)
	{
		return implemented.get(sjname);
	}

	public SJSessionType getSelectorType(String sjname)
	{
		return selectorsInScope.get(sjname);
	}
	
	public void setService(String sjname, SJSessionType st)
	{
		services.put(sjname, st);
	}
	
	public void setSession(String sjname, SJSessionType st)
	{
		sessions.put(sjname, st);
	}
	
	public void setActive(String sjname, SJSessionType st)
	{
		active.put(sjname, st);
	}
	
	public void setImplemented(String sjname, SJSessionType st)
	{
		implemented.put(sjname, st);
	}
	
	public void setSelectorInScope(String sjname, SJSessionType st)
	{
		selectorsInScope.put(sjname, st);
	}
	
	public Set<String> channelSet()
	{
		return channels.keySet();
	}
	
	public Set<String> socketSet()
	{
		return sockets.keySet();
	}
	
	public Set<String> serverSet()
	{
		return servers.keySet();
	}
	
	public Set<String> selectorSet()
	{
		return selectors.keySet();
	}
	
	public Set<String> servicesInScope()
	{
		return services.keySet();
	}	
	
	public Set<String> sessionsInScope()
	{
		return sessions.keySet();
	}	
	
	public Set<String> activeSessions()
	{
		return active.keySet(); // Should be the same as implemented.keySet, and also the sessionsInScope that aren't SJUnknownType.
	}
	
	public Set<String> selectorsInScope()
	{
		return selectorsInScope.keySet();
	}
	
	public boolean hasChannel(String sjname)
	{
		return channelSet().contains(sjname); 
	}
	
	public boolean hasSocket(String sjname)
	{
		return socketSet().contains(sjname); 
	}
	
	public boolean hasServer(String sjname)
	{
		return serverSet().contains(sjname); 
	}
	
	public boolean hasSelector(String sjname)
	{
		return selectorSet().contains(sjname); 
	}
	
	public boolean serviceInScope(String sjname)
	{
		return servicesInScope().contains(sjname);
	}
	
	public boolean serviceOpen(String sjname)
	{
		return !(getService(sjname) instanceof SJUnknownType); // Hacky?
	}
	
	public boolean sessionInScope(String sjname)
	{
		return sessionsInScope().contains(sjname);
	}
	
	public boolean sessionActive(String sjname)
	{
		return activeSessions().contains(sjname);
	}

	public boolean selectorInScope(String sjname)
	{
		return selectorsInScope().contains(sjname);
	}

	
    public void checkActiveSessionStartsWith(String sjname, Class<? extends SJSessionType> type, String message) throws SemanticException {
        SJSessionType active = getActive(sjname);
        if (!active.startsWith(type))
            throw new SemanticException(message + active);
    }

    public void clearChannels()
	{
		channels.clear();
	}
	
	public void clearSockets()
	{
		sockets.clear();
	}

	public void clearServices()
	{
		services.clear();
	}
	
	public void clearSessions()
	{
		sessions.clear();
	}
	
	public void clearServers()
	{
		servers.clear();
	}
	
	public void clearSelectors()
	{
		selectors.clear();
	}

	public void clearSelectorsInScope()
	{
		selectorsInScope.clear();
	}
	
	public void removeSession(String sjname)
	{
		sessions.remove(sjname);
		active.remove(sjname);
		implemented.remove(sjname);
	}
	
	/*public SJSessionType getImplementedOverflow(String sjname)
	{
		return overflow.get(sjname);
	}
	
	public void setImplementedOverflow(String sjname, SJSessionType st)
	{
		overflow.put(sjname, st);
	}
	
	public void removeImplementedOverflow(String sjname)
	{
		overflow.remove(sjname);
	}*/
}
