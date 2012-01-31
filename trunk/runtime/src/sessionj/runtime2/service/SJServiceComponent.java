package sessionj.runtime2.service;

import sessionj.runtime2.net.*;

public interface SJServiceComponent extends SJComponent
{
	public SJComponentId getServiceId();
	
	/*public void doOutput(SJSocket s, Object arg) throws SJIOException;
	public Object doInput(SJSocket s) throws SJIOException;*/
}
