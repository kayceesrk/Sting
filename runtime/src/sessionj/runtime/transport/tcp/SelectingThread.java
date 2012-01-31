package sessionj.runtime.transport.tcp;

import sessionj.runtime.SJIOException;
import static sessionj.runtime.transport.tcp.ChangeAction.*;
import sessionj.runtime.util.SJRuntimeUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import static java.nio.channels.SelectionKey.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.*;
import static java.util.logging.Level.*;
import java.util.logging.Logger;

final class SelectingThread implements Runnable {
	private static final Logger log = SJRuntimeUtils.getLogger(SelectingThread.class);
    private static final int BUFFER_SIZE = 16384;
	
    final Selector selector;
    private final ByteBuffer readBuffer;

    private final Queue<ChangeRequest> pendingChangeRequests;
	private final Map<ServerSocketChannel, BlockingQueue<SocketChannel>> accepted;


	SelectingThread() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        pendingChangeRequests = new ConcurrentLinkedQueue<ChangeRequest>();
        accepted = new ConcurrentHashMap<ServerSocketChannel, BlockingQueue<SocketChannel>>();
		
        readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    synchronized void registerAccept(ServerSocketChannel ssc) {
        assert !accepted.containsKey(ssc) : "The channel " + ssc + " has already been registered";
        accepted.put(ssc, new LinkedBlockingQueue<SocketChannel>());
        pendingChangeRequests.add(new ChangeRequest(ssc, REGISTER, OP_ACCEPT));
        // Don't touch selector registrations here, do all
        // selector handling in the selecting thread (as selector keys are not thread-safe)
        
        selector.wakeup(); 
        // Essential for the first selector registration:
        // before that, the thread is blocked on select with no channel registered,
        // hence sleeping forever.
    }

    synchronized void registerInput(SocketChannel sc, AsyncConnection conn) {
	    SelectionKey key = sc.keyFor(selector);
        if (key == null) {
	        // We could have already enqueued the new registration, as selector
	        // registrations are done later on the selecting thread.
	        // The "else" branch here is just an optimization to avoid waking up the
	        // selector needlessly. But we still need to check for double-registrations
	        // in ChangeAction.REGISTER.execute().
            if (log.isLoggable(FINER))
                log.finer("Enqueued registration for input request for: " + sc);
            pendingChangeRequests.add(new ChangeRequest(sc, REGISTER, OP_READ, conn));
            // Don't touch selector registrations here, do all
            // selector handling in the selecting thread (as selector keys are not thread-safe)
	        selector.wakeup();
        } else {
            // Not changing interest ops for now, to avoid losing/delaying writes. The interest ops
            // will be changed to OP_READ when everything has been written (see write() method).
        }
    }
    
    void deregister(SelectableChannel sc) {
	    pendingChangeRequests.add(new ChangeRequest(sc, CANCEL, -1));
	    selector.wakeup();
    }

    void enqueuedOutput(SocketChannel sc) {
        pendingChangeRequests.add(new ChangeRequest(sc, CHANGEOPS, OP_WRITE));
        selector.wakeup();
    }

    void close(SelectableChannel sc) {
        // ConcurrentLinkedQueue - no synchronization needed
        pendingChangeRequests.add(new ChangeRequest(sc, CLOSE, -1));
    }

