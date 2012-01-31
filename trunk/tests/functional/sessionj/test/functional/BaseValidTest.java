package sessionj.test.functional;

import org.testng.annotations.Test;

import java.util.concurrent.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

import sessionj.runtime.util.NamedThreadFactory;

public abstract class BaseValidTest {
    private static final int STARTING_DELAY = 400;
    protected static final int TEST_PORT = 1234;

    protected abstract List<Callable<?>> peers();

    @Test
    public void runTestcase() throws Exception {
        List<Callable<?>> peers = peers();
        Thread[] threads = new Thread[peers.size()];
        Exception[] exceptions = new Exception[peers.size()];

        for (int i=0; i< peers.size(); ++i) {
            Runnable r = wrapRunnable(
                exceptions, i,
                i == 0 ? peers.get(i) : wrapDelay(peers.get(i))
            );
            threads[i] = new Thread(r, "Testcase Peer"+(i+1));
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        for (Exception e : exceptions) if (e != null) throw e;
    }

    private Runnable wrapRunnable(final Exception[] exceptions, final int i, final Callable<?> callable) {
        return new Runnable() {
            public void run() {
                try {
                    callable.call();
                } catch (Exception e) {
                    exceptions[i] = e;
                }
            }
        };
    }

    private Callable<?> wrapDelay(final Callable<?> peer) {
        return new Callable<Object>() {
            public Object call() throws Exception {
                Thread.sleep(STARTING_DELAY);
                return peer.call();
            }
        };
    }

}
