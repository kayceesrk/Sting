package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * noalias and final-noalias client SJSockets can
 * be passed as method arguments.
 */
public class SocketMethodParameters extends AbstractValidTest {

    public void client(int port) throws Exception {
        final noalias protocol pA { cbegin.!<int> }

        final noalias SJService c = SJService.create(pA, "", port);
        noalias SJSocket s;

        try (s)
		{
			s = c.request();
            doSend(s);
		} finally {
		}
    }

    public void server(int port) throws Exception {
        final noalias protocol pB { sbegin.?(int) }
		final noalias SJServerSocket ss;

		try (ss)
		{
            ss = SJServerSocket.create(pB, port);
            noalias SJSocket s;
            try (s) {
                s = ss.accept();
                doReceive(s);
			} finally {
			}
		} finally {
		}
    }

    private void doReceive(noalias ?(int) s) throws SJIOException {
        try (s) {
            int i = s.receiveInt();
            assert i == 42;
        } finally {}
    }

    private void doSend(noalias !<int> s) throws SJIOException {
        try (s) {
            s.send(42);
        } finally {}   
    }
}
