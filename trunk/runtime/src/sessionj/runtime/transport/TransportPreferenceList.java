package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.httpservlet.SJHTTPServlet;
import sessionj.runtime.transport.sharedmem.SJBoundedFifoPair;
import sessionj.runtime.transport.sharedmem.SJFifoPair;
import sessionj.runtime.transport.tcp.SJAsyncManualTCP;
import sessionj.runtime.transport.tcp.SJManualTCP;
import sessionj.runtime.transport.tcp.SJStreamTCP;

import java.io.IOException;
import java.util.*;

public final class TransportPreferenceList {
    //private final Map<Character, SJTransport> backingStore;
		private final Map<Class<? extends SJTransport>, SJTransport> backingStore; // Yes, this can be slower than using the character codes, but this is less ad hoc and optimisation can come later. e.g. can use .class.toString().
		
    private final List<SJTransport> preferenceList;
    
    //private final String defaultTransportsCodes;
    private final List<Class<? extends SJTransport>> defaultTransportClasses; 

   //public TransportPreferenceList(Map<Character, SJTransport> backingStore, String defaultTransportsCodes) throws SJIOException {
    public TransportPreferenceList(Map<Class<? extends SJTransport>, SJTransport> backingStore, List<Class<? extends SJTransport>> defaults) throws SJIOException {
        // We do want other instances to change the same map - that's the whole point.
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        this.backingStore = backingStore;
        this.defaultTransportClasses = defaults;
        preferenceList = new LinkedList<SJTransport>();
        //loadTransports(defaultTransportsCodes);
        loadTransports(defaults);
    }
    
  public List<Class<? extends SJTransport>> defaultTransportClasses() 
  {
    return defaultTransportClasses;
  }
    
    public List<SJTransport> defaultTransports() {
        List<SJTransport> defaults = new LinkedList<SJTransport>();
        //for (char c : defaultTransportsCodes.toCharArray()) {
        for (Class<? extends SJTransport> c : defaultTransportClasses) {
            defaults.add(backingStore.get(c));
        }        
        return defaults;
    }
    
    /*// It would probably be better if this routine took the transport classes and made instances as appropriate. And the "letter codes" should be moved back to SJTransportUtils.
    public List<SJTransport> loadTransports(String transportLetterCodes) throws SJIOException {
        List<SJTransport> ts = new LinkedList<SJTransport>();
        for (char c : transportLetterCodes.toCharArray()) {
            SJTransport t = backingStore.get(c);
            if (t == null) { // If the system has not already loaded a transport component requested by a session, load it now. However, I think this affects the "default" value for transports across the system. Need to factor out an orthogonal defaults value again.
                t = SJTransportManager_c.createTransport(c);
                backingStore.put(c, t);
            }
            if (!preferenceList.contains(t)) {
                preferenceList.add(t);
            }
            ts.add(t);
        }
        return ts;
    }*/

  public List<SJTransport> loadTransports(List<Class<? extends SJTransport>> cs) throws SJIOException 
  {
    List<SJTransport> ts;
    
    ts = new LinkedList<SJTransport>();
  	//ts = Collections.unmodifiableList(ts);

  	try
  	{	    
	    for (Class<? extends SJTransport> c : cs) 
	    {
	    	SJTransport t;
	    	
	    	if (backingStore.containsKey(c))
	    	{
	  			t = backingStore.get(c);    		
	    	}
	    	else // If the system has not already loaded a transport component requested by a session, load it now. 
	    	{
	    		t = c.newInstance(); // This is the only place where we instance transport components.
	    		
	  			backingStore.put(c, t);  		
	    	}
								
				if (!preferenceList.contains(t)) // So it's possible for a transport to be already in the backing store, but not in the preference list? 
				{
					preferenceList.add(t);
				}
				
				ts.add(t);		
			}
  	}
  	catch (IllegalAccessException iae) // Could make failure more fine-grained.
  	{
  		throw new SJIOException(iae);
  	}
  	catch (InstantiationException ie)
  	{
  		throw new SJIOException(ie);
  	}
		return ts;
  }    
    
    public List<SJTransport> getActive() {
        synchronized (preferenceList)
        {
            return Collections.unmodifiableList(preferenceList);
        }
    }
 }
