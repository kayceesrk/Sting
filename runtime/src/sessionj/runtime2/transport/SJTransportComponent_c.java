package sessionj.runtime2.transport;

import sessionj.runtime2.net.*;

abstract public class SJTransportComponent_c extends SJComponent_c implements SJTransportComponent
{
	protected SJTransportComponent_c(SJComponentId cid)
	{
		super(cid);
	}
	
	public final SJComponentId getTransportId()
	{
		return getComponentId();
	}
}
