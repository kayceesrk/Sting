package sessionj.runtime.session;

import sessionj.runtime.net.SJServerSocket;
import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.tcp.InputState;

public class SJAcceptProtocolImpl implements SJAcceptProtocol {
    public InputState initialAcceptState(SJServerSocket serverSocket) throws SJIOException {
        return new DefaultSJProtocolAcceptState(serverSocket);
    }
}
