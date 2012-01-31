package sessionj.test.functional;

import java.util.concurrent.Callable;
import java.util.List;
import java.util.LinkedList;

public abstract class AbstractValidTest3Peers extends BaseValidTest {

    protected abstract void peer1(int port) throws Exception;
    protected abstract void peer2(int port) throws Exception;
    protected abstract void peer3(int port) throws Exception;

    protected List<Callable<?>> peers() {
        List<Callable<?>> peers = new LinkedList<Callable<?>>();
        peers.add(
            new Callable<Object>() {
                public Object call() throws Exception {
                    peer1(TEST_PORT);
                    return null;
                }
            });
        peers.add(new Callable<Object>() {
            public Object call() throws Exception {
                peer2(TEST_PORT);
                return null;
            }
        });
        peers.add(new Callable<Object>() {
            public Object call() throws Exception {
                peer3(TEST_PORT);
                return null;
            }
        });
        return peers;
    }
}
