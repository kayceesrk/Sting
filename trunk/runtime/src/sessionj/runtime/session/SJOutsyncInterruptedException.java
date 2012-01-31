package sessionj.runtime.session;

import sessionj.runtime.SJIOException;

/**
 *
 */
public class SJOutsyncInterruptedException extends SJIOException {
    public SJOutsyncInterruptedException() {
    }

    public SJOutsyncInterruptedException(String message) {
        super(message);
    }
}
