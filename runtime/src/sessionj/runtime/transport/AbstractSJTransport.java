package sessionj.runtime.transport;

import sessionj.runtime.net.*;

public abstract class AbstractSJTransport implements SJTransport {
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
	    return o instanceof SJTransport 
		    && getTransportName().equals(((SJTransport) o).getTransportName());
    }

    /**
     * Default implementation if async mode is unsupported.
     * @return null, always
     */
    public TransportSelector transportSelector() {
        return new TransportSelector() {
	        public boolean registerAccept(SJSelectorInternal sjSelector, SJServerSocket ss) {
		        return false;
	        }

	        public boolean registerInput(SJSelectorInternal sjSelector, SJConnection s) {
		        return false;
	        }

	        public boolean deregisterInput(SJConnection sc) {
		        throw new UnsupportedOperationException("Dummy Transport Selector");
	        }

	        public boolean deregisterAccept(SJServerSocket ss, SJSelectorInternal selectorInternal) {
		        throw new UnsupportedOperationException("Dummy Transport Selector");
	        }
        };
    }

    public boolean supportsBlocking() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
