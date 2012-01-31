package sessionj.runtime2.service;

import sessionj.runtime2.net.*;
import sessionj.runtime2.util.SJRuntimeTypeEncoder;

abstract public class SJDefaultWireProtocol_c extends SJWireProtocolService_c implements SJDefaultWireProtocol
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.net.SJDefaultWireProtocol_c");
	
	private static final SJRuntimeTypeEncoder sjrte = SJRuntime.SJ_RUNTIME.getRuntimeTypeEncoder();
	
	public SJDefaultWireProtocol_c()
	{
		super(COMPONENT_ID);
	}
}
