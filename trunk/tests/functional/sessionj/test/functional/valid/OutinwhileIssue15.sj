//DISABLED
// Needs fixing: locks up after an assertion failure in peer3.
package sessionj.test.functional;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

/*
 * This reproduces issue 15: break after an outinwhile.
 */
public class OutinwhileIssue15 extends AbstractValidTest3Peers {
    final noalias protocol pOut { sbegin.![]* }
    final noalias protocol pIn { ^(pOut) }
    
    static final int ITERATIONS = 3;

    public void peer1(int port) throws Exception {
        final noalias SJServerSocket ss1, ss2;
        final noalias SJSocket s1, s2;
        int i = 0;
        
        try (ss1) {
			ss1 = SJServerSocketImpl.create(pOut, port);
            try (ss2) {
                ss2 = SJServerSocketImpl.create(pOut, port+1);
                try (s1,s2) {
                    s1 = ss1.accept();
                    s2 = ss2.accept();
                    <s1,s2>.outwhile(i < ITERATIONS) {
                        i++;
                    }
                } finally {
                }
		    } finally {
            }
        } finally {
            assert i == ITERATIONS;        
        }
    }

    public void peer2(int port) throws Exception {
        final noalias SJServerSocket ss1;
        final noalias SJSocket sIn1, sIn2, sOut;
        final noalias SJService c1 = SJService.create(pIn, "", port);
        final noalias SJService c2 = SJService.create(pIn, "", port+1);

        try (ss1) {
            ss1 = SJServerSocketImpl.create(pOut, port+2);
            while (true) {
                try (sIn1, sOut, sIn2) {
                    sOut = ss1.accept();
                    sIn1 = c1.request();
                    sIn2 = c2.request();
                    sOut.outwhile(<sIn1,sIn2>.inwhile()) {
                    }
                    break;
                } finally {
                }
            }
        } finally {
        }
    }

    public void peer3(int port) throws Exception {
        final noalias SJSocket sIn;
        final noalias SJService c = SJService.create(pIn, "", port+2);
        int count = 0;
        try (sIn) {
            sIn = c.request();
            sIn.inwhile() {
                count++;
            }
        } finally {
            assert count == ITERATIONS;
        }
    }
}
