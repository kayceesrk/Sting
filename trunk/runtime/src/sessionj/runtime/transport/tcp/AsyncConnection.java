package sessionj.runtime.transport.tcp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSelectorInternal;
import sessionj.runtime.session.OngoingRead;
import sessionj.runtime.session.SJDeserializer;
import sessionj.runtime.transport.AbstractSJConnection;
import sessionj.runtime.transport.SJTransport;
import sessionj.runtime.util.SJRuntimeUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import java.util.logging.Logger;

public class AsyncConnection extends AbstractSJConnection
{
    private final SelectingThread thread;
    private final SocketChannel sc;
    private static final Logger log = SJRuntimeUtils.getLogger(AsyncConnection.class);
	private final SJDeserializer deserializer;
	private final BlockingQueue<ByteBuffer> readyInputs = new LinkedBlockingQueue<ByteBuffer>();
	private final Queue<ByteBuffer> requestedOutputs = new ConcurrentLinkedQueue<ByteBuffer>();
	
	private OngoingRead or = null;
	private SJSelectorInternal interestedSelector = null;
	
	AsyncConnection(SelectingThread thread, SocketChannel sc, SJTransport transport, SJDeserializer deserializer) {
        super(transport);
        this.thread = thread;
        this.sc = sc;
		this.deserializer = deserializer;
	}

    public void disconnect() {
        log.fine("Closing channel: " + sc);
        thread.close(sc);
    }

    public void writeByte(byte b) throws SJIOException {
        writeBytes(new byte[] {b});
    }

    public void writeBytes(byte[] bs) throws SJIOException {
	    requestedOutputs.add(ByteBuffer.wrap(bs));
	    if (log.isLoggable(FINER))
		    log.finer("Enqueued write on: " + sc + " of: " + bs.length + " bytes");
        thread.enqueuedOutput(sc);
    }
	
	void setInterestedSelector(SJSelectorInternal selector) {
		if (interestedSelector == null) {
			interestedSelector = selector;
		} else if (!interestedSelector.equals(selector)) {
			throw new IllegalStateException("Tried to set interested selector to: " 
				+ selector + " but already set to: " + interestedSelector);
		}
	}

    /**
     * Non-blocking read from the connection.
     * @throws NullPointerException If called when no data is ready on this connection (ie. not after a select call).
     */
    public byte readByte() throws SJIOException {
        ByteBuffer input = checkAndDequeue(1);
        
        return input.get();
    }

    private ByteBuffer checkAndDequeue(int remaining) throws SJIOException {
        ByteBuffer input = readyInputs.peek();
	    if (log.isLoggable(FINEST))
		    log.finest("Peeked input for: " + sc + ", input: " + input);
        if (input == null) {
            throw new SJIOException("No available inputs on connection: " + this);
        }
        
        if (log.isLoggable(FINEST))
            log.finest("Returning input: " + input);

        if (input.remaining() == remaining) {
	        if (log.isLoggable(FINEST))
		        log.finest("Dequeueing input from: " + sc);
            readyInputs.remove();
        }
        
        return input;
    }
    
    /**
     * Non-blocking read from the connection.
     * @throws NullPointerException If called when no data is ready on this connection (ie. not after a select call).
     */
    public void readBytes(byte[] bs) throws SJIOException {
        ByteBuffer input = checkAndDequeue(bs.length);
        input.get(bs, 0, bs.length);
    }

    public void flush() throws SJIOException {
        // Do nothing; to do anything meaningful we would need to make
        // this blocking, and all the writeXXX() methods in the serializers call
        // this method.
    }

    public String getHostName()
    {
        return sc.socket().getInetAddress().getHostName();
    }

    public int getPort()
    {
        return sc.socket().getPort();
    }

    public int getLocalPort()
    {
        return sc.socket().getLocalPort();
    }

	// For AsyncManualTCPSelector
    SocketChannel socketChannel() {
        return sc;
    }

	@Override
	public boolean arrived() {
		return !readyInputs.isEmpty();
	}

	@Override
    public boolean supportsBlocking() {
        return false;
    }

    @Override
    public String toString() {
        return "AsyncConnection{sc: " + sc + ", readyInputs: " 
	        + readyInputs + ", requestedOutputs: " + requestedOutputs + '}';
    }

	public void consumeBytesRead(SocketChannel sc, ByteBuffer bytes, boolean eof) throws SJIOException {
		if (or == null) {
			// For custom message formatting mode, this does *not* create a new OngoingRead: it returns a singleton. 
			or = deserializer.newOngoingRead();
		}

		while (bytes.remaining() > 0)  {
			or.updatePendingInput(bytes, eof);

			if (or.finished()) {
				ByteBuffer input = or.getCompleteInput();

				if (log.isLoggable(FINER))
					log.finer("Received complete input on channel " + sc + ": " + input);

				readyInputs.add(input);
				// order is important here: notifying makes the read visible to the user-level select
				notifyInput();
				or = deserializer.newOngoingRead();
			} 
		}
	}

	private void notifyInput() {
		if (interestedSelector != null)
			interestedSelector.notifyInput(this);
	}

	public boolean hasMoreWrites() {
		return !requestedOutputs.isEmpty();
	}

	public ByteBuffer peekWrite() {
		return requestedOutputs.peek();
	}

	public void removeWrite() {
		requestedOutputs.remove();
	}
}
