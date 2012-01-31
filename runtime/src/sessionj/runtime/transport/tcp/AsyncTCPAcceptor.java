package sessionj.runtime.transport.tcp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.*;
import sessionj.runtime.util.SJRuntimeUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class AsyncTCPAcceptor extends AbstractWithTransport implements SJConnectionAcceptor {
    private final SelectingThread thread;
    // package-private so the selector can access it
    final ServerSocketChannel ssc;
    private static final Logger logger = SJRuntimeUtils.getLogger(AsyncTCPAcceptor.class);
	private final AsyncManualTCPSelector transportSelector;
	private final SJSessionParameters params;

	AsyncTCPAcceptor(SelectingThread thread, int port, SJTransport transport, AsyncManualTCPSelector transportSelector, SJSessionParameters params) throws IOException {
	    super(transport);
		this.transportSelector = transportSelector;
		this.params = params;
		this.thread = transportSelector.thread;
		ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(port));
        thread.registerAccept(ssc);
    }

    public SJConnection accept() throws SJIOException {
        try {
            SocketChannel sc = thread.takeAccept(ssc);
            logger.finer("Accepted: " + sc);
	        AsyncConnection connection = createSJConnection(sc);
	        transportSelector.notifyAccepted(ssc, sc, connection);
	        return connection;
        } catch (IOException e) {
            throw new SJIOException(e);
        } catch (InterruptedException e) {
            throw new SJIOException(e);
        }
    }

    public void close() {
        thread.close(ssc);
    }

    public boolean interruptToClose() {
        return false;
    }

    public boolean isClosed() {
        return !ssc.isOpen();
    }

	private AsyncConnection createSJConnection(SocketChannel socketChannel) throws
		IOException,
		SJIOException {
        socketChannel.socket().setTcpNoDelay(SJManualTCP.TCP_NO_DELAY);
		// Important: do not create the deserializer in advance as it can contain some state.
		// A new instance needs to be created for each connection.
		return new AsyncConnection(thread, socketChannel, getTransport(), params.createDeserializer());
    }
}
