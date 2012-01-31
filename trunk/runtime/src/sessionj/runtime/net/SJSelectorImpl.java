package sessionj.runtime.net;

import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.SJConnection;
import sessionj.runtime.transport.SJTransport;
import sessionj.runtime.transport.tcp.DirectlyToUser;
import sessionj.runtime.transport.tcp.InputState;
import sessionj.runtime.util.SJRuntimeUtils;
import sessionj.util.Pair;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class SJSelectorImpl implements SJSelector, SJSelectorInternal {
    private static final String UNSUPPORTED = "None of the transports support non-blocking mode";
    private static final Logger log = SJRuntimeUtils.getLogger(SJSelectorImpl.class);
	
	private final BlockingQueue<Object> readyForSelect = new LinkedBlockingQueue<Object>();
	private final Map<SJConnection, InputState> registrations = new HashMap<SJConnection, InputState>();
	private final Set<SJServerSocket> serverSockets = new HashSet<SJServerSocket>();

	@SuppressWarnings({"MethodParameterOfConcreteClass"})
    public void registerAccept(SJServerSocket ss) throws SJIOException {
	    Set<SJTransport> activeTransports = ss.activeTransports();
	    Collection<Boolean> results = new HashSet<Boolean>();
      	
		log.finer("registerAccept, ss: " + ss + ", activeTransports: " + activeTransports);
        for (SJTransport transport : activeTransports)
            try {
	            results.add(
		            transport.transportSelector().registerAccept(this, ss)
	            );
            } catch (Exception e) {
                throw new SJIOException(e);
            }
	    checkResults(results);
		// Only if checkResults didn't throw an exception
		serverSockets.add(ss);
    }

    public void registerInput(SJSocket s) throws SJIOException {
	    boolean accepted;
	    try {
		    accepted = s.transportSelector()
			    .registerInput(this, s.getConnection());
		    registrations.put(s.getConnection(), new DirectlyToUser(s));
	    } catch (Exception e) {
		    throw new SJIOException(e);
	    }
	    if (!accepted) throw new SJIOException(UNSUPPORTED);
    }

    private void checkResults(Collection<Boolean> results) throws SJIOException {
        if (!results.contains(true)) throw new SJIOException(UNSUPPORTED);
    }

    public SJSocket select() throws SJIOException, SJIncompatibleSessionException {
	    while (true) {
		    Object chan = dequeue();
		    if (chan instanceof SJConnection) {
			    SJConnection conn = (SJConnection) chan;
			    InputState state = registrations.get(conn);
			    InputState newState = state.receivedInput();
			    registrations.put(conn, newState);
			    SJSocket s = newState.sjSocket();
			    if (s == null) {
				    log.finest("Read: InputState not complete: looping in select");
			    } else if (s.remainingSessionType() == null) {
				    // User-level inputs all done - this must be from the close protocol
				    if (log.isLoggable(Level.FINER))
					    log.finer("remainingSessionType is null: looping in select and deregistering socket " + s);
				    // The close protocol only does one single read, which has already been read by now.
				    // So we can deregister the channel - the data for the read is already in the connection
				    // object's available reads queue.
				    deregister(s, conn);
			    } else {
				    return s;
			    }
		    } else {
			    Pair<SJServerSocket, SJConnection> p = (Pair<SJServerSocket, SJConnection>) chan;

			    registerOngoingAccept(p.first, p.second);

			    InputState initialState = registrations.get(p.second);
			    SJSocket s = initialState.sjSocket();
			    if (s == null) {
				    log.finest("Accept: InputState not complete: looping in select");
			    } else {
				    return s;
			    }
		    }
	    }
    }

	private Object dequeue() throws SJIOException {
		Object chan;
		try {
			log.finest("Blocking dequeue...");
			chan = readyForSelect.take();
			if (log.isLoggable(Level.FINER)) log.finer("Channel selected: " + chan);
		} catch (InterruptedException e) {
			throw new SJIOException(e);
		}
		return chan;
	}

	private void deregister(SJSocket s, SJConnection connection) {
		s.transportSelector().deregisterInput(connection);

		registrations.remove(s);
	}

	private void registerOngoingAccept(SJServerSocket sjss, SJConnection conn) throws SJIOException, SJIncompatibleSessionException {
		// OK even if we only do outputs: enqueing write requests will change the interest
		// set for the channel. Input is the default interest that we go back to after 
		// everything is written.
		log.finest("sjss: " + sjss + ", conn: " + conn);
		conn.getTransport().transportSelector().registerInput(this, conn);
		sjss.getParameters().setExpectedConnectionHack(conn);
        registrations.put(conn, sjss.getParameters().getAcceptProtocol().initialAcceptState(sjss));
	}

    public void close() {
	    for (SJServerSocket ss : serverSockets) {
		    Set<SJTransport> socketTransports = ss.activeTransports();
		    for (SJTransport tr : socketTransports) {
			    tr.transportSelector().deregisterAccept(ss, this);
		    }
	    }
	    for (SJConnection conn : registrations.keySet()) {
		    conn.getTransport().transportSelector().deregisterInput(conn);
	    }
	    if (log.isLoggable(Level.FINE))
		    log.fine("Closed selector: " + this);
    }

	public void notifyInput(SJConnection channel) {
		readyForSelect.add(channel);
	}

	public void notifyAccept(SJServerSocket sjss, SJConnection connection) {
		readyForSelect.add(new Pair<SJServerSocket, SJConnection>(sjss, connection));
	}

	@Override
	public String toString() {
		return "SJSelectorImpl{" +
			"readyForSelect=" + readyForSelect +
			", registrations=" + registrations +
			", serverSockets=" + serverSockets +
			'}';
	}
}
