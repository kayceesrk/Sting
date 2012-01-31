package sessionj.runtime.transport.tcp;

import sessionj.runtime.net.SJSocket;
import sessionj.runtime.net.SJIncompatibleSessionException;
import sessionj.runtime.SJIOException;

public interface InputState {
    InputState receivedInput() throws SJIOException, SJIncompatibleSessionException;

    SJSocket sjSocket();
}
