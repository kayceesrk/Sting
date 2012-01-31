package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJIncompatibleSessionException;
import sessionj.runtime.net.SJServerSocket;
import sessionj.runtime.transport.tcp.InputState;

public interface SJAcceptProtocol {
    InputState initialAcceptState(SJServerSocket serverSocket) throws SJIOException, SJIncompatibleSessionException;
}
