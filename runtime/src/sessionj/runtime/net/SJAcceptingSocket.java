package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.types.sesstypes.SJSessionType;

public class SJAcceptingSocket extends SJAbstractSocket
{
    /**
     * For accept(): user-level session types cannot be set types.
     */
    public String participantName;  //<By MQ>
    public SJAcceptingSocket(SJProtocol protocol, SJSessionParameters params) throws SJIOException
	{
		super(protocol, params);
	}

    /**
     * For delegation: type can be a set type, need to know actual runtime type for typecase
     */
    public SJAcceptingSocket(SJProtocol protocol, SJSessionParameters params, SJSessionType receivedRuntimeType) throws SJIOException {
        super(protocol, params, receivedRuntimeType);
    }

    @Override
    public boolean isOriginalRequestor() {
        return false;
    }
}
