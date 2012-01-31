package sessionj.runtime.session;

import sessionj.runtime.SJIOException;
import sessionj.runtime.transport.SJConnection;

/**
 *
 */
public interface SJSerializer {
    void close();

    void writeObject(Object o) throws SJIOException;

    void writeByte(byte b) throws SJIOException;

    void writeInt(int i) throws SJIOException;

    void writeBoolean(boolean b) throws SJIOException;

    void writeDouble(double d) throws SJIOException;

    Object readObject() throws SJIOException, ClassNotFoundException, SJControlSignal;

    byte readByte() throws SJIOException, SJControlSignal;

    int readInt() throws SJIOException, SJControlSignal;

    boolean readBoolean() throws SJIOException, SJControlSignal;

    double readDouble() throws SJIOException, SJControlSignal;

    void writeReference(Object o) throws SJIOException; // FIXME: these shouldn't be part of a "serializer" component.

    Object readReference() throws SJIOException, SJControlSignal;

    void writeControlSignal(SJControlSignal cs) throws SJIOException;

    SJControlSignal readControlSignal() throws SJIOException;

    boolean isClosed();

    boolean zeroCopySupported();

    SJMessage nextMessage() throws SJIOException, ClassNotFoundException;//, SJControlSignal; // Get the next messsage regardless of type (wrapped as an SJMessage). Could implement other receive operations in terms of this one, but could be slower.

    SJConnection getConnection();
}
