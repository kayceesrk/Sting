package sessionj.runtime.net;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.session.SJStateManager;
import sessionj.runtime.transport.SJConnection;
import sessionj.types.sesstypes.SJSessionType;

@SuppressWarnings({"ALL"})
public class MultiPartyInsyncTest {
    private SJSocket sockContinue;
    private SJSocket[] sockets;
    private SJSocket sockStop;

    /**
     * Only the insync() method is of interest for this test.
     */
    static class DummySocket implements SJSocket {
        private final boolean willContinue;

        DummySocket(boolean willContinue) {
            this.willContinue = willContinue;
        }

        public void close() {
        }

        public void send(Object o) throws SJIOException {
        }

        public void sendInt(int i) throws SJIOException {
        }

        public void sendBoolean(boolean b) throws SJIOException {
        }

        public void sendDouble(double d) throws SJIOException {
        }

        public void pass(Object o) throws SJIOException {
        }

        public void copy(Object o) throws SJIOException {
        }

        public Object receive() throws SJIOException, ClassNotFoundException {
            return null;
        }

        public int receiveInt() throws SJIOException {
            return 0;
        }

        public boolean receiveBoolean() throws SJIOException {
            return false;
        }

        public double receiveDouble() throws SJIOException {
            return 0;
        }

        public void outlabel(String lab) throws SJIOException {
        }

        public String inlabel() throws SJIOException {
            return null;
        }

        public boolean outsync(boolean bool) throws SJIOException {
            return false;
        }

        public boolean insync() throws SJIOException {
            return willContinue;
        }

        public boolean isPeerInterruptibleOut(boolean selfInterrupting) throws SJIOException {
            return false;
        }

        public boolean isPeerInterruptingIn(boolean selfInterruptible) throws SJIOException {
            return false;
        }

        public boolean interruptibleOutsync(boolean condition) throws SJIOException {
            return false;
        }

        public boolean interruptingInsync(boolean condition, boolean peerInterruptible) throws SJIOException {
            return false;
        }

        public void sendChannel(SJService c, String encoded) throws SJIOException {
        }

        public SJService receiveChannel(String encoded) throws SJIOException {
            return null;
        }

        public void delegateSession(SJAbstractSocket s, String encoded) throws SJIOException {
        }

        public SJAbstractSocket receiveSession(String encoded, SJSessionParameters params) throws SJIOException {
            return null;
        }

        public SJProtocol getProtocol() {
            return null;
        }

        public String getHostName() {
            return null;
        }

        public int getPort() {
            return 0;
        }

        public String getLocalHostName() {
            return null;
        }

        public int getLocalPort() {
            return 0;
        }

        public SJSessionParameters getParameters() {
            return null;
        }

        public SJConnection getConnection() {
            return null;
        }

        public void reconnect(SJConnection connection) throws SJIOException {
        }

        public void setHostName(String hostAddress) {
        }

        public void setPort(int port) {
        }

        public int typeLabel() throws SJIOException {
            return 0;
        }

        public SJStateManager getStateManager() {
            return null;
        }

        public void setStateManager(SJStateManager sm) {
        }

        public SJSessionType currentSessionType() {
            return null;
        }

        public SJSessionType remainingSessionType() {
            return null;
        }

        public boolean typeStartsWithOutput() {
            return false;
        }

        public SJSessionType getInitialRuntimeType() {
            return null;
        }

	    public boolean supportsBlocking() {
		    return false;
	    }

	    public boolean arrived() {
		    return false;
	    }

	    public TransportSelector transportSelector() {
		    return null;
	    }

	    public boolean recurse(String lab) throws SJIOException {
            return false;
        }

        public boolean recursionEnter(String lab) throws SJIOException {
            return false;
        }

        public boolean recursionExit() throws SJIOException {
            return false;
        }
    }

    @BeforeTest
    public void createSockets() {
        sockContinue = new DummySocket(true);
        sockStop = new DummySocket(false);
    }

    @Test
    public void allSocketsContinue() throws SJIOException {
        sockets = new SJSocket[]{sockContinue, sockContinue, sockContinue};
        assert SJRuntime.insync(sockets);
    }

    @Test
    public void allSocketsStop() throws SJIOException {
        sockets = new SJSocket[]{sockStop, sockStop, sockStop};
        assert !SJRuntime.insync(sockets);
    }

    @Test(expectedExceptions = SJIOException.class)
    public void oneSocketStopOthersContinue() throws SJIOException {
        sockets = new SJSocket[]{sockStop, sockContinue, sockStop};
        SJRuntime.insync(sockets);
    }

}
