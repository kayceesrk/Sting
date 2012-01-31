package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * Noalias SJSockets should not be nulled out
 * after a session operation, like they are
 * when passed as parameters to a normal method.
 * (This reproduces issue 4).
 */
public class NoAliasSocket extends AbstractValidTest {

    public void client(int port) throws Exception {
        protocol pA { cbegin.!<int>.!<int> }

        final noalias SJService c = SJService.create(pA, "", port);
        noalias SJSocket s;

        try (s)
		{
			s = c.request();

			s.send(41);
		    s.send(42);
		} finally {
		}
    }

    public void server(int port) throws Exception {
        protocol pB { sbegin.?(int).?(int) }
		final noalias SJServerSocket ss;

		try (ss)
		{
            ss = SJServerSocket.create(pB, port);
            noalias SJSocket s;
            try (s) {
                s = ss.accept();

                int i = s.receiveInt();
                assert i == 41;
                i = s.receiveInt();
                assert i == 42;
			} finally {
			}
		} finally {
		}
    }
}
