package sessionj.runtime.transport.tcp;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSocket;

public class WaitInitialInputIfNeeded implements InputState {
    private final boolean startsWithInput;
    private final SJSocket s;

    public WaitInitialInputIfNeeded(SJSocket s) throws SJIOException {
        this.s = s;
        startsWithInput = !s.typeStartsWithOutput();
    }

    public InputState receivedInput() {
        return new DirectlyToUser(s);
    }

    public SJSocket sjSocket() {
        if (startsWithInput) return null;
        else return s;
    }
}
