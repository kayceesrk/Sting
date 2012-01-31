package sessionj.runtime.session;

import static org.easymock.EasyMock.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSocket;

/**
 *
 */
public class OutsyncInsyncProtocolsTest {
    private SJSerializer mock;
    private SJSessionProtocols sp;

    @BeforeMethod
    protected void setUp() {
        mock = createMock(SJSerializer.class);
        SJSocket s = createNiceMock(SJSocket.class);
        sp = new SJSessionProtocolsImpl(s, mock);
    }

    // Case 1:
    // Both interruptible outsync and interrupting insync

    @Test
    public void outsyncNotFinishedPeerDoesntInterrupt() throws SJIOException, SJControlSignal {
        mock.writeBoolean(true);
        expect(mock.readBoolean()).andReturn(true);
        
        replay(mock);
        assert sp.interruptibleOutsync(true);
        verify(mock);
    }

    @Test
    public void insyncNotInterruptingPeerIsNotFinished() throws SJIOException, SJControlSignal {
        expect(mock.readBoolean()).andReturn(true);
        mock.writeBoolean(true);

        replay(mock);
        assert sp.interruptingInsync(true, true);
        verify(mock);
    }
    @Test
    public void outsyncNotFinishedPeerInterrupts() throws SJIOException, SJControlSignal {
        mock.writeBoolean(true);
        expect(mock.readBoolean()).andReturn(false);

        replay(mock);
        assert !sp.interruptibleOutsync(true);
        verify(mock);
    }

    @Test
    public void insyncNotInterruptingPeerIsFinished() throws SJIOException, SJControlSignal {
        expect(mock.readBoolean()).andReturn(false);
        // no write of local condition, mirrors next test
        replay(mock);
        assert !sp.interruptingInsync(true, true);
        verify(mock);
    }

    @Test
    public void outsyncIsFinished() throws SJIOException {
        mock.writeBoolean(false);
        // no read of remote condition, stopping right away - saves a message
        replay(mock);
        assert !sp.interruptibleOutsync(false);
        verify(mock);
    }

    @Test
    public void insyncInterrupting() throws SJIOException, SJControlSignal {
        expect(mock.readBoolean()).andReturn(true);
        mock.writeBoolean(false);

        replay(mock);
        assert !sp.interruptingInsync(false, true);
        verify(mock);
    }

    @DataProvider(name = "sjsignals")
    public Object[][] allSJSignals() {
        return new Object[][] {
                new Object[] { new SJFIN() },
                new Object[] { new SJDelegationACK() },
                new Object[] { new SJDelegationSignal("localhost", 1234) }
        };
    }
    @Test(expectedExceptions = SJIOException.class, dataProvider = "sjsignals")
    public void outsyncNotFinishedControlSignalRaised(Throwable signal) throws SJIOException, SJControlSignal {
        mock.writeBoolean(true);
        expect(mock.readBoolean()).andThrow(signal);

        replay(mock);
        sp.interruptibleOutsync(true);
    }

    @Test(expectedExceptions = SJIOException.class, dataProvider = "sjsignals")
    public void insyncNotInterruptingControlSignalRaised(Throwable signal) throws SJIOException, SJControlSignal {
        expect(mock.readBoolean()).andThrow(signal);

        replay(mock);
        sp.interruptingInsync(true, true);
    }

    // Case 2:
    // Interrupting insync with normal outsync on the other end
    // Only adding the cases where behaviour is changed

    @Test(expectedExceptions = SJOutsyncInterruptedException.class)
    public void insyncInterruptingNonInterruptiblePeerNotFinished() throws SJControlSignal, SJIOException {
        expect(mock.readBoolean()).andReturn(true);

        replay(mock);
        sp.interruptingInsync(false, false);
    }

    @Test
    public void insyncInterruptingNonInterruptiblePeerIsFinished() throws SJControlSignal, SJIOException {
        expect(mock.readBoolean()).andReturn(false);

        replay(mock);
        assert !sp.interruptingInsync(false, false);
        verify(mock);
    }
}
