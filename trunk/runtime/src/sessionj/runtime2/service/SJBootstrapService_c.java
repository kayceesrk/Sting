package sessionj.runtime2.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sessionj.runtime2.SJRuntimeException;
import sessionj.runtime2.net.*;

public class SJBootstrapService_c extends SJServiceComponent_c implements SJBootstrapService
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.net.SJBootstrapService_c");
	
	public SJBootstrapService_c()
	{
		super(COMPONENT_ID);
	}
	
	public void registerServices(Map<SJComponentId, SJServiceComponent> services)	
	{
		List<Class<?>> serviceClasses = new LinkedList<Class<?>>();
		
		serviceClasses.add(SJDefaultInitialisation_c.class); // Should be customisable. Configuration file?
		serviceClasses.add(SJDirectStreamSerialization_c.class);
		serviceClasses.add(SJDefaultWireProtocol_c.class);
		
		registerServices(services, serviceClasses);
	}
	
	private void registerServices(Map<SJComponentId, SJServiceComponent> services, List<Class<?>> serviceClasses)
	{			
		try
		{
			for (Class<?> c : serviceClasses)
			{
				SJServiceComponent sc = (SJServiceComponent) c.newInstance();
				
				if (sc instanceof SJStatefulServiceComponent)
				{
					throw new SJRuntimeException("[" + getComponentId() + "] Should not pre-cache stateful service components: " + c);
				}
				
				addService(services, sc);
			}
		}
		catch (IllegalAccessException iae)
		{
			throw new SJRuntimeException(iae);
		}
		catch (InstantiationException ie)
		{
			throw new SJRuntimeException(ie);
		}
	}
	
	private void addService(Map<SJComponentId, SJServiceComponent> services, SJServiceComponent sc) 
	{
		services.put(sc.getServiceId(), sc);
	}
}
