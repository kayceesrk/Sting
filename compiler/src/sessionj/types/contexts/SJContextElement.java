/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;
import polyglot.types.SemanticException;

/**
 * @author Raymond
 *
 */
public interface SJContextElement
{
	SJNamedInstance getChannel(String sjname);
	SJNamedInstance getSocket(String sjname);
	SJNamedInstance getServer(String sjname); 
	SJNamedInstance getSelector(String sjname);
	
	void setChannel(SJNamedInstance ni);
	void setSocket(SJNamedInstance ni);
	void setServer(SJNamedInstance ni);
	void setSelector(SJNamedInstance ni);
	
	// FIXME: factor something out across "inactive" entities, e.g. servers and selectors, and their "active" (and linear) counterparts, e.g. services and selectorsInScope.
	SJSessionType getService(String sjname); // Services are server sockets that are open (i.e. listening). Not to be confused with SJServices (shared channels). // Servers are the server variables, and services are initialised server variables. 
	SJSessionType getSession(String sjname); // The "original" type on entering the session?
	SJSessionType getActive(String sjname);
	SJSessionType getImplemented(String sjname);
	SJSessionType getSelectorType(String sjname); // Could be called getSelectorInScope. 
	
	void setService(String sjname, SJSessionType st);
	void setSession(String sjname, SJSessionType st); // Seems to be the "original" session type when entering a session-try.
    // This is also used to record the sessions in scope, i.e. which sessions operations can be performed on.
	void setActive(String sjname, SJSessionType st); // The remaining session type to be implemented.
    // Used to check, in conjunction with the sessions in scope, whether sessions have been completed.
    // For noalias (not na-final) method parameters, sessions are initially active, but not yet in scope.
	void setImplemented(String sjname, SJSessionType st); // The type of the session implemented so far.
	void setSelectorInScope(String sjname, SJSessionType st); // Selectors-in-scope vs. selectors analogous to services vs. servers.
	
	Set<String> channelSet();
	Set<String> socketSet();
	Set<String> serverSet();
	Set<String> selectorSet();
	Set<String> servicesInScope();
	Set<String> sessionsInScope();
	Set<String> activeSessions();
	Set<String> selectorsInScope();

    // TODO replace these by checkXXX methods that throw a SemanticException if the result is false
	boolean hasChannel(String sjname);
	boolean hasSocket(String sjname);
	boolean hasServer(String sjname);
	boolean hasSelector(String sjname);
	
	boolean serviceInScope(String sjname); // Basically the "in scope" versions of the above "has" methods.
	boolean serviceOpen(String sjname);
	boolean sessionInScope(String sjname);
	boolean sessionActive(String sjname);
	boolean selectorInScope(String sjname);
	
    void checkActiveSessionStartsWith(String sjname, Class<? extends SJSessionType> type, String message) throws SemanticException;
	
	void clearChannels();
	void clearSockets();
	void clearServices();
	void clearSessions();
	void clearServers();
	void clearSelectors();
	void clearSelectorsInScope();
	
	void removeSession(String sjname);
	
	/*public SJSessionType getImplementedOverflow(String sjname); // Hack for fixing delegation from within branch cases.
	public void setImplementedOverflow(String sjname, SJSessionType st);
	public void removeImplementedOverflow(String sjname);*/
	
	//public boolean canDelegate(String sjname);
}
