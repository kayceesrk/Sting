package sessionj.runtime2.service;

import sessionj.runtime2.net.*;

abstract public class SJServiceComponent_c extends SJComponent_c implements SJServiceComponent
{
	protected SJServiceComponent_c(SJComponentId cid)
	{
		super(cid);
	}
	
	public final SJComponentId getServiceId()
	{
		return getComponentId();
	}
	
	/*public void doOutput()
	{
		
	}
	
	public void doInput()
	{
		
	}*/
}
