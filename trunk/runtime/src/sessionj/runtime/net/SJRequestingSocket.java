package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.types.sesstypes.SJSessionType;

public class SJRequestingSocket extends SJAbstractSocket
{
	private SJService service;
        public String participantName;  //<By MQ> Updated to support multiple participants
	SJRequestingSocket(SJService service, SJSessionParameters params) throws SJIOException
	{
		super(service.getProtocol(), params); // Could override getProtocol but no point.
		
		this.service = service;
		
		//setParameters(params);
	}

    /**
     * For session receive: type can be a set type, need to know the actual runtime type for typecase
     */
	public SJRequestingSocket(SJProtocol p, SJSessionParameters params, SJSessionType receivedRuntimeType) throws SJIOException
	{
		super(p, params, receivedRuntimeType); // FIXME: null service OK? Probably OK for received sessions.
	}

    @Override
    public boolean isOriginalRequestor() {
        return true;
    }

    public SJServerIdentifier getServerIdentifier()
	{
	        return service.getServerIdentifier(participantName);//<By MQ> Updated to support multiple participants
	}
}
