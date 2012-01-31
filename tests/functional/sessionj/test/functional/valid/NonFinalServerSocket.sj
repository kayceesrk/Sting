package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * New feature: allow SJServerSockets to be non-final
 * (required by events implementation)
 */
public class NonFinalServerSocket extends AbstractValidTest {
    public void client(int port) throws Exception {
        protocol pA { cbegin.!<int> }
        final noalias SJService c = SJService.create(pA, "", port);
        noalias SJSocket s;

        try (s)
		{
			s = c.request();
		    s.send(42);
		} finally {
		}
    }

    public void server(int port) throws Exception {
        protocol pB { sbegin.?(int) }
		noalias SJServerSocket ss;

		try (ss)
		{
            ss = SJServerSocket.create(pB, port);
            doStuff(ss);                 
		} finally {
		}
    }

    private void doStuff(noalias sbegin.?(int) ss) throws SJIOException, SJIncompatibleSessionException {
        try (ss) {
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
}
