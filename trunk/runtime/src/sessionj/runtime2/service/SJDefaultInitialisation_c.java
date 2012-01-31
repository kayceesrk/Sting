package sessionj.runtime2.service;

import sessionj.types.sesstypes.SJSessionType;

import sessionj.runtime2.*;
import sessionj.runtime2.net.*;
import sessionj.runtime2.util.SJRuntimeTypeEncoder;

public class SJDefaultInitialisation_c extends SJInitialisationService_c implements SJDefaultInitialisation
{
	public static final SJComponentId COMPONENT_ID = new SJComponentId("sessionj.runtime2.service.SJDefaultInitialisation_c");
	
	private static final SJRuntimeTypeEncoder sjrte = SJRuntime.SJ_RUNTIME.getRuntimeTypeEncoder();
	
	public SJDefaultInitialisation_c()
	{
		super(COMPONENT_ID);
	}
		
	//Continue from here.
	public void doDualityCheck(SJSocket s) throws SJIncompatibleSessionException, SJIOException 
	{
		/*SJServiceComponent wps = s.getService(...);
		
		String encoded = s.getProtocol().getEncodedSessionType();
		
		wps.doOutput(encoded);
		
		SJSessionType ours = sjrte.decode(encoded);				
		
		try
		{
			SJSessionType theirs = sjrte.decode((String) wps.readObject());
			
			if (!ours.isDualtype(theirs))
			{
				//wps.close(); // The session socket variable will still be null because of this exception, so close won't be called in the finally block - manually close here to flush (call the close protocol instead?)
				//SJRuntime.getTransportManager().closeConnection(wps.getConnection()); // FIXME: this isn't nice. Also need to unbind session socket...
				
				s.close();
				
				throw new SJIncompatibleSessionException("[SJSessionProtocolsImpl] Our session type (" + ours + ") incompatible with theirs: " + theirs);
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new SJRuntimeException(cnfe);
		}
		catch (SJControlSignal cs)
		{
			throw new SJRuntimeException("[SJSessionProtocolsImpl] Unexpected control signal: " + cs);
		}*/
	}	
}
