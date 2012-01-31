package sessionj.runtime.net;

import sessionj.runtime.SJIOException;

public interface SJSelector {

    void registerAccept(SJServerSocket ss) throws SJIOException;

    void registerInput(SJSocket s) throws SJIOException;

    // Select may throw SJIncompatibleSessionException when finishing accept actions.
    // But this is a bit inconvenient when we are not using a selector in this way.      
    // Will be removed when compiler is updated to allow returning SJServerSockets too;
    // then, the accept() call will be done in the user code.
    SJSocket select() throws SJIOException, SJIncompatibleSessionException;

	void close();
}
