package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * This reproduces issue 8.
 * Verifies that the order of session socket targets
 * for inwhile does not change runtime behaviour.
 */
public class OutwhileIssue8 extends AbstractValidTest {

    public void client(int port) throws Exception {
        final noalias protocol pro_first { cbegin.![!<int>]* }
        final noalias protocol pro_second { cbegin.![?(int)]* }

        final noalias SJService c_first = SJService.create(pro_first, "", port);
        final noalias SJService c_second = SJService.create(pro_second, "", port+1);

        final noalias SJServerSocket ss;
        final int ITERATIONS = 2;

        try (ss) {
            int i=0;
            while(true) {
         	    final noalias SJSocket fst, snd;
         	    try (fst,snd) {
	                fst = c_first.request();
                    snd = c_second.request();
	                <snd,fst>.outwhile(i < ITERATIONS) {
                        int current = 100;
                        fst.send(current);
                        current = snd.receiveInt();
                        i++;
                    }
                    break;
         	    } finally {
         	    }
            }
            assert i == ITERATIONS;
        } finally {
        }

    }

    public void server(int port) throws Exception {
        final noalias protocol pro_first { sbegin.?[?(int)]* }
        final noalias protocol pro_second { sbegin.?[!<int>]* }

        final noalias SJServerSocket ss_second, ss_first;

        try (ss_second) {
            ss_second = SJServerSocketImpl.create(pro_second, port+1);
            try (ss_first) {
                ss_first = SJServerSocketImpl.create(pro_first, port);
                while (true) {
                    final noalias SJSocket fst,snd;
                    try (snd,fst) {
                        fst = ss_first.accept();
                        snd = ss_second.accept();
                        <fst,snd>.inwhile() {
                            int current;
                            current = fst.receiveInt();
                            snd.send(current);
                        }
                        break;
                    } finally {
                    }
                }
            } finally {
            }
        } finally {
        }
    }
}
