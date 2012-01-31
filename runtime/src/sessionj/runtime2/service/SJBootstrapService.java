package sessionj.runtime2.service;

import java.util.*;

import sessionj.runtime2.net.*;

public interface SJBootstrapService extends SJInternalServiceComponent
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.net.SJBootstrapService");
	
	public void registerServices(Map<SJComponentId, SJServiceComponent> services);	
}
