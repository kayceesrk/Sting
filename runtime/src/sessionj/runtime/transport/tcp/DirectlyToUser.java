package sessionj.runtime.transport.tcp;

import sessionj.runtime.net.SJSocket;

public class DirectlyToUser implements InputState {
    private final SJSocket s;

    public DirectlyToUser(SJSocket s) {
        this.s = s;
    }

    public InputState receivedInput() { return this; }

    public SJSocket sjSocket() {
        return s;
    }
}
