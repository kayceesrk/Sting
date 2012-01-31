package sessionj.test.functional;

import org.testng.annotations.Test;

import java.util.concurrent.*;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 */
public abstract class AbstractValidTest extends BaseValidTest {

    protected abstract void server(int port) throws Exception;
    protected abstract void client(int port) throws Exception;

    protected List<Callable<?>> peers() {
        List<Callable<?>> peers = new LinkedList<Callable<?>>();
        peers.add(
            new Callable<Object>() {
                public Object call() throws Exception {
                    server(TEST_PORT);
                    return null;
                }
            });
        peers.add(new Callable<Object>() {
            public Object call() throws Exception {
                client(TEST_PORT);
                return null;
            }
        });
        return peers;
    }
}
