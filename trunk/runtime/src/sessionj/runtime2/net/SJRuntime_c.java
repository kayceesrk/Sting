package sessionj.runtime2.net;

import java.util.*;

import sessionj.runtime2.*;
import sessionj.runtime2.service.*;
import sessionj.runtime2.util.*;

public class SJRuntime_c extends SJRuntime
{	
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.net.SJRuntime_c");
	
	private final Map<SJComponentId, SJServiceComponent> services = new HashMap<SJComponentId, SJServiceComponent>();
	
	//private final Map<SJServerPort, SJAcceptorThreadGroup> serverPorts = new HashMap<SJServerPort, SJAcceptorThreadGroup>();
	private final Set<Integer> clientPorts = new HashSet<Integer>();
	
	protected SJRuntime_c() 
	{
		super(COMPONENT_ID);
		
		init();
	}

	protected void init() // Customised (subclass) Runtimes might want to override this method with a new bootstrap service.
	{
		//reset(new SJBootstrapService_c());
		
		// Find if a bootstrap has been specified in a configuration file.
		
		SJBootstrapService bs = new SJBootstrapService_c();
		
		services.clear();
		
		this.addService(bs);
		this.addService(SJBootstrapService.COMPONENT_ID, bs);
		
		bs.registerServices(services);
	}
	
	/*// If this is disallowed, then can avoid synchronization on services? 
	public synchronized void reset(SJBootstrapService bs)
	{
		// Disallow if any sessions are active?
		
		services.clear();
		
		this.addService(bs);
		this.addService(SJBootstrapService.COMPONENT_ID, bs);
		
		bs.registerServices(this);
	}
	
	public synchronized void reset(SJBootstrapService bs, List<Class<?>> serviceClasses)
	{
		services.clear();
				
		this.addService(bs);
		this.addService(SJBootstrapService.COMPONENT_ID, bs);
		
		bs.registerServices(this, serviceClasses);
	}
	
	public synchronized void reset(List<Class<?>> serviceClasses)
	{
		services.clear();
		
		((SJBootstrapService) this.getService(SJBootstrapService.COMPONENT_ID)).registerServices(this, serviceClasses);
	}*/

	public void acceptSession(SJSocket s) throws SJIOException
	{
		SJInitialisationService is = (SJInitialisationService) s.getService(SJInitialisationService.COMPONENT_ID);
		
		is.doDualityCheck(s);
	}
	
	public void requestSession(SJSocket s) throws SJIOException
	{
		SJInitialisationService is = (SJInitialisationService) s.getService(SJInitialisationService.COMPONENT_ID);
		
		is.doDualityCheck(s);
	}
	
	public void closeSession(SJSocket s)
	{
		
	}
	
	protected SJServiceComponent getService(SJComponentId cid) // Don't need synchronization (if services cannot be modified after initialisation).
	{
		return services.get(cid);
	}
	
	protected void addService(SJServiceComponent sc) 
	{
		addService(sc.getServiceId(), sc);
	}
	
	protected void addService(SJComponentId cid, SJServiceComponent sc)
	{
		services.put(cid, sc);
	}
}
