package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Issue10 extends AbstractValidTest {
    final noalias protocol p1 { cbegin.!<int> }
    final noalias protocol p2 { ^(p1) }

    public void server(int port) throws Exception {
        final noalias SJServerSocket ss;

        try (ss) {
            ss = SJServerSocket.create(p2, port);
            noalias SJSocket s;
            try (s) {
                s = ss.accept();

                int i = s.receiveInt();
                assert i == 42;
            } finally {
            }
        } finally {
        }
    }

    public void client(int port) throws Exception {
        noalias SJSocket s;
        final noalias SJService c1 = SJService.create(p1, "", port);

        try (s)
        {
            s = c1.request();
            s.send(42);
        } finally {
        }
    }
}
