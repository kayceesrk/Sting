package sessionj.runtime.session;

import sessionj.runtime.SJIOException;

import java.nio.ByteBuffer;

/**
 */
public interface OngoingRead {
    void updatePendingInput(ByteBuffer bytes, boolean eof) throws SJIOException;

    boolean finished();

    ByteBuffer getCompleteInput() throws SJIOException;
}
