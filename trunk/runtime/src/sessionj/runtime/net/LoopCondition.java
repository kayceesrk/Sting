package sessionj.runtime.net;

import sessionj.runtime.SJIOException;

public interface LoopCondition {
    boolean call(boolean arg) throws SJIOException;
}
