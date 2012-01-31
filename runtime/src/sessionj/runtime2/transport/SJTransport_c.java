package sessionj.runtime2.transport;

import sessionj.runtime.*;
import sessionj.runtime2.net.SJComponent;
import sessionj.runtime2.net.SJComponentId;

/**
 * @author Raymond
 * 
 */
abstract public class SJTransport_c extends SJTransportComponent_c implements SJTransport 
{
	protected SJTransport_c(SJComponentId cid)
	{
		super(cid);
	}
}
