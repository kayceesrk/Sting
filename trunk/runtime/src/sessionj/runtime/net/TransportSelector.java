package sessionj.runtime.net;

import sessionj.runtime.transport.SJConnection;

/**
 * All methods return true to indicate that the SJServerSocket or SJConnection
 * parameter was recognized as belonging to this transport.
 * Otherwise, the methods do nothing and return false.
 */
public interface TransportSelector {
	
    boolean registerAccept(SJSelectorInternal sjSelector, SJServerSocket ss);
    boolean registerInput(SJSelectorInternal sjSelector, SJConnection s);

	boolean deregisterInput(SJConnection connection);
	boolean deregisterAccept(SJServerSocket ss, SJSelectorInternal selectorInternal);

	// At the moment the transport selectors are not explicitly created - nor closed - by the 
	// upper layers of the system, so this is unnecessary. It will probably be needed eventually.
	// void close() throws SJIOException;
}
