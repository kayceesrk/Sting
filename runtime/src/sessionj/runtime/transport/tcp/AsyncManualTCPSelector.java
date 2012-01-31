package sessionj.runtime.transport.tcp;

import sessionj.runtime.net.SJSelectorInternal;
import sessionj.runtime.net.SJServerSocket;
import sessionj.runtime.net.TransportSelector;
import sessionj.runtime.transport.SJConnection;
import sessionj.runtime.transport.SJTransport;
import sessionj.runtime.util.SJRuntimeUtils;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
class AsyncManualTCPSelector implements TransportSelector {
    private static final Logger log = SJRuntimeUtils.getLogger(AsyncManualTCPSelector.class);
    
    final SelectingThread thread;
    private final SJTransport transport;
	private Map<ServerSocketChannel, SJServerSocket> serverSockets;
	final ConcurrentHashMap<ServerSocketChannel, SJSelectorInternal> interestedSelectorForServerSocket;

	AsyncManualTCPSelector(SelectingThread thread, SJTransport transport) {
        this.thread = thread;
        this.transport = transport;
		serverSockets = new HashMap<ServerSocketChannel, SJServerSocket>();
		interestedSelectorForServerSocket = new ConcurrentHashMap<ServerSocketChannel, SJSelectorInternal>();
    }

    @SuppressWarnings({"MethodParameterOfConcreteClass"})
    public boolean registerAccept(SJSelectorInternal sel, SJServerSocket ss) {
        ServerSocketChannel ssc = retrieveServerSocketChannel(ss);

	    //noinspection RedundantIfStatement
	    if (ssc != null) {
            // Not doing the real registration with the selecting thread,
            // this is done by the acceptor, ahead of time.
            // When the real registration is done twice, a race condition (occasional deadlocks) happens.
		    serverSockets.put(ssc, ss);
		    interestedSelectorForServerSocket.put(ssc, sel);
		    
            return true;
        }
        return false;
    }
	
	void notifyAccepted(ServerSocketChannel ssc, SocketChannel socketChannel, AsyncConnection connection) {
		SJServerSocket sjss = serverSockets.get(ssc);
		SJSelectorInternal sel = interestedSelectorForServerSocket.get(ssc);
		connection.setInterestedSelector(sel);
		sel.notifyAccept(sjss, connection);
	}


	public boolean registerInput(SJSelectorInternal sel, SJConnection connection) {
		SocketChannel sc = retrieveSocketChannel(connection);
		if (sc != null) {
			if (log.isLoggable(Level.FINER))
				log.finer("Registering: " + sc);
			AsyncConnection asyncConnection = (AsyncConnection) connection;
			asyncConnection.setInterestedSelector(sel);
			thread.registerInput(sc, asyncConnection);
			return true;
		}
		return false;
	}

	public boolean deregisterInput(SJConnection sc) {
		SocketChannel schan = retrieveSocketChannel(sc);
		if (schan != null) {
			thread.deregister(schan);
			return true;
		}
		return false;
	}

	public boolean deregisterAccept(SJServerSocket ss, SJSelectorInternal selectorInternal) {
		ServerSocketChannel ssc = retrieveServerSocketChannel(ss);
		if (ssc != null) {
			thread.deregister(ssc);
			return true;
		}
		return false;
	}

	private SocketChannel retrieveSocketChannel(SJConnection connection) {
        SocketChannel sc = null;
        if (connection instanceof AsyncConnection) {
            sc = ((AsyncConnection) connection).socketChannel();
            assert sc != null;
        }
        return sc;
    }
    
    private ServerSocketChannel retrieveServerSocketChannel(SJServerSocket ss) {
        AsyncTCPAcceptor acceptor = (AsyncTCPAcceptor) 
            ss.getAcceptorFor(transport.getTransportName());
        if (acceptor == null) {
            return null;
        } else {
            return acceptor.ssc;
        }
    }
}
