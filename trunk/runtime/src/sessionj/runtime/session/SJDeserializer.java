package sessionj.runtime.session;

import sessionj.runtime.SJIOException;

/**
 */
public interface SJDeserializer {
    OngoingRead newOngoingRead() throws SJIOException;
}
