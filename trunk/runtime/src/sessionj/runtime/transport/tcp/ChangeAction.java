package sessionj.runtime.transport.tcp;

import sessionj.runtime.util.SJRuntimeUtils;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Set;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import java.util.logging.Logger;

enum ChangeAction {
    REGISTER {
        boolean execute(SelectingThread thread, ChangeRequest req, Set<SelectableChannel> modified) {
            try {
	            if (req.chan.keyFor(thread.selector) == null) {
                    if (log.isLoggable(FINE))
                        log.fine("Registering chan: " + req.chan + ", ops: " + SelectingThread.formatOps(req.interestOps));
                    SelectionKey key = req.chan.register(thread.selector, req.interestOps);
                    key.attach(req.conn);
	            } else if (log.isLoggable(FINER)) {
			            log.finer("Requested registration but chan already registered: " + req.chan);
	            }
			} catch (CancelledKeyException e) {
                if (log.isLoggable(FINE))
                    log.log(FINE, "Tried to register but key cancelled: " + req.chan, e);
            } catch (ClosedChannelException e) {
                // This can happen with the close-protocol selector. In this case,
                // we're fine: it just means the reads are in the inputs queue already.
                if (log.isLoggable(FINE))
                    log.fine("Tried to register but channel already closed: " + req.chan);
            }
            return true;
        }
    }, CHANGEOPS {
        boolean execute(SelectingThread thread, ChangeRequest req, Set<SelectableChannel> modified) {
            SelectionKey key = req.chan.keyFor(thread.selector);
            if (key != null && key.isValid() && key.interestOps() != req.interestOps) {
                if (!modified.contains(req.chan)){
                    if (log.isLoggable(FINER))
                        log.finer("Changing ops for: " + req.chan + " to: " + SelectingThread.formatOps(req.interestOps));
                    key.interestOps(req.interestOps);
                    modified.add(req.chan);
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }, CLOSE {
        boolean execute(SelectingThread thread, ChangeRequest req, Set<SelectableChannel> modified) throws
	        IOException {
            if (log.isLoggable(FINER))
                log.finer("Closing channel: " + req.chan);
            // Implicitly cancels all existing selection keys for that channel (see javadoc).
            req.chan.close();
            return true;
        }
    }, CANCEL {
        boolean execute(SelectingThread thread, ChangeRequest req, Set<SelectableChannel> modified) {
            if (log.isLoggable(FINER))
                log.finer("Deregistering channel: " + req.chan);
            SelectionKey key = req.chan.keyFor(thread.selector);
            if (key != null) key.cancel();
            return true;
        }};

	private static final Logger log = SJRuntimeUtils.getLogger(ChangeAction.class);
    abstract boolean execute(SelectingThread thread, ChangeRequest req, Set<SelectableChannel> modified) throws IOException;
}