    SocketChannel takeAccept(ServerSocketChannel ssc) throws InterruptedException {
        // ConcurrentHashMap and LinkedBlockingQueue, both thread-safe,
        // and no need for atomicity here
        BlockingQueue<SocketChannel> queue = accepted.get(ssc);
        if (log.isLoggable(FINER))
            log.finer("Waiting for accept on server socket: " + ssc);
        return queue.take();
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                updateRegistrations();
                doSelect();
            } catch (Exception e) {
                log.log(SEVERE, "Error in selecting loop", e);
            }
        }
    }

	// This is a field and not a local variable as a small speed optimization: updateRegistrations is called very often
    private final Set<SelectableChannel> modified = new HashSet<SelectableChannel>();
    private void updateRegistrations() throws IOException {
        modified.clear();
        Collection<ChangeRequest> postponed = new LinkedList<ChangeRequest>();
        while (!pendingChangeRequests.isEmpty()) {
            ChangeRequest req = pendingChangeRequests.remove();
            boolean done = req.execute(this, modified);
            if (!done) postponed.add(req);
        }
        pendingChangeRequests.addAll(postponed);
    }

    private void doSelect() throws IOException {
        if (log.isLoggable(FINEST)) {
            log.finest("NIO select, registered keys: \n" + dumpKeys(selector)); 
        }
        selector.select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            // This seems important: without it, we get notified several times
            // for the same event, resulting in eg. NPEs on accept.
            it.remove();

            if (key.isValid()) {
                // TODO: To support large numbers of clients (200+), we should only do the accepts
                // in this thread, and do the reads and writes in a separate thread, with its own
                // selector. See http://stackoverflow.com/questions/843283/asynchronous-channel-close-in-java-nio
                // This is probably the cause of the SocketException: Connection reset that can be
                // seen on the client side with large number of concurrent clients.
                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    write(key);
                } else { // isConnectable
                    assert false : "Should not get here: readyOps = " + key.readyOps();
                }
            }
        }
    }

    private static String dumpKeys(Selector selector) {
        Set<SelectionKey> keys = selector.keys();
        StringBuilder b = new StringBuilder();
        for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext();) {
            SelectionKey key = it.next();
            b.append(key.channel())
                .append(" : ")
                .append(formatKey(key));
            if (it.hasNext()) b.append(", ");
            b.append('\n');
        }
        return b.toString();
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
	    AsyncConnection conn = (AsyncConnection) key.attachment();

        if (log.isLoggable(FINER))
            log.finer("Writing data on: " + conn);
        boolean writtenInFull = true;
        // Write until there's no more data, or the socket's buffer fills up
        while (conn.hasMoreWrites() && writtenInFull) {
            ByteBuffer buf = conn.peekWrite();
	        assert buf != null;
            socketChannel.write(buf);
	        if (log.isLoggable(FINEST))
	            log.finest("Written data, remaining in buf: " + buf.remaining());
            writtenInFull = buf.remaining() == 0;
            if (writtenInFull) {
	            if (log.isLoggable(FINEST)) log.finest("removing write from queue in conn: " + conn);
	            conn.removeWrite();
            }
        }

        if (writtenInFull) {
            // We wrote away all data, so we're no longer interested
            // in writing on this socket. Switch back to waiting for data.
            if (log.isLoggable(FINEST))
                log.finest("Finished writing, changing interest back to OP_READ on: " + socketChannel); 
            key.interestOps(OP_READ);
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if(log.isLoggable(FINEST))
            log.finest("Reading on: " + socketChannel);
        // Clear out our read buffer so it's ready for new data
        readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
        } catch (IOException e) {
            log.log(WARNING, "Remote peer forcibly closed connection, closing channel and cancelling key", e);
            key.cancel();
            socketChannel.close();
            return;
        }

        // socketChannel.read() advances the position, so need to set it back to 0.
        // also set the limit to the previous position, so that the next
        // method will not try to read past what was read from the socket.
        readBuffer.flip();

        try {
	        boolean eof = numRead == -1;
	        AsyncConnection conn = (AsyncConnection) key.attachment();
	        conn.consumeBytesRead((SocketChannel) key.channel(), readBuffer, eof);
        } catch (SJIOException e) {
            log.log(WARNING, "Could not deserialize on channel: " + key.channel(), e);
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
	        log.fine("Remote peer shut down connection cleanly, cleaning up and closing our side: " + socketChannel);
            key.cancel();
            socketChannel.close();
        }

    }

	private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = ssc.accept();
        socketChannel.configureBlocking(false);
        socketChannel.socket().setTcpNoDelay(SJManualTCP.TCP_NO_DELAY);
        BlockingQueue<SocketChannel> queue = accepted.get(ssc);
        if (log.isLoggable(FINER))
            log.finer("Enqueuing accepted socket: "
	            +socketChannel+" for server socket: " + ssc + " in queue: " + queue);
        queue.add(socketChannel);
    }
    
	static String formatKey(SelectionKey key) {
        if (!key.isValid()) return "[cancelled]";
        return formatOps(key.interestOps());
    }

    static String formatOps(int interests) {
        switch (interests) {
            case 1: return "1 (OP_READ)";
            case 4: return "4 (OP_WRITE)";
            case 8: return "8 (OP_CONNECT)";
            case 16: return "16 (OP_ACCEPT)";
            default: return String.valueOf(interests);
        }
    }
}
