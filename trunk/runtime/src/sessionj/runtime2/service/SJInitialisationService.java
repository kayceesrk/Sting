package sessionj.runtime2.service;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;

public interface SJInitialisationService extends SJServiceComponent
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.SJInitialisationService");
	
	public void doDualityCheck(SJSocket s) throws SJIncompatibleSessionException, SJIOException;
}
